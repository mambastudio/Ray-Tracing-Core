/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.coordinates;

/**
 *
 * @author user
 */
public class Point3f {
    public float x, y, z;

    public Point3f() {
    }

    public Point3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3f(Point3f p) {
        x = p.x;
        y = p.y;
        z = p.z;
    }

    public float get(int i) {
        switch (i) {
            case 0:
                return x;
            case 1:
                return y;
            default:
                return z;
        }
    }
    
    public Vector3f asVector()
    {
        return new Vector3f(x, y, z);
    }

    public final float distanceTo(Point3f p) {
        float dx = x - p.x;
        float dy = y - p.y;
        float dz = z - p.z;
        return (float) Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    public final float distanceTo(float px, float py, float pz) {
        float dx = x - px;
        float dy = y - py;
        float dz = z - pz;
        return (float) Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    public final float distanceToSquared(Point3f p) {
        float dx = x - p.x;
        float dy = y - p.y;
        float dz = z - p.z;
        return (dx * dx) + (dy * dy) + (dz * dz);
    }

    public final float distanceToSquared(float px, float py, float pz) {
        float dx = x - px;
        float dy = y - py;
        float dz = z - pz;
        return (dx * dx) + (dy * dy) + (dz * dz);
    }

    public final Point3f set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public final Point3f set(Point3f p) {
        x = p.x;
        y = p.y;
        z = p.z;
        return this;
    }
    
    public Point3f add(Vector3f v) {return new Point3f(x + v.x, y + v.y, z + v.z);}
    public Vector3f subV(Point3f a) {return new Vector3f(x - a.x, y - a.y, z - a.z);}    
    public Normal3f subN(Point3f a) {return new Normal3f(x - a.x, y - a.y, z - a.z).normalize();}
    public Point3f mul(float a) {return new Point3f(x * a, y * a, z * a);}
    //public Point3f add(float )
    
    public static final Point3f add(Point3f p, Vector3f v) 
    {
        Point3f dest = new Point3f();
        dest.x = p.x + v.x;
        dest.y = p.y + v.y;
        dest.z = p.z + v.z;
        return dest;
    }

    public static final Vector3f sub(Point3f p1, Point3f p2) 
    {
        Vector3f dest = new Vector3f();
        dest.x = p1.x - p2.x;
        dest.y = p1.y - p2.y;
        dest.z = p1.z - p2.z;
        return dest;
    }

    public static final Point3f mid(Point3f p1, Point3f p2) 
    {
        Point3f dest = new Point3f();
        dest.x = 0.5f * (p1.x + p2.x);
        dest.y = 0.5f * (p1.y + p2.y);
        dest.z = 0.5f * (p1.z + p2.z);
        return dest;
    }

    public static final Vector3f normal(Point3f p0, Point3f p1, Point3f p2) {
        float edge1x = p1.x - p0.x;
        float edge1y = p1.y - p0.y;
        float edge1z = p1.z - p0.z;
        float edge2x = p2.x - p0.x;
        float edge2y = p2.y - p0.y;
        float edge2z = p2.z - p0.z;
        float nx = edge1y * edge2z - edge1z * edge2y;
        float ny = edge1z * edge2x - edge1x * edge2z;
        float nz = edge1x * edge2y - edge1y * edge2x;
        return new Vector3f(nx, ny, nz);
    }
    
    @Override
    public Point3f clone()
    {
        return new Point3f(x, y, z);
    }

    @Override
    public final String toString() {
        return String.format("(%.2f, %.2f, %.2f)", x, y, z);
    }
}
