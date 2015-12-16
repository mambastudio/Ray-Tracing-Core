/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.shape;

import core.AbstractShape;
import core.coordinates.Normal3f;
import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import core.math.BoundingBox;
import core.math.DifferentialGeometry;
import core.math.Frame;
import static core.math.MonteCarlo.uniformSampleSphere;
import core.math.Ray;
import core.math.Transform;
import static core.math.Utility.PI_F;
import static core.math.Utility.TWO_PI_F;
import static core.math.Utility.acosf;
import static core.math.Utility.pdfUniformConePdfW;
import static core.math.Utility.quadratic;
import static core.math.Utility.sampleUniformConeW;
import static core.math.Utility.sqrtf;
import static java.lang.Math.acos;
import static java.lang.Math.atan2;
import static java.lang.Math.max;

/**
 *
 * @author user
 */
public class Sphere extends AbstractShape
{
    private final float radius;
    
    
        
    public Sphere()
    {
        super(new Transform(), new Transform());
        radius = 1;
    }
    
    public Sphere(Transform o2w, float radius)
    {
        this(o2w, o2w.inverse(), radius);
    }
    
    public Sphere(float radius)
    {
        this(Transform.translate(0, 0, 0), radius);
        
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
    
    public Point3f getCenterWorld()
    {
        return o2w.transform(new Point3f());
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
        float u = phi / TWO_PI_F;
        float theta = (float) acos(phit.z / radius);
        float v = theta / PI_F;
        
        // normal
        Normal3f nhit = new Normal3f(phit.x, phit.y, phit.z).normalize();
          
        /*
        if(Vector3f.dot(nhit, ray.d) > 0)
            nhit = nhit.neg();
        */
        
        dg.n = o2w.transform(nhit);
        dg.p = o2w.transform(phit);
        dg.u = u;
        dg.v = v;
        dg.shape = this;        
        r.setMax(thit);
        
        dg.nn = dg.n.clone();
        
        return true;
    }    
    
    @Override
    public Point3f sampleA(float u1, float u2, Normal3f ns) 
    {
        Point3f p = Point3f.add(new Point3f(), uniformSampleSphere(u1, u2).mul(radius).asVector());
        
        if(ns != null)
        {
            Vector3f n = new Vector3f(p.x, p.y, p.z).normalize();
            n = o2w.transform(n);
            ns.set(n);
        }
        
        return o2w.transform(p);
    }
    
    @Override
    public Point3f sampleW(Point3f p, float u1, float u2,
            Normal3f ns) {
        
        Point3f pCenter = o2w.transform(new Point3f());
        Vector3f wc1 = pCenter.subV(p).normalize();
        
        Frame frame = new Frame();
        frame.setFromZ(wc1);
                        
        if(p.distanceTo(pCenter) - radius*radius < 1e-4f)
            return sampleA(u1, u2, ns);
        
        // sample sphere uniformly inside substended cone
        float sinThetaMax2 = radius * radius / p.distanceToSquared(pCenter);
        float cosThetaMax = sqrtf(max(0.f, 1.f - sinThetaMax2));
        float thetaMaximum = acosf(cosThetaMax);
        Vector3f wc2 = frame.toWorld(sampleUniformConeW(u1, u2, thetaMaximum, null));
        DifferentialGeometry dgSphere = new DifferentialGeometry();        
        Point3f ps;
        Ray r = new Ray(p, wc2);
        
        if(!intersect(r, dgSphere))
        {                 
            r.setMax(Vector3f.dot(pCenter.subV(p), r.d));
        }
        
        ps = r.getPoint();
        ns.set(ps.subV(pCenter).normalize());
        
        //System.out.println(ps);
        
        return ps;
    }
    
    @Override
    public float pdfW(Point3f p, Vector3f w)
    {
        Point3f pCenter = o2w.transform(new Point3f());
        float distSqr = p.distanceToSquared(pCenter);
        // Return uniform weight if point inside sphere
        if (p.distanceTo(pCenter) - radius * radius < 1e-4f) {
            return super.pdfW(p, w);
        }

        // Compute general sphere weight
        float sinThetaMax2 = radius * radius / distSqr;
        float cosThetaMax = sqrtf(max(0.f, 1.f - sinThetaMax2));
        float thetaMaximum = acosf(cosThetaMax);
        float pdfW = pdfUniformConePdfW(thetaMaximum);
        
        return pdfW;
    }

    @Override
    public Normal3f getNormal(Point3f p) {
        Point3f p1 = w2o.transform(p);
        Normal3f n = p1.subN(new Point3f()).normalize();
        
        return n;
    }
}
