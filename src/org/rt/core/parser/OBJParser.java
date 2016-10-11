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

import org.rt.core.AbstractPrimitive;
import org.rt.core.Material;
import org.rt.core.color.Color;
import org.rt.util.IntArray;
import org.rt.core.primitive.Geometry;
import org.rt.core.shape.TriangleMesh;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class OBJParser 
{
    public static ArrayList<TriangleMesh> meshes = new ArrayList<>();    
    public static ArrayList<AbstractPrimitive> primitiveList = new ArrayList<>();
    
    public static void main(String... args)
    {
        read("C:\\Users\\user\\Documents\\Scene3d\\simplebox\\box.obj");
    }
    
    private static void clear()
    {
        TriangleMesh.clear();
        meshes.clear();
        primitiveList.clear();
    }
    
    public static ArrayList<AbstractPrimitive> read(String uri)
    {
        return read(new File(uri).toURI());
    }
    
    public static ArrayList<AbstractPrimitive> read(URI uri)
    {
        //Clear buffer first
        clear();
        
        //Init string parser i/o stream
        StringParser parser = new StringParser(uri);
        
        //Start the parsing
        while(parser.hasNext())
        {           
            String peekToken = parser.peekNextToken();            
            switch (peekToken) {
                case "o":
                    readObject(parser);
                    break; 
                case "g":
                    readGroup(parser);
                    break;
                case "v":
                    readVertex(parser);
                    break;
                case "vn":                    
                    readNormal(parser);
                    break;
                case "vt":
                    readUV(parser);
                    break;
                case "f":
                    readFaces(parser);
                    break;
                default:
                    parser.getNextToken();
                    break;
            }            
        }
        
        for(TriangleMesh mesh : meshes)
        {                     
            Material material = Material.createLambert(Color.LIGHTGRAY);
            Geometry geometry = new Geometry(material);
            geometry.addGeometryPrimitive(mesh);
            primitiveList.add(geometry);
        }                              
        return primitiveList;       
    }
    
    protected static void readNormal(StringParser parser)
    {
        if(parser.getNextToken().equals("vn"))    
        {            
            TriangleMesh.addNormal(parser.getNextFloat(), parser.getNextFloat(), parser.getNextFloat());
        }         
    }
    
    protected static void readVertex(StringParser parser)
    {        
        if(parser.getNextToken().equals("v"))
        {            
            TriangleMesh.addVertex(parser.getNextFloat(), parser.getNextFloat(), parser.getNextFloat());
        }
    }
    
    protected static void readUV(StringParser parser)
    {
        if(parser.getNextToken().equals("vt"))
        {            
            TriangleMesh.addUV(parser.getNextFloat(), parser.getNextFloat());
        }
    }
    
    protected static void readObject(StringParser parser)
    {
        if(parser.getNextToken().equals("o"))
        {
            String name = parser.getNextToken();             
            meshes.add(new TriangleMesh(name));
        }
    }
    
    protected static void readGroup(StringParser parser)
    {
        if(parser.getNextToken().equals("g"))
        {
            String name = parser.getNextToken();              
            meshes.add(new TriangleMesh(name));
            System.out.println("Kubafu");
        }
    }
    
    protected static void readFaces(StringParser parser)
    {
        IntArray intArray = new IntArray();
        boolean doubleBackSlash = parser.getLine().contains("//");
        
        parser.skipTokens(1); //parser.skip("f")
        
        while(parser.hasNext() && parser.peekNextTokenIsNumber())  
            intArray.add(parser.getNextInt());
        
        if(intArray.size() == 3)
        {
            read_triangle(intArray);
        }
        else if(intArray.size() == 4)
        {
            read_quad(intArray);
        }
        else if(intArray.size() == 6 && doubleBackSlash)
        {
            read_triangle_with_normals(intArray);
        }
        else if(intArray.size() == 6)
        {
            read_triangle_with_uvs(intArray);
        }       
        else if(intArray.size() == 8 && doubleBackSlash)
        {           
            read_quad_with_normals(intArray);            
        }
         else if(intArray.size() == 8)
        {                        
            read_quad_with_uvs(intArray);
        }
        else if(intArray.size() == 9)
        {
            read_triangle_with_uvs_normals(intArray);
        }
        else if(intArray.size() == 12)
        {
            read_quad_with_uvs_normals(intArray);
        }    
    }
    
    public static void read_triangle(IntArray intArray)
    {
        int[] array = intArray.trim();
        TriangleMesh mesh = meshes.get(meshes.size()-1);
             
        mesh.addVertexIndex(array[0], array[1], array[2]);        
    }
    
    public static void read_quad(IntArray intArray)
    {
        int[] array = intArray.trim();
        TriangleMesh mesh = meshes.get(meshes.size()-1);
        
        mesh.addVertexIndex(array[0], array[1], array[2]); //p1 p2 p3
        mesh.addVertexIndex(array[0], array[2], array[3]); //p1 p3 p4        
    }
    
     public static void read_triangle_with_normals(IntArray intArray)
    {
        int[] array = intArray.trim();
        TriangleMesh mesh = meshes.get(meshes.size()-1);
        
        mesh.addVertexIndex(array[0], array[2], array[4]);
        mesh.addNormalIndex(array[1], array[3], array[5]);        
    }
    
    public static void read_triangle_with_uvs(IntArray intArray)
    {
        int[] array = intArray.trim();
        TriangleMesh mesh = meshes.get(meshes.size()-1);
        
        mesh.addVertexIndex(array[0], array[2], array[4]);
        mesh.addUVIndex(array[1], array[3], array[5]);        
    }
    
    public static void read_quad_with_uvs(IntArray intArray)
    {
        int[] array = intArray.trim();
        TriangleMesh mesh = meshes.get(meshes.size()-1);
        
        mesh.addVertexIndex(array[0], array[2], array[4]);
        mesh.addVertexIndex(array[0], array[4], array[6]);
        
        mesh.addUVIndex(array[1], array[3], array[5]);
        mesh.addUVIndex(array[1], array[5], array[7]);         
    }
    
    public static void read_quad_with_normals(IntArray intArray)
    {
        int[] array = intArray.trim();
        TriangleMesh mesh = meshes.get(meshes.size()-1);
        
        mesh.addVertexIndex(array[0], array[2], array[4]);
        mesh.addVertexIndex(array[0], array[4], array[6]);
        
        mesh.addNormalIndex(array[1], array[3], array[5]);
        mesh.addNormalIndex(array[1], array[5], array[7]);
        
    }
    
    public static void read_triangle_with_uvs_normals(IntArray intArray)
    {
        int[] array = intArray.trim();
        TriangleMesh mesh = meshes.get(meshes.size()-1);
        
        mesh.addVertexIndex(array[0], array[3], array[6]);
        mesh.addUVIndex(array[1], array[4], array[7]);
        mesh.addNormalIndex(array[2], array[5], array[8]);        
    }
    
    public static void read_quad_with_uvs_normals(IntArray intArray)
    {
        int[] array = intArray.trim();
        TriangleMesh mesh = meshes.get(meshes.size()-1);
        
        mesh.addVertexIndex(array[0], array[3], array[6]);
        mesh.addVertexIndex(array[0], array[6], array[9]);
        
        mesh.addUVIndex(array[1], array[4], array[7]);
        mesh.addUVIndex(array[1], array[7], array[10]);
        
        mesh.addNormalIndex(array[2], array[5], array[8]);     
        mesh.addNormalIndex(array[2], array[8], array[11]);          
    }
    
    
}
