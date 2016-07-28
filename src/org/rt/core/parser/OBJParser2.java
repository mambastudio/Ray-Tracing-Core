/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.parser;

import org.rt.core.AbstractPrimitive;
import org.rt.core.Intersection;
import org.rt.core.Material;
import org.rt.core.accelerator.NullAccelerator;
import org.rt.core.color.Color;
import org.rt.core.coordinates.Point3f;
import org.rt.core.coordinates.Vector3f;
import org.rt.core.math.IntArray;
import org.rt.core.math.Ray;
import org.rt.core.primitive.Geometry;
import org.rt.core.shape.TriangleMesh;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class OBJParser2 
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
        
        for(AbstractPrimitive prim : primitiveList)
            prim.build();
                
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
        }
    }
    
    protected static void readFaces(StringParser parser)
    {
        IntArray intArray = new IntArray();
        boolean doubleBackSlash = parser.getLine().contains("//");
        
        parser.skipTokens(1); //parser.skip("f")
        
        while(parser.hasNext() && parser.peekNextTokenIsNumber())  
            intArray.add(parser.getNextInt());
        
        if(intArray.getSize() == 3)
        {
            read_triangle(intArray);
        }
        else if(intArray.getSize() == 4)
        {
            read_quad(intArray);
        }
        else if(intArray.getSize() == 6 && doubleBackSlash)
        {
            read_triangle_with_normals(intArray);
        }
        else if(intArray.getSize() == 6)
        {
            read_triangle_with_uvs(intArray);
        }       
        else if(intArray.getSize() == 8 && doubleBackSlash)
        {           
            read_quad_with_normals(intArray);            
        }
         else if(intArray.getSize() == 8)
        {                        
            read_quad_with_uvs(intArray);
        }
        else if(intArray.getSize() == 9)
        {
            read_triangle_with_uvs_normals(intArray);
        }
        else if(intArray.getSize() == 12)
        {
            read_quad_with_uvs_normals(intArray);
        }    
    }
    
    public static void read_triangle(IntArray intArray)
    {
        int[] array = intArray.trim();
        TriangleMesh mesh = meshes.get(meshes.size()-1);
             
        mesh.addVertexIndex(array[0]-1, array[0]-2, array[0]-1);        
    }
    
    public static void read_quad(IntArray intArray)
    {
        int[] array = intArray.trim();
        TriangleMesh mesh = meshes.get(meshes.size()-1);
        
        mesh.addVertexIndex(array[0]-1, array[1]-1, array[2]-1); //p1 p2 p3
        mesh.addVertexIndex(array[0]-1, array[2]-1, array[3]-1); //p1 p3 p4        
    }
    
     public static void read_triangle_with_normals(IntArray intArray)
    {
        int[] array = intArray.trim();
        TriangleMesh mesh = meshes.get(meshes.size()-1);
        
        mesh.addVertexIndex(array[0]-1, array[2]-1, array[4]-1);
        mesh.addNormalIndex(array[1]-1, array[3]-1, array[5]-1);        
    }
    
    public static void read_triangle_with_uvs(IntArray intArray)
    {
        int[] array = intArray.trim();
        TriangleMesh mesh = meshes.get(meshes.size()-1);
        
        mesh.addVertexIndex(array[0]-1, array[2]-1, array[4]-1);
        mesh.addUVIndex(array[1]-1, array[3]-1, array[5]-1);        
    }
    
    public static void read_quad_with_uvs(IntArray intArray)
    {
        int[] array = intArray.trim();
        TriangleMesh mesh = meshes.get(meshes.size()-1);
        
        mesh.addVertexIndex(array[0]-1, array[2]-1, array[4]-1);
        mesh.addVertexIndex(array[0]-1, array[4]-1, array[6]-1);
        
        mesh.addUVIndex(array[1]-1, array[3]-1, array[5]-1);
        mesh.addUVIndex(array[1]-1, array[5]-1, array[7]-1);         
    }
    
    public static void read_quad_with_normals(IntArray intArray)
    {
        int[] array = intArray.trim();
        TriangleMesh mesh = meshes.get(meshes.size()-1);
        
        mesh.addVertexIndex(array[0]-1, array[2]-1, array[4]-1);
        mesh.addVertexIndex(array[0]-1, array[4]-1, array[6]-1);
        
        mesh.addNormalIndex(array[1]-1, array[3]-1, array[5]-1);
        mesh.addNormalIndex(array[1]-1, array[5]-1, array[7]-1);
        
    }
    
    public static void read_triangle_with_uvs_normals(IntArray intArray)
    {
        int[] array = intArray.trim();
        TriangleMesh mesh = meshes.get(meshes.size()-1);
        
        mesh.addVertexIndex(array[0]-1, array[3]-1, array[6]-1);
        mesh.addUVIndex(array[1]-1, array[4]-1, array[7]-1);
        mesh.addNormalIndex(array[2]-1, array[5]-1, array[8]-1);        
    }
    
    public static void read_quad_with_uvs_normals(IntArray intArray)
    {
        int[] array = intArray.trim();
        TriangleMesh mesh = meshes.get(meshes.size()-1);
        
        mesh.addVertexIndex(array[0]-1, array[3]-1, array[6]-1);
        mesh.addVertexIndex(array[0]-1, array[6]-1, array[9]-1);
        
        mesh.addUVIndex(array[1]-1, array[4]-1, array[7]-1);
        mesh.addUVIndex(array[1]-1, array[7]-1, array[10]-1);
        
        mesh.addNormalIndex(array[2]-1, array[5]-1, array[8]-1);     
        mesh.addNormalIndex(array[2]-1, array[8]-1, array[11]-1);          
    }
}
