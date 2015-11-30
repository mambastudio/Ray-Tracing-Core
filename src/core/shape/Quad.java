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
import core.math.Ray;
import core.math.Transform;
import static java.lang.Math.abs;

/**
 *
 * @author user
 */
public class Quad extends AbstractShape
{
    Point3f p00, p01, p10, p11;
    Normal3f n;
    
    public Quad(Point3f p00, Point3f p01, Point3f p10, Point3f p11, Normal3f n)
    {
        super(new Transform(), new Transform());
        this.p00 = p00;
        this.p01 = p01;
        this.p10 = p10;
        this.p11 = p11;
        this.n = n.normalize();
    }
    
    @Override
    public BoundingBox getObjectBounds() 
    {
        BoundingBox bound = new BoundingBox();
        bound.include(p00);
        bound.include(p01);
        bound.include(p10);
        bound.include(p11);
        return bound;
    }

    @Override
    public boolean intersectP(Ray r) {
        // Reject rays using the barycentric coordinates of
        // the intersection point with respect to T.        
        Vector3f E01 = Point3f.sub(p10, p00);
        Vector3f E03 = Point3f.sub(p01, p00);
        Vector3f P   = Vector3f.cross(r.d, E03);
        float det    = Vector3f.dot(E01, P);
        if(abs(det) < 0.001f) return false;
        Vector3f T   = Point3f.sub(r.o, p00);
        float alpha  = Vector3f.dot(T, P)/det;
        if(alpha < 0) return false;
        if(alpha > 1) return false;
        Vector3f Q   = Vector3f.cross(T, E01);
        float beta   = Vector3f.dot(r.d, Q)/det;
        if(beta < 0) return false;
        if(beta > 1) return false;
        
        // Reject rays using the barycentric coordinates of
        // the intersection point with respect to T'.
        if((alpha + beta) > 1)
        {
            Vector3f E23 = Point3f.sub(p01, p11);
            Vector3f E21 = Point3f.sub(p10, p11);
            Vector3f PP  = Vector3f.cross(r.d, E21);
            float dett   = Vector3f.dot(E23, PP); 
            if(Math.abs(dett) < 0.001f) return false;
            Vector3f TT  = Point3f.sub(r.o, p11);
            float alpha_ = Vector3f.dot(TT, PP)/dett;
            if(alpha_ < 0) return false;
            Vector3f QQ  = Vector3f.cross(TT, E23);
            float beta_  = Vector3f.dot(r.d, QQ)/dett;
            if(beta_ < 0) return false;            
        }
        
        // Compute the ray parameter of the intersection
        // point.
        
        float t = Vector3f.dot(E03, Q)/det;
        if( t < 0) return false;
        return true;
    }

    @Override
    public boolean intersect(Ray r, DifferentialGeometry dg) 
    {
        // Reject rays using the barycentric coordinates of
        // the intersection point with respect to T.        
        Vector3f E01 = Point3f.sub(p10, p00);
        Vector3f E03 = Point3f.sub(p01, p00);
        Vector3f P   = Vector3f.cross(r.d, E03);
        float det    = Vector3f.dot(E01, P);
        if(abs(det) < 0.001f) return false;
        Vector3f T   = Point3f.sub(r.o, p00);
        float alpha  = Vector3f.dot(T, P)/det;
        if(alpha < 0) return false;
        if(alpha > 1) return false;
        Vector3f Q   = Vector3f.cross(T, E01);
        float beta   = Vector3f.dot(r.d, Q)/det;
        if(beta < 0) return false;
        if(beta > 1) return false;
        
        // Reject rays using the barycentric coordinates of
        // the intersection point with respect to T'.
        if((alpha + beta) > 1)
        {
            Vector3f E23 = Point3f.sub(p01, p11);
            Vector3f E21 = Point3f.sub(p10, p11);
            Vector3f PP  = Vector3f.cross(r.d, E21);
            float dett   = Vector3f.dot(E23, PP); 
            if(Math.abs(dett) < 0.001f) return false;
            Vector3f TT  = Point3f.sub(r.o, p11);
            float alpha_ = Vector3f.dot(TT, PP)/dett;
            if(alpha_ < 0) return false;
            Vector3f QQ  = Vector3f.cross(TT, E23);
            float beta_  = Vector3f.dot(r.d, QQ)/dett;
            if(beta_ < 0) return false;            
        }
        
        // Compute the ray parameter of the intersection
        // point.
        
        float t = Vector3f.dot(E03, Q)/det;
        if( t < 0) return false;
        
        // Compute the barycentric coordinates of V11.
        Vector3f E02 = Point3f.sub(p11, p00);
        //Vector3f   N = Vector3f.cross(E01, E03);
        Vector3f N   = n.clone();
        float alpha11, beta11;
        if (Math.abs(N.x) >= Math.abs(N.y)
                && Math.abs(N.x) >= Math.abs(N.z))
        {
            alpha11 = (E02.y * E03.z - E02.z * E03.y)/N.x;
            beta11  = (E01.y * E02.z - E01.z * E02.y)/N.x;
        } 
        else if (Math.abs(N.y) >= Math.abs(N.x)
                && Math.abs(N.y) >= Math.abs(N.z))
        {
            alpha11 = (E02.z * E03.x - E02.x * E03.z)/N.y;
            beta11 =  (E01.z * E02.x - E01.x * E02.z)/N.y;
        } 
        else 
        {
            alpha11 = (E02.x * E03.y - E02.y * E03.x)/N.z;
            beta11  = (E01.x * E02.y - E01.y * E02.x)/N.z;
        }
        
        // Compute the bilinear coordinates of the
        // intersection point.
        float u, v;
        if (Math.abs(alpha11 - 1f) < 0.001f) 
        {
            u = alpha;
            if (Math.abs(beta11 - 1) < 0.001f) 
                v = beta;
            else 
                v = beta/(u * (beta11 - 1f) + 1f);
        }
        else if (Math.abs(beta11 - 1f) < 0.001f) 
        {
            v = beta;
            u = alpha/(v * (alpha11 - 1f) + 1f);
        } 
        else 
        {
            float A     = -(beta11 - 1f);
            float B     = alpha*(beta11 - 1f) - beta*(alpha11 - 1f) - 1f;
            float C     = alpha;    
            float delta = B*B - 4*A*C;
            float q     = -0.5f * (B + ((B < 0f) ? -1f : 1)) * (float)Math.sqrt(delta);

            u = q/A;
            if ((u < 0) || (u > 1)) u = C/q;
            v = beta/(u * (beta11 - 1f) + 1f);
        }
        return true;
    }

    @Override
    public float getArea() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Normal3f getNormal(Point3f p) {
        return n.clone();
    }
    
}
