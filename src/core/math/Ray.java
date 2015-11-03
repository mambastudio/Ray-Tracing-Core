/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.math;

import core.coordinates.Point3f;
import core.coordinates.Vector3f;

/**
 *
 * @author user
 */
public class Ray {
    public Point3f o = null;
    public Vector3f d = null;
    
    private float tMin;
    private float tMax;
    public static final float EPSILON = 0.0001f;// 0.01f;

    private Ray() 
    {
    }

    public Ray(Point3f o, Vector3f d)
    {
        this(o.x, o.y, o.z, d.x, d.y, d.z);
    }
    
    public Ray(float ox, float oy, float oz, float dx, float dy, float dz) 
    {
        o = new Point3f(ox, oy, oz);
        d = new Vector3f(dx, dy, dz).normalize();
                
        tMin = EPSILON;
        tMax = Float.POSITIVE_INFINITY;
    }
    
    public final boolean isInside(float t) 
    {
        return (tMin < t) && (t < tMax);
    }
    
    public final Point3f getPoint() 
    {
        Point3f dest = new Point3f();        
        dest.x = o.x + (tMax * d.x);
        dest.y = o.y + (tMax * d.y);
        dest.z = o.z + (tMax * d.z);
        return dest;
    }
    
    public final Point3f getPoint(float t)
    {
        Point3f dest = new Point3f();        
        dest.x = o.x + (t * d.x);
        dest.y = o.y + (t * d.y);
        dest.z = o.z + (t * d.z);
        return dest;
    }

    public final void setMax(float t) 
    {
        tMax = t;
    }
    
    public final float getMin() 
    {
        return tMin;
    }

    public final float getMax() 
    {
        return tMax;
    }
    
    //getMax(), included for verbosity in ray tracing code
    public float getDistance()
    {
        return getMax();
    }
    
     public float getDistanceSquared()
    {
        return getMax();
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        
        builder.append("Ray: ").append("\n");
        builder.append("         o    ").append(String.format("(%.5f, %.5f, %.5f)", o.x, o.y, o.z)).append("\n");
        builder.append("         d    ").append(String.format("(%.5f, %.5f, %.5f)", d.x, d.y, d.z)).append("\n");
        builder.append("         tMin ").append(String.format("(%.5f)", tMin));
                
        return builder.toString();   
    }
}
