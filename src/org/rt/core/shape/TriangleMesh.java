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
package org.rt.core.shape;

import org.rt.core.AbstractShape;
import org.rt.core.coordinates.Normal3f;
import org.rt.core.coordinates.Point2f;
import org.rt.core.coordinates.Point3f;
import org.rt.core.math.BoundingBox;
import org.rt.core.math.DifferentialGeometry;
import org.rt.util.IntArray;
import org.rt.core.math.Ray;
import org.rt.core.math.Transform;
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
        int a = getIndex(i, p.size());
        int b = getIndex(j, p.size());
        int c = getIndex(k, p.size());
        
        vertexIndex.add(a, b, c);
    }
        
    public void addUVIndex(int i, int j, int k)
    {
        int a = getIndex(i, uv.size());
        int b = getIndex(j, uv.size());
        int c = getIndex(k, uv.size());
        
        uvIndex.add(a, b, c);
    }
        
     public void addNormalIndex(int i, int j, int k)
    {
        int a = getIndex(i, n.size());
        int b = getIndex(j, n.size());
        int c = getIndex(k, n.size());
        
        normalIndex.add(a, b, c);
    }
    
    public boolean hasNormal()
    {
        return normalIndex.size() > 0;
    }
    
    public boolean hasUV()
    {
        return uvIndex.size() > 0;
    }
    
    private int getIndex(int index, int size)
    {
        if(index > 0)
            return index - 1;
        else if(index < 0)
            return size + index;
        else
            throw new UnsupportedOperationException("weird index  " +index);
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
        return vertexIndex.size()/3;
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
