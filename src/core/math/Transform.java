/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.math;

import core.coordinates.Normal3f;
import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

/**
 *
 * @author user
 */
public class Transform {
    public Matrix m;
    public Matrix mInv;
    
    public Transform()
    {
        m = new Matrix();
        mInv = new Matrix();
    }
            
    public Transform(Matrix mat)
    {
        m = mat.clone();
        mInv = m.inverse();
    }
    
    public Transform(Matrix mat, Matrix matInv) 
    {
        m = mat.clone();
        mInv = matInv.clone();
    }
        
    public static Transform scale(float x, float y, float z) 
    {
        Matrix sM = new Matrix();
        sM.setRow(0, x, 0, 0, 0);
        sM.setRow(1, 0, y, 0, 0);
        sM.setRow(2, 0, 0, z, 0);
        sM.setRow(3, 0, 0, 0, 1);
        
        Matrix sInvM = new Matrix();
        sM.setRow(0, 1.f/x,     0,     0, 0);
        sM.setRow(1,     0, 1.f/y,     0, 0);
        sM.setRow(2,     0,     0, 1.f/z, 0);
        sM.setRow(3,     0,     0,     0, 1);
        
        return new Transform(sM, sInvM);
    }
    
    public static Transform translate(Vector3f v)
    {
        return translate(v.x, v.y, v.z);
    }
    
    public static Transform translate(float x, float y, float z) 
    {
        Matrix sM = new Matrix();
        sM.setRow(0, 1, 0, 0, x);
        sM.setRow(1, 0, 1, 0, y);
        sM.setRow(2, 0, 0, 1, z);
        sM.setRow(3, 0, 0, 0, 1);
        
        Matrix sInvM = new Matrix();
        sInvM.setRow(0, 1, 0, 0, -x);
        sInvM.setRow(1, 0, 1, 0, -y);
        sInvM.setRow(2, 0, 0, 1, -z);
        sInvM.setRow(3, 0, 0, 0,  1);
        
        return new Transform(sM, sInvM);
    }
    
    public static Transform rotate(float angle, Vector3f axis)
    {
        Vector3f a = axis.normalize();
        float s = (float) sin(toRadians(angle));
        float c = (float) cos(toRadians(angle));
        
        Matrix rM = new Matrix();
        
        rM.set(0, 0, a.x * a.x + (1.f - a.x * a.x) * c);
        rM.set(0, 1, a.x * a.y * (1.f - c) - a.z * s);
        rM.set(0, 2, a.x * a.z * (1.f - c) + a.y * s);
        rM.set(0, 3, 0);
        
        rM.set(1, 0, a.x * a.y * (1.f - c) + a.z * s);
        rM.set(1, 1, a.y * a.y + (1.f - a.y * a.y) * c);
        rM.set(1, 2, a.y * a.z * (1.f - c) - a.x * s);
        rM.set(1, 3, 0);
        
        rM.set(2, 0, a.x * a.z * (1.f - c) - a.y * s);
        rM.set(2, 1, a.y * a.z * (1.f - c) + a.x * s);
        rM.set(2, 2, a.z * a.z + (1.f - a.z * a.z) * c);
        rM.set(2, 3, 0);
       
        rM.set(3, 0, 0);
        rM.set(3, 1, 0);
        rM.set(3, 2, 0);
        rM.set(3, 3, 1);
        
        return new Transform(rM, rM.transpose());
    }
    
    public Transform inverse()
    {
        return new Transform(mInv, m);
    }
    
    public Ray transform(Ray r)
    {
        Point3f o = transform(r.o);
        Vector3f d = transform(r.d);
        
        Ray ray = new Ray(o, d);
        ray.setMax(r.getMax());
        
        return ray;
    }
    
    public Point3f transform(Point3f aVec)
    {
        float x = aVec.x, y = aVec.y, z = aVec.z;
        float xp = m.get(0,0) * x + m.get(0,1) * y + m.get(0,2) * z + m.get(0,3);
        float yp = m.get(1,0) * x + m.get(1,1) * y + m.get(1,2) * z + m.get(1,3);
        float zp = m.get(2,0) * x + m.get(2,1) * y + m.get(2,2) * z + m.get(2,3);
        float wp = m.get(3,0) * x + m.get(3,1) * y + m.get(3,2) * z + m.get(3,3);
        assert (wp != 0);
        if (wp == 1.)
        {            
            return new Point3f(xp, yp, zp);
        } 
        else 
        {          
            return new Point3f(xp * wp, yp * wp, zp * wp);
        }
    }
    
    public Vector3f transform(Vector3f aVec)
    {         
        Vector3f v = new Vector3f();
        float x = aVec.x, y = aVec.y, z = aVec.z;
        v.x = m.get(0,0) * x + m.get(0,1) * y + m.get(0,2) * z;
        v.y = m.get(1,0) * x + m.get(1,1) * y + m.get(1,2) * z;
        v.z = m.get(2,0) * x + m.get(2,1) * y + m.get(2,2) * z;   
        return v;
    }  
    
    public Normal3f transform(Normal3f n)
    {
        float x = n.x, y = n.y, z = n.z;
        return new Normal3f(mInv.get(0,0) * x + mInv.get(1,0) * y + mInv.get(2,0) * z,
                            mInv.get(0,1) * x + mInv.get(1,1) * y + mInv.get(2,1) * z,
                            mInv.get(0,2) * x + mInv.get(1,2) * y + mInv.get(2,2) * z);
    }
    
    public BoundingBox transform(BoundingBox b) {

        BoundingBox ret = new BoundingBox(transform(new Point3f(b.minimum.x, b.minimum.y, b.minimum.z)));
        ret = BoundingBox.union(ret, transform(new Point3f(b.maximum.x, b.minimum.y, b.minimum.z)));
        ret = BoundingBox.union(ret, transform(new Point3f(b.minimum.x, b.maximum.y, b.minimum.z)));
        ret = BoundingBox.union(ret, transform(new Point3f(b.minimum.x, b.minimum.y, b.maximum.z)));
        ret = BoundingBox.union(ret, transform(new Point3f(b.minimum.x, b.maximum.y, b.maximum.z)));
        ret = BoundingBox.union(ret, transform(new Point3f(b.maximum.x, b.maximum.y, b.minimum.z)));
        ret = BoundingBox.union(ret, transform(new Point3f(b.maximum.x, b.minimum.y, b.maximum.z)));
        ret = BoundingBox.union(ret, transform(new Point3f(b.maximum.x, b.maximum.y, b.maximum.z)));
        return ret;
    }
    
    public void transformAssign(Normal3f n) 
    {
        float x = n.x, y = n.y, z = n.z;

        n.x = mInv.get(0,0) * x + mInv.get(1,0) * y + mInv.get(2,0) * z;
        n.y = mInv.get(0,1) * x + mInv.get(1,1) * y + mInv.get(2,1) * z;
        n.z = mInv.get(0,2) * x + mInv.get(1,2) * y + mInv.get(2,2) * z;        
    }
       
    public void transformAssign(Vector3f aVec)
    {         
        float x = aVec.x, y = aVec.y, z = aVec.z;
        aVec.x = m.get(0,0) * x + m.get(0,1) * y + m.get(0,2) * z;
        aVec.y = m.get(1,0) * x + m.get(1,1) * y + m.get(1,2) * z;
        aVec.z = m.get(2,0) * x + m.get(2,1) * y + m.get(2,2) * z;   
    }    
    
    public void transformAssign(Point3f aVec)
    {
        float x = aVec.x, y = aVec.y, z = aVec.z;
        float xp = m.get(0,0) * x + m.get(0,1) * y + m.get(0,2) * z + m.get(0,3);
        float yp = m.get(1,0) * x + m.get(1,1) * y + m.get(1,2) * z + m.get(1,3);
        float zp = m.get(2,0) * x + m.get(2,1) * y + m.get(2,2) * z + m.get(2,3);
        float wp = m.get(3,0) * x + m.get(3,1) * y + m.get(3,2) * z + m.get(3,3);
        assert (wp != 0);
        if (wp == 1.)
        {
            aVec.x = xp;
            aVec.y = yp;
            aVec.z = zp;
        } 
        else 
        {
            wp = 1 / wp;
            aVec.x = xp * wp;
            aVec.y = yp * wp;
            aVec.z = zp * wp;
        }
    }
}
