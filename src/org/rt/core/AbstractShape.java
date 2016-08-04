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
package org.rt.core;

import org.rt.core.coordinates.Normal3f;
import org.rt.core.coordinates.Point3f;
import org.rt.core.coordinates.Vector3f;
import org.rt.core.math.BoundingBox;
import org.rt.core.math.DifferentialGeometry;
import org.rt.core.math.Ray;
import org.rt.core.math.Transform;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public abstract class AbstractShape 
{
    protected Transform o2w;
    protected Transform w2o;
    
    public AbstractShape(Transform o2w, Transform w2o)
    {
        this.o2w = o2w; this.w2o = w2o;
    }
    
    public BoundingBox getWorldBounds() {
        return o2w.transform(getObjectBounds());
    }
    public boolean canIntersect() 
    {
        return true;
    }
    
    public abstract BoundingBox getObjectBounds();     
    public abstract boolean intersectP(Ray ray);
    public abstract boolean intersect(Ray r, DifferentialGeometry dg);
    public abstract float getArea();
    public abstract Normal3f getNormal(Point3f p);
    
    public Normal3f getNormal(Point3f p, Vector3f incidentVector)
    {
        Normal3f n = getNormal(p);
        if(Vector3f.dot(incidentVector, n) >= 0)
            return n.neg();
        else
            return n;
    }
    
    public  ArrayList<AbstractShape> refine()
    {
        return null;
    }
    
    public Point3f sampleA(float u1, float u2, Normal3f n) 
    {
        throw new UnsupportedOperationException("Unimplemented Shape::Sample() method called");        
    }
    
    public Point3f sampleW(Point3f p, float u1, float u2,
            Normal3f n) {
        return sampleA(u1, u2, n);
    }
    
    public float pdfA() 
    {
        return 1.f / getArea();
    }
    
    public float inverseArea()
    {
        return pdfA();
    }
    
    // This should be resolved since it's yielding error accuracy on intersection
    public float pdfW(Point3f p, Vector3f w)
    {       
        DifferentialGeometry dg = new DifferentialGeometry();
        Ray r = new Ray(p, w);
                
        if(!intersect(r, dg))
        {    
            System.out.println(dg.p);
            return 0;
        }
        
        float pdfW = pdfA() * r.getDistanceSquared() / Vector3f.absDot(dg.n, w);
         
        if (Float.isInfinite(pdfW)) 
        {            
            pdfW = 0.f;
        }
              
        return pdfW;
    }
}
