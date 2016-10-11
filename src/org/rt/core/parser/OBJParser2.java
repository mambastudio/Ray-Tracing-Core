/*
 * The MIT License
 *
 * Copyright 2016 user.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.rt.core.parser;

import java.io.File;
import java.net.URI;
import org.rt.util.IntArray;
import org.rt.core.parser.data.MeshData;
import static org.rt.core.parser.data.MeshData.TriangleType.FACE;
import static org.rt.core.parser.data.MeshData.TriangleType.FACE_NORMAL;
import static org.rt.core.parser.data.MeshData.TriangleType.FACE_UV;
import static org.rt.core.parser.data.MeshData.TriangleType.FACE_UV_NORMAL;
import org.rt.core.system.Timer;

/**
 *
 * @author user
 */
public class OBJParser2 
{
    public static void main(String... args)
    {
        Timer timer = new Timer();
        timer.start();
        read("C:\\Users\\user\\Documents\\Scene3d\\simplebox\\hair.obj");
        timer.end();
        
        System.out.println(timer);
    }
    
    public static MeshData read(String uri)
    {
        return read(new File(uri).toURI());
    }
    
    public static MeshData read(URI uri)
    {
        MeshData data = new MeshData();        
        StringParser parser = new StringParser(uri);
        
        while(parser.hasNext())
        {            
            String peekToken = parser.peekNextToken();            
            switch (peekToken) {
                case "o":
                    readObject(parser, data);
                    break;
                case "g":
                    readGroup(parser, data);
                    break;
                case "v":
                    readVertex(parser, data);
                    break;
                case "vn":                    
                    readNormal(parser, data);
                    break;
                case "vt":
                    readUV(parser, data);
                    break;
                case "f":
                    readFaces(parser, data);
                    break;
                default:
                    parser.getNextToken();
                    break;
            }
        }
        
        System.out.println(data);
        return data;
    }
    
    private static void readObject(StringParser parser, MeshData data) {
        if(parser.getNextToken().equals("o"))
        {
            data.addObject(parser.getNextToken());
        }
    }

    private static void readGroup(StringParser parser, MeshData data)  {
        if(parser.getNextToken().equals("g"))
        {
            data.addGroup(parser.getNextToken());
        }
    }

    private static void readVertex(StringParser parser, MeshData data) {
        if(parser.getNextToken().equals("v"))
        {
            data.addVertex(parser.getNextFloat(), parser.getNextFloat(), parser.getNextFloat());
        }
    }

    private static void readNormal(StringParser parser, MeshData data) {
        if(parser.getNextToken().equals("vn"))
        {
            data.addNormal(parser.getNextFloat(), parser.getNextFloat(), parser.getNextFloat());
        }
    }

    private static void readUV(StringParser parser, MeshData data) {
        if(parser.getNextToken().equals("vt"))
        {
            data.addTexCoord(parser.getNextFloat(), parser.getNextFloat());
        }
    }
    
    private static void readFaces(StringParser parser, MeshData mesh) {
        IntArray intArray = new IntArray();
        boolean doubleBackSlash = parser.getLine().contains("//");
        
        parser.skipTokens(1); //parser.skip("f")
        
        while(parser.hasNext() && parser.peekNextTokenIsNumber())  
            intArray.add(parser.getNextInt());
        
        if(intArray.size() == 3)
        {
            int[] array = intArray.trim();            
            mesh.addTriangle(FACE, array[0], array[1], array[2]);       
        }
        else if(intArray.size() == 4)
        {
            int[] array = intArray.trim();        
            mesh.addTriangle(FACE, array[0], array[1], array[2]); //p1 p2 p3
            mesh.addTriangle(FACE, array[0], array[2], array[3]); //p1 p3 p4        
        }
        else if(intArray.size() == 6 && doubleBackSlash)
        {
            int[] array = intArray.trim();        
            mesh.addTriangle(FACE_NORMAL, array[0], array[2], array[4],
                                          array[1], array[3], array[5]);
        }
        else if(intArray.size() == 6)
        {
            int[] array = intArray.trim();        
            mesh.addTriangle(FACE_UV, array[0], array[2], array[4],
                                      array[1], array[3], array[5]);
        }       
        else if(intArray.size() == 8 && doubleBackSlash)
        {           
            int[] array = intArray.trim();        
            mesh.addTriangle(FACE_NORMAL, array[0], array[2], array[4],
                                          array[1], array[3], array[5]);
            mesh.addTriangle(FACE_NORMAL, array[0], array[4], array[6],
                                          array[1], array[5], array[7]);     
        }
         else if(intArray.size() == 8)
        {                        
            int[] array = intArray.trim();        
            mesh.addTriangle(FACE_UV, array[0], array[2], array[4],
                                      array[1], array[3], array[5]);        
            mesh.addTriangle(FACE_UV, array[0], array[4], array[6],
                                      array[1], array[5], array[7]); 
        }
        else if(intArray.size() == 9)
        {
            int[] array = intArray.trim();        
            mesh.addTriangle(FACE_UV_NORMAL, array[0], array[3], array[6],                             
                                             array[1], array[4], array[7],
                                             array[2], array[5], array[8]); 
        }
        else if(intArray.size() == 12)
        {
            int[] array = intArray.trim();        
            mesh.addTriangle(FACE_UV_NORMAL, array[0], array[3], array[6],                             
                                             array[1], array[4], array[7],
                                             array[2], array[5], array[8]);   

            mesh.addTriangle(FACE_UV_NORMAL, array[0], array[6], array[9],                             
                                             array[1], array[7], array[10],
                                             array[2], array[8], array[11]); 
        }    
    }    
}
