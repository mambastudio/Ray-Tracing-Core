/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.parser;

import core.AbstractPrimitive;
import core.Material;
import core.coordinates.Normal3f;
import core.coordinates.Point2f;
import core.coordinates.Point3f;
import core.color.Color;
import core.math.IntArray;
import static core.parser.OBJParser2.readUseMtl;
import core.primitive.Geometry;
import core.shape.Triangle;
import java.net.URI;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class OBJParser 
{
    private static final ArrayList<Point3f> vertices = new ArrayList<>();
    private static final ArrayList<Point2f> uvs = new ArrayList<>();
    private static final ArrayList<Normal3f> normals = new ArrayList<>();
    
    private static final ArrayList<AbstractPrimitive> geometriesList = new ArrayList<>();
    
    public static void main(String[] args)
    {
        //run();        
    }
    
    public static void clear()
    {
        vertices.clear();
        uvs.clear();
        normals.clear();
        geometriesList.clear();
    }
    
    public static ArrayList<AbstractPrimitive> read(URI uri)
    {
        StringParser parser = new StringParser(uri);
        
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
                case "vn":                    
                    readNormal(parser);
                    break;
                case "v":
                    readVertex(parser);
                    break;
                case "f":
                    readFaces(parser);
                    break;
                case "usemtl":
                    readUseMtl(parser);
                    break;
                case "vt":
                    readUV(parser);
                    break;
                default:
                    parser.getNextToken();
                    break;
            }
        }
        for(AbstractPrimitive primitive : geometriesList)
        {
            System.out.println("init object");
            ((Geometry)primitive).init();
        }
        
        return geometriesList;
    }
    
    public static void readObject(StringParser parser)
    {
        if(parser.getNextToken().equals("o"))
        {
            String name = parser.getNextToken();
            System.out.println("object: " +name);
            Material material = Material.createLambert(Color.WHITE);
            material.name = name;
            Geometry geometries = new Geometry(material);
            geometriesList.add(geometries);
        }
    }
    
    public static void readGroup(StringParser parser)
    {
        if(parser.getNextToken().equals("g"))
        {
            String name = parser.getNextToken();
            System.out.println("group: " +name);
            Material material = Material.createLambert(Color.WHITE);
            material.name = name;
            Geometry geometries = new Geometry(material);
            geometriesList.add(geometries);
        }
    }
        
    public static void readVertex(StringParser parser)
    {           
        if(parser.getNextToken().equals("v"))        
            vertices.add(new Point3f(parser.getNextFloat(), parser.getNextFloat(), parser.getNextFloat()));             
    }
    
    public static void readNormal(StringParser parser)
    {
        if(parser.getNextToken().equals("vn"))        
            normals.add(new Normal3f(parser.getNextFloat(), parser.getNextFloat(), parser.getNextFloat()));         
    }
    
    public static void readUV(StringParser parser)
    {
        if(parser.getNextToken().equals("vt"))
            uvs.add(new Point2f(parser.getNextFloat(), parser.getNextFloat()));
    }
    
    public static void readFaces(StringParser parser)
    {
        IntArray intArray = new IntArray();
        
        if(parser.getNextToken().equals("f"))        
            while(parser.hasNext() && parser.peekNextTokenNumber())            
                intArray.add(parser.getNextInt());
            
        if(intArray.getSize() == 3)
        {
            
        }
        else if(intArray.getSize() == 6)
        {
            int[] array = intArray.trim();
            Geometry geometries = (Geometry) geometriesList.get(geometriesList.size()-1);
                        
            Point3f  p1 = vertices.get(array[0]-1);
            Point3f  p2 = vertices.get(array[2]-1);
            Point3f  p3 = vertices.get(array[4]-1);
            
            Normal3f  n1 = normals.get(array[1]-1);
            Normal3f  n2 = normals.get(array[3]-1);
            Normal3f  n3 = normals.get(array[5]-1);
            
            geometries.addGeometryPrimitive(new Triangle(p1, p2, p3, n1, n2, n3));
        }
        else if(intArray.getSize() == 8)
        {
            int[] array = intArray.trim();
            Geometry geometries = (Geometry) geometriesList.get(geometriesList.size()-1);
                        
            Point3f  p1 = vertices.get(array[0]-1);
            Point3f  p2 = vertices.get(array[2]-1);
            Point3f  p3 = vertices.get(array[4]-1);
            Point3f  p4 = vertices.get(array[6]-1);
            
            if(!normals.isEmpty())
            {
                Normal3f  n1 = normals.get(array[1]-1);
                Normal3f  n2 = normals.get(array[3]-1);
                Normal3f  n3 = normals.get(array[5]-1);
                Normal3f  n4 = normals.get(array[7]-1);
                
                geometries.addGeometryPrimitive(new Triangle(p1, p2, p3, n1, n2, n3));
                geometries.addGeometryPrimitive(new Triangle(p1, p3, p4, n1, n3, n4));      
            }
            else if(!uvs.isEmpty())
            {
                Point2f  uv1 = uvs.get(array[1]-1);
                Point2f  uv2 = uvs.get(array[3]-1);
                Point2f  uv3 = uvs.get(array[5]-1);
                Point2f  uv4 = uvs.get(array[7]-1);
                
                geometries.addGeometryPrimitive(new Triangle(p1, p2, p3, uv1, uv2, uv3));
                geometries.addGeometryPrimitive(new Triangle(p1, p3, p4, uv1, uv3, uv4));
            }    
        }
        else if(intArray.getSize() == 9)
        {
            int[] array = intArray.trim();
            Geometry geometries = (Geometry) geometriesList.get(geometriesList.size()-1);
            
            Point3f  p1 = vertices.get(array[0]-1);
            Point3f  p2 = vertices.get(array[3]-1);
            Point3f  p3 = vertices.get(array[6]-1);
            
            Point2f  uv1 = uvs.get(array[1]-1);
            Point2f  uv2 = uvs.get(array[4]-1);
            Point2f  uv3 = uvs.get(array[7]-1);
            
            Normal3f  n1 = normals.get(array[2]-1);
            Normal3f  n2 = normals.get(array[5]-1);
            Normal3f  n3 = normals.get(array[8]-1);
            
            geometries.addGeometryPrimitive(new Triangle(p1, p2, p3, n1, n2, n3, uv1, uv2, uv3));
        }
        else if(intArray.getSize() == 12)
        {
            
        }        
    }
    
    public static void get(String string, Point3f p, Normal3f n)
    {
        String[] str = string.split("//");
        int seqV = Integer.valueOf(str[0]);
        int seqN = Integer.valueOf(str[1]);
        if(seqV > 0)
        {
            p.set(vertices.get(seqV - 1));
            n.set(normals.get(seqN - 1));     
        }
        else
        {
            p.set(vertices.get(vertices.size() + seqV));
            n.set(normals.get(normals.size() + seqN));
        }
    }
    
    public static void get(String string, Point3f p, Point2f uv, Normal3f n)
    {
        String[] str = string.split("/");
        int seqV = Integer.valueOf(str[0]);
        int seqT = Integer.valueOf(str[1]);
        int seqN = Integer.valueOf(str[2]);
        
        if(seqV > 0)
        {
            p.set(vertices.get(seqV - 1));
            uv.set(uvs.get(seqT - 1));
            n.set(normals.get(seqN - 1));  
        }
        else
        {
            p.set(vertices.get(vertices.size() + seqV));
            uv.set(uvs.get(uvs.size() + seqT));
            n.set(normals.get(normals.size() + seqN));  
        }
    }
}
