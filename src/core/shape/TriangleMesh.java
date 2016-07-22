/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.shape;

import core.AbstractShape;
import core.coordinates.Normal3f;
import core.coordinates.Point2f;
import core.coordinates.Point3f;
import core.math.BoundingBox;
import core.math.DifferentialGeometry;
import core.math.IntArray;
import core.math.Ray;
import core.math.Transform;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class TriangleMesh extends AbstractShape
{    
    //Global variables
    protected static ArrayList<Point3f>  p  = new ArrayList<>();                    // array of nv vertex positions
    protected static ArrayList<Normal3f> n  = new ArrayList<>();                    // an optional array of normal vectors, one per vertex in the mesh   
    protected static ArrayList<Point2f>  uv = new ArrayList<>();                    // an optional array uv values, one of each vertex
    
    //Local variables
    protected IntArray   vertexIndex;  
    protected IntArray   uvIndex;
    protected IntArray   normalIndex;
    
       
    BoundingBox bounds = new BoundingBox();
    String name = null;
    
    public TriangleMesh()
    {
        super(new Transform(), new Transform());
        
        vertexIndex = new IntArray();
        uvIndex = new IntArray();
        normalIndex = new IntArray();
    }
    
    public TriangleMesh(String name)
    {
        super(new Transform(), new Transform());
        
        vertexIndex = new IntArray();
        uvIndex = new IntArray();
        normalIndex = new IntArray();
        
        this.name = name;        
    }
    
         
    public static void addVertex(float x, float y, float z)
    {
        p.add(new Point3f(x, y, z));
    }
        
    public static void addNormal(float x, float y, float z)
    {
        n.add(new Normal3f(x, y, z));
    }
    
    public static void addUV(float x, float y)
    {
        uv.add(new Point2f(x, y));
    }
    
    public static void clear()
    {
        p.clear();
        n.clear();
        uv.clear();
    }
    
    public void addVertexIndex(int i, int j, int k)
    {
        vertexIndex.add(i, j, k);
    }
    
    public void addUVIndex(int i, int j, int k)
    {
        uvIndex.add(i, j, k);
    }
    
    public void addNormalIndex(int i, int j, int k)
    {
        normalIndex.add(i, j, k);
    }
    
    public boolean hasNormal()
    {
        return normalIndex.getSize() > 0;
    }
    
    public boolean hasUV()
    {
        return uvIndex.getSize() > 0;
    }
    
    @Override
    public BoundingBox getWorldBounds() {
        return bounds;
    }

    @Override
    public BoundingBox getObjectBounds() {
        return bounds;
    }

    @Override
    public boolean intersectP(Ray ray) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean intersect(Ray r, DifferentialGeometry dg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float getArea() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Normal3f getNormal(Point3f p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public  ArrayList<AbstractShape> refine()
    {
        //TODO
        ArrayList<AbstractShape> refined = new ArrayList<>();
        for(int i = 0; i<getTriangleSize(); i++)
        {
            TriangleM triangle = new TriangleM(this, i);            
            refined.add(triangle);
        }
        return refined;
    }
    
    public int getTriangleSize()
    {
        return vertexIndex.getSize()/3;
    }
    
    public static String getMeshInfo()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("points   : ").append(p.size()).append("\n");
        builder.append("vertices : ").append(n.size()).append("\n");
        builder.append("uvs      : ").append(uv.size()).append("\n");
        
        return builder.toString();
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("name        : ").append(name).append("\n").append("\n");
        builder.append("faces       : ").append(getTriangleSize()).append("\n");
        builder.append("has UV      : ").append(hasUV()).append("\n");
        builder.append("has normals : ").append(hasNormal()).append("\n");
        return builder.toString();
    }
}
