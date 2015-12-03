/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.shape;

import core.AbstractShape;
import core.coordinates.Normal3f;
import core.coordinates.Point3f;
import static core.coordinates.Point3f.sub;
import core.coordinates.Vector3f;
import static core.coordinates.Vector3f.cross;
import static core.coordinates.Vector3f.dot;
import core.math.BoundingBox;
import core.math.DifferentialGeometry;
import core.math.Ray;
import core.math.Transform;
import static core.math.Utility.sqrt;
import static java.lang.Math.abs;

/**
 *
 * @author user
 */
public class Quad extends AbstractShape
{
    Point3f p00, p01, p10, p11;
    Normal3f n;
    
    public Quad(Point3f p00, Point3f p10, Point3f p11, Point3f p01, Normal3f n)
    {
        super(new Transform(), new Transform());
        this.p00 = p00;
        this.p10 = p10;
        this.p11 = p11;
        this.p01 = p01;
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
        float eps = 10e-6f, u, v;
        // Rejects rays that are parallel to Q, and rays that intersect the plane of
        // Q either on the left of the line V00V01 or on the right of the line V00V10.

        Vector3f E_01 = sub(p10, p00);
        Vector3f E_03 = sub(p01, p00);
        Vector3f P = cross(r.d, E_03);
        float det = dot(E_01, P);
        if (abs(det) < eps) return false;
        float inv_det = (1.0f) / det;
        Vector3f T = sub(r.o, p00);
        float alpha = dot(T, P) * inv_det;
        if (alpha < (0.0f)) return false;
        // if (alpha > real(1.0)) return false; // Uncomment if R is used.
        Vector3f Q = cross(T, E_01);
        float beta = dot(r.d, Q) * inv_det;
        if (beta < (0.0f)) return false; 
        // if (beta > real(1.0)) return false; // Uncomment if VR is used.
        
        if ((alpha + beta) > (1.0f)) 
        {
            // Rejects rays that intersect the plane of Q either on the
            // left of the line V11V10 or on the right of the line V11V01.

            Vector3f E_23 = sub(p01, p11);
            Vector3f E_21 = sub(p10, p11);
            Vector3f P_prime = cross(r.d, E_21);
            float det_prime = dot(E_23, P_prime);
            if (abs(det_prime) < eps) return false;
            float inv_det_prime = (1.0f) / det_prime;
            Vector3f T_prime = sub(r.o, p11);
            float alpha_prime = dot(T_prime, P_prime) * inv_det_prime;
            if (alpha_prime < (0.0f)) return false;
            Vector3f Q_prime = cross(T_prime, E_23);
            float beta_prime = dot(r.d, Q_prime) * inv_det_prime;
            if (beta_prime < (0.0f)) return false;  
        }
        
        // Compute the ray parameter of the intersection point, and
        // reject the ray if it does not hit Q.

        float t = dot(E_03, Q) * inv_det;
        if (t < (0.0)) return false;
        
        if(r.isInside(t))
        { 
            return true;
        }
        return false;
    }

    @Override
    public boolean intersect(Ray r, DifferentialGeometry dg) 
    {
        float eps = 10e-6f, u, v;
        // Rejects rays that are parallel to Q, and rays that intersect the plane of
        // Q either on the left of the line V00V01 or on the right of the line V00V10.

        Vector3f E_01 = sub(p10, p00);
        Vector3f E_03 = sub(p01, p00);
        Vector3f P = cross(r.d, E_03);
        float det = dot(E_01, P);
        if (abs(det) < eps) return false;
        float inv_det = (1.0f) / det;
        Vector3f T = sub(r.o, p00);
        float alpha = dot(T, P) * inv_det;
        if (alpha < (0.0f)) return false;
        // if (alpha > real(1.0)) return false; // Uncomment if R is used.
        Vector3f Q = cross(T, E_01);
        float beta = dot(r.d, Q) * inv_det;
        if (beta < (0.0f)) return false; 
        // if (beta > real(1.0)) return false; // Uncomment if VR is used.
        
        if ((alpha + beta) > (1.0f)) 
        {
            // Rejects rays that intersect the plane of Q either on the
            // left of the line V11V10 or on the right of the line V11V01.

            Vector3f E_23 = sub(p01, p11);
            Vector3f E_21 = sub(p10, p11);
            Vector3f P_prime = cross(r.d, E_21);
            float det_prime = dot(E_23, P_prime);
            if (abs(det_prime) < eps) return false;
            float inv_det_prime = (1.0f) / det_prime;
            Vector3f T_prime = sub(r.o, p11);
            float alpha_prime = dot(T_prime, P_prime) * inv_det_prime;
            if (alpha_prime < (0.0f)) return false;
            Vector3f Q_prime = cross(T_prime, E_23);
            float beta_prime = dot(r.d, Q_prime) * inv_det_prime;
            if (beta_prime < (0.0f)) return false;  
        }
        
        // Compute the ray parameter of the intersection point, and
        // reject the ray if it does not hit Q.

        float t = dot(E_03, Q) * inv_det;
        if (t < (0.0)) return false;
        
        // Compute the barycentric coordinates of the fourth vertex.
        // These do not depend on the ray, and can be precomputed
        // and stored with the quadrilateral.  

        float alpha_11, beta_11;
        Vector3f E_02 = sub(p11, p00);
        Vector3f n = cross(E_01, E_03);

        if ((abs(n.x) >= abs(n.y))
            && (abs(n.x) >= abs(n.z))) 
        {
            alpha_11 = ((E_02.y * E_03.z) - (E_02.z * E_03.y)) / n.x;
            beta_11  = ((E_01.y * E_02.z) - (E_01.z * E_02.y)) / n.x;
        }
        else if ((abs(n.y) >= abs(n.x))
            && (abs(n.y) >= abs(n.z))) 
        {  
            alpha_11 = ((E_02.z * E_03.x) - (E_02.x * E_03.z)) / n.y;
            beta_11  = ((E_01.z * E_02.x) - (E_01.x * E_02.z)) / n.y;
        }
        else 
        {
            alpha_11 = ((E_02.x * E_03.y) - (E_02.y * E_03.x)) / n.z;        
            beta_11  = ((E_01.x * E_02.y) - (E_01.y * E_02.x)) / n.z;
        }
        
        // Compute the bilinear coordinates of the intersection point.

        if (abs(alpha_11 - (1.0)) < eps) 
        {  
            // Q is a trapezium.
            u = alpha;
            if (abs(beta_11 - (1.0)) < eps) v = beta; // Q is a parallelogram.
            else v = beta / ((u * (beta_11 - (1.0f))) + (1.0f)); // Q is a trapezium.
        }
        else if (abs(beta_11 - (1.0)) < eps) {

            // Q is a trapezium.
            v = beta;
            u = alpha / ((v * (alpha_11 - (1.0f))) + (1.0f));
        }
        else 
        {
            float A = (1.0f) - beta_11;
            float B = (alpha * (beta_11 - (1.0f)))
                        - (beta * (alpha_11 - (1.0f))) - (1.0f);
            float C = alpha;
            float D = (B * B) - ((4.0f) * A * C);
            float Q_ = (-0.5f) * (B + ((B < (0.0f) ? (-1.0f) : (1.0f))
                        * sqrt(D)));
            u = Q_ / A;
            if ((u < (0.0f)) || (u > (1.0f))) u = C / Q_;
            v = beta / ((u * (beta_11 - (1.0f))) + (1.0f)); 
        }
        
        if(r.isInside(t))
        {  
            Normal3f nhit;
            if(Vector3f.dot(n, r.d) < 0) 
                nhit = new Normal3f(n.normalize());   
            else
                nhit = new Normal3f(n.neg().normalize());
            
            r.setMax(t);
            dg.p = r.getPoint();       
            dg.n = nhit;
            dg.u = u;
            dg.v = v;            
            dg.shape = this; 
            return true;
        }
        return false;
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
