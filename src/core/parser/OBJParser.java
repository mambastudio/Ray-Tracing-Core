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
import core.primitive.Geometries;
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
        run2();
        
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
        
        Geometries geometries = null;
        
        while(parser.hasNext())
        {
            String nextToken = parser.getNextToken();
            switch (nextToken) {
                case "o":                
                    String name = parser.getNextToken();
                    System.out.println("object: " +name);
                    Material material = Material.createLambert(Color.WHITE);
                    material.name = name;
                    geometries = new Geometries(material);
                    geometriesList.add(geometries);
                    break;
                case "v":
                    Point3f v = new Point3f();
                    v.x = parser.getNextFloat();
                    v.y = parser.getNextFloat();
                    v.z = parser.getNextFloat();   
                    vertices.add(v);
                    //System.out.println(v);
                    break;
                case "vn":
                    Normal3f n = new Normal3f();
                    n.x = parser.getNextFloat();
                    n.y = parser.getNextFloat();
                    n.z = parser.getNextFloat();
                    normals.add(n);
                    //System.out.println(n);
                    break;
                case "vt":
                    Point2f p = new Point2f();
                    p.x = parser.getNextFloat();
                    p.y = parser.getNextFloat();
                    if(parser.peekNextTokenNumber())
                        parser.skipTokens(1);
                    uvs.add(p);
                    //System.out.println(p);
                    break;
                case "f":
                    if(parser.peekNextToken().contains("//"))
                    {                        
                        Point3f  p1 = new Point3f();
                        Normal3f n1 = new Normal3f();
                        get(parser.getNextToken(), p1, n1);
                        
                        Point3f  p2 = new Point3f();
                        Normal3f n2 = new Normal3f();
                        get(parser.getNextToken(), p2, n2);
                        
                        Point3f  p3 = new Point3f();
                        Normal3f n3 = new Normal3f();
                        get(parser.getNextToken(), p3, n3);
                        
                        Triangle triangle = new Triangle(p1, p2, p3, n1, n2, n3);
                        geometries.addGeometryPrimitive(triangle);                        
                    }   
                    else if(parser.peekNextToken().contains("/"))
                    {
                        Point3f  p1 = new Point3f();
                        Point2f uv1  = new Point2f();
                        Normal3f n1 = new Normal3f();
                        get(parser.getNextToken(), p1, uv1, n1);
                        
                        Point3f  p2 = new Point3f();
                        Point2f uv2  = new Point2f();
                        Normal3f n2 = new Normal3f();
                        get(parser.getNextToken(), p2, uv2, n2);
                        
                        Point3f  p3 = new Point3f();
                        Point2f uv3  = new Point2f();
                        Normal3f n3 = new Normal3f();
                        get(parser.getNextToken(), p3, uv3, n3);
                        
                        Triangle triangle = new Triangle(p1, p2, p3, n1, n2, n3, uv1, uv2, uv3);
                        geometries.addGeometryPrimitive(triangle);
                    }
                    break;                    
            }
        }
        for(AbstractPrimitive primitive : geometriesList)
        {
            System.out.println("init object");
            ((Geometries)primitive).init();
        }
        
        return geometriesList;
    }
    
    public static void run2()
    {
        StringParser parser = new StringParser("C:\\Users\\user\\Documents\\Scene3d\\simplebox", "mitsuba.obj");
        
        Geometries geometries = null;
        
        while(parser.hasNext())
        {
            String nextToken = parser.getNextToken();
            switch (nextToken) {
                case "o":                
                    String name = parser.getNextToken();
                    System.out.println("object: " +name);
                    Material material = Material.createLambert(Color.WHITE);
                    material.name = name;
                    geometries = new Geometries(material);
                    geometriesList.add(geometries);
                    break;
                case "v":
                    Point3f v = new Point3f();
                    v.x = parser.getNextFloat();
                    v.y = parser.getNextFloat();
                    v.z = parser.getNextFloat();   
                    vertices.add(v);
                    //System.out.println(v);
                    break;
                case "vn":
                    Normal3f n = new Normal3f();
                    n.x = parser.getNextFloat();
                    n.y = parser.getNextFloat();
                    n.z = parser.getNextFloat();
                    normals.add(n);
                    //System.out.println(n);
                    break;
                case "vt":
                    Point2f p = new Point2f();
                    p.x = parser.getNextFloat();
                    p.y = parser.getNextFloat();
                    if(parser.peekNextTokenNumber())
                        parser.skipTokens(1);
                    uvs.add(p);
                    //System.out.println(p);
                    break;
                case "f":
                    if(parser.peekNextToken().contains("//"))
                    {                        
                        Point3f  p1 = new Point3f();
                        Normal3f n1 = new Normal3f();
                        get(parser.getNextToken(), p1, n1);
                        
                        Point3f  p2 = new Point3f();
                        Normal3f n2 = new Normal3f();
                        get(parser.getNextToken(), p2, n2);
                        
                        Point3f  p3 = new Point3f();
                        Normal3f n3 = new Normal3f();
                        get(parser.getNextToken(), p3, n3);
                        
                        Triangle triangle = new Triangle(p1, p2, p3, n1, n2, n3);
                        geometries.addGeometryPrimitive(triangle);                        
                    }   
                    else if(parser.peekNextToken().contains("/"))
                    {
                        Point3f  p1 = new Point3f();
                        Point2f uv1  = new Point2f();
                        Normal3f n1 = new Normal3f();
                        get(parser.getNextToken(), p1, uv1, n1);
                        
                        Point3f  p2 = new Point3f();
                        Point2f uv2  = new Point2f();
                        Normal3f n2 = new Normal3f();
                        get(parser.getNextToken(), p2, uv2, n2);
                        
                        Point3f  p3 = new Point3f();
                        Point2f uv3  = new Point2f();
                        Normal3f n3 = new Normal3f();
                        get(parser.getNextToken(), p3, uv3, n3);
                        
                        Triangle triangle = new Triangle(p1, p2, p3, n1, n2, n3);
                        geometries.addGeometryPrimitive(triangle);
                    }
                    break;                    
            }
        }
        for(AbstractPrimitive primitive : geometriesList)
        {
            System.out.println("init object");
            ((Geometries)primitive).init();
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
