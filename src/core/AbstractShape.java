/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.coordinates.Normal3f;
import core.coordinates.Point3f;
import core.coordinates.Vector3;
import core.coordinates.Vector3f;
import core.math.BoundingBox;
import core.math.DifferentialGeometry;
import core.math.Ray;
import core.math.Transform;

/**
 *
 * @author user
 */
public abstract class AbstractShape 
{
    Transform o2w;
    Transform w2o;
    
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
    
    public Point3f sample(float u1, float u2, Normal3f n) 
    {
        throw new UnsupportedOperationException("Unimplemented Shape::Sample() method called");
        //return Point();
    }
    
    public float pdfA() 
    {
        return 1.f / getArea();
    }
    
    public float pdfW(Point3f p, Vector3f wo)
    {
        DifferentialGeometry dg = new DifferentialGeometry();
        Ray r = new Ray(p, wo);
        
        if(!intersect(r, dg))
            return 0;
        
        float pdfW = pdfA() * r.getDistanceSquared() / Vector3.dot(dg.n, wo);
        
        if (Float.isInfinite(pdfW)) 
            pdfW = 0.f;
        
        return pdfW;
    }
}
