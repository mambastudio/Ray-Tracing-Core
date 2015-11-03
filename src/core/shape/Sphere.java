/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.shape;

import core.AbstractShape;
import core.coordinates.Normal3f;
import core.coordinates.Point3f;
import core.coordinates.Vector3;
import core.math.BoundingBox;
import core.math.DifferentialGeometry;
import core.math.Ray;
import core.math.Transform;
import static core.math.Utility.PI_F;
import static core.math.Utility.clamp;
import static core.math.Utility.quadratic;
import static java.lang.Math.acos;
import static java.lang.Math.atan2;

/**
 *
 * @author user
 */
public class Sphere extends AbstractShape
{
    private final float radius;
    
    private final float phiMax = (float) Math.toRadians(360);    
    private final float thetaMin = (float) acos(0);
    private final float thetaMax = (float) acos(1);
        
    public Sphere()
    {
        super(new Transform(), new Transform());
        radius = 1;
    }
    
    public Sphere(Transform o2w, float radius)
    {
        this(o2w, o2w.inverse(), radius);
    }
    
    public Sphere(Point3f p, float radius)
    {
        this(Transform.translate(p.x, p.y, p.z), radius);
        
    }
    
    public Sphere(Transform o2w, Transform w2o,
            float radius)
    {
        super(o2w, w2o);
        this.radius = radius;        
    }
    
    @Override
    public BoundingBox getObjectBounds() 
    {
        return new BoundingBox(-radius, -radius, -radius, radius, radius, radius);
    }

    @Override
    public boolean intersectP(Ray r) {
        Ray ray = w2o.transform(r);
        
        // Compute Quadratic sphere coefficients, page 100
        float A = ray.d.x * ray.d.x + ray.d.y * ray.d.y + ray.d.z * ray.d.z;
        float B = 2 * (ray.d.x * ray.o.x + ray.d.y * ray.o.y + ray.d.z * ray.o.z);
        float C = ray.o.x * ray.o.x + ray.o.y * ray.o.y
                + ray.o.z * ray.o.z - radius * radius;
        
         // Solve Quadratic equation for t values, page 100
        float[] t = new float[2];
        if (!quadratic(A, B, C, t)) {
            return false;
        }
        
        // Compute intersection distance along ray
        if (t[0] > ray.getMax() || t[1] < ray.getMin()) {
            return false;
        }
        float thit = t[0];
        if (t[0] < ray.getMin()) {
            thit = t[1];
            if (thit > ray.getMax()) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public float getArea() 
    {
        return PI_F * radius * radius;
    }

    @Override
    public boolean intersect(Ray r, DifferentialGeometry dg) {
        Ray ray = w2o.transform(r);
        
        float phi;
        Point3f phit;
        
        // Compute Quadratic sphere coefficients, page 100
        float A = ray.d.x * ray.d.x + ray.d.y * ray.d.y + ray.d.z * ray.d.z;
        float B = 2 * (ray.d.x * ray.o.x + ray.d.y * ray.o.y + ray.d.z * ray.o.z);
        float C = ray.o.x * ray.o.x + ray.o.y * ray.o.y
                + ray.o.z * ray.o.z - radius * radius;
        
         // Solve Quadratic equation for t values, page 100
        float[] t = new float[2];
        if (!quadratic(A, B, C, t)) {
            return false;
        }
        
        // Compute intersection distance along ray
        if (t[0] > ray.getMax() || t[1] < ray.getMin()) {
            return false;
        }
        float thit = t[0];
        if (t[0] < ray.getMin()) {
            thit = t[1];
            if (thit > ray.getMax()) {
                return false;
            }
        }
        
        // Compute sphere hit position and phi        
        phit = ray.getPoint(thit);
         if (phit.x == 0.f && phit.y == 0.f) {
            phit.x = 1e-5f * radius;
        }
        phi = (float) atan2(phit.y, phit.x);
        if (phi < 0.) {
            phi += 2.f * (float) PI_F;
        }
        
        // Find parametric representation of sphere hit
        float u = phi / phiMax;
        float theta = (float) acos(clamp(phit.z / radius, -1.f, 1.f));
        float v = (theta - thetaMin) / (thetaMax - thetaMin);
        
        // normal
        Normal3f nhit = new Normal3f(phit.x, phit.y, phit.z).normalize();
        if(Vector3.dot(nhit, ray.d) < 0)
            nhit = nhit.neg();
        
        dg.n = nhit;
        dg.p = phit;
        dg.u = u;
        dg.v = v;
        dg.shape = this;        
        r.setMax(thit);
        
        return true;
    }    
}
