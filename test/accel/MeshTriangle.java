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
package accel;

import org.rt.core.Intersection;
import org.rt.core.coordinates.Point2f;
import org.rt.core.coordinates.Point3f;
import org.rt.core.math.BoundingBox;
import org.rt.core.math.Ray;

/**
 *
 * @author user
 */
public class MeshTriangle implements PrimitiveList
{
    float[] points = null;
    float[] normals = null;
    float[] uvs = null;
    
    int[] triangles = null;
    
    BoundingBox bounds = new BoundingBox();
    
    public MeshTriangle(float[] points, float[] normals, float[] uvs, int[] triangles)
    {
        this.points = points;
        this.normals = normals;
        this.uvs = uvs;
        this.triangles = triangles;
        
        for(int i = 0; i<points.length; i+=3)
            bounds.include(points[i], points[i+1], points[i+2]);
    }
    
    @Override
    public int getNumPrimitives() {
        return triangles.length/10;
    }

    @Override
    public BoundingBox getPrimitiveBound(int primID) {
        int index = primID * 10;
        int vIndex1 = triangles[index + 0];
        int vIndex2 = triangles[index + 1];
        int vIndex3 = triangles[index + 2];
        
        BoundingBox bbox = new BoundingBox();
        bbox.include(getPoint(vIndex1));
        bbox.include(getPoint(vIndex2));
        bbox.include(getPoint(vIndex3));
        return bbox;
    }

    @Override
    public BoundingBox getWorldBounds() {
        return bounds;
    }

    @Override
    public boolean intersectPrimitive(Ray r, int primID, Intersection state) {
        int index = primID * 10;
        
        int vIndex1 = triangles[index + 0];
        int vIndex2 = triangles[index + 1];
        int vIndex3 = triangles[index + 2];
                
        Point3f p1 = getPoint(vIndex1);
        Point3f p2 = getPoint(vIndex2);
        Point3f p3 = getPoint(vIndex3);
        
        return false;
    }
    
    private Point3f getPoint(int index)
    {        
        return new Point3f(points[3 * index + 0],
                           points[3 * index + 1],
                           points[3 * index + 2]);
    }
    
    private Point3f getNormal(int index)
    {        
        return new Point3f(normals[3 * index + 0],
                           normals[3 * index + 1],
                           normals[3 * index + 2]);
    }
    
    private Point2f getTexCoord(int index)
    {        
        return new Point2f(uvs[2 * index + 0],
                           uvs[2 * index + 1]);
    }
    
    private boolean hasNormal(int index)
    {
        return false;
    }
    
    private boolean hasTexCoord(int index)
    {
        return false;
    }
    
    private boolean hasMaterial(int index)
    {
        return false;
    }
}
