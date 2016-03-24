/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.parser;

import core.coordinates.Normal3f;
import core.coordinates.Point2f;
import core.coordinates.Point3f;
import core.math.FloatArray;
import core.math.IntArray;
import core.shape.TriangleMesh;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class OBJParser2 
{
    private static final ArrayList<Point3f> points = new ArrayList<>();    
    private static final ArrayList<Normal3f> normals = new ArrayList<>();
    
    private static final FloatArray uvs = new FloatArray();
    private static final IntArray vi = new IntArray();
    
    private static int nt;
    private static int nv;
   
    private static final ArrayList<TriangleMesh> triangleMesh = new ArrayList<>();
    
    private static String groupName;
    private static String objectName;
    
    public static void main(String... args)
    {
        testRead();
    }
    
    public static void testRead()
    {
        StringParser parser = new StringParser("C:\\Users\\user\\Desktop", "Josto.obj");
        
        TriangleMesh mesh = null;
        
        while(parser.hasNext())
        {           
            String peekToken = parser.peekNextToken();            
            switch (peekToken) {
                case "vn":                    
                    readNormal(parser);
                    break;
                case "f":
                    readFaces(parser);
                    break;
                case "usemtl":
                    readUseMtl(parser);
                    break;
                default:
                    parser.getNextToken();
                    break;
            }
        }
    }
    
    
    public static void readUseMtl(StringParser parser)
    {
        if(parser.getNextToken().equals("usemtl"))
            System.out.println(parser.getNextToken());
    }
    
    public static void readVertex(StringParser parser)
    {           
        if(parser.getNextToken().equals("v"))        
            points.add(new Point3f(parser.getNextFloat(), parser.getNextFloat(), parser.getNextFloat()));             
    }
    
    public static void readNormal(StringParser parser)
    {
        if(parser.getNextToken().equals("vn"))        
            normals.add(new Normal3f(parser.getNextFloat(), parser.getNextFloat(), parser.getNextFloat()));         
    }
    
    public static void readFaces(StringParser parser)
    {
        FloatArray floatArray = new FloatArray();
        
        if(parser.getNextToken().equals("f"))        
            while(parser.hasNext() && parser.peekNextTokenNumber())            
                floatArray.add(parser.getNextFloat());
            
        if(floatArray.getSize() == 3)
        {
            
        }
        else if(floatArray.getSize() == 6)
        {
            
        }
        else if(floatArray.getSize() == 8)
        {
            
        }
        else if(floatArray.getSize() == 9)
        {
            
        }
        else if(floatArray.getSize() == 12)
        {
            
        }
        
    }
    
    private static TriangleMesh getTriangleMesh()
    {
        return new TriangleMesh(nt, nv, vi.trim(), (Point3f[])points.toArray(), 
                (Normal3f[])normals.toArray(), (float[])uvs.trim());
    }
    
    private static class Mesh
    {
        
    }
}
