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
public class Vector3f
{
    public float x, y, z;
    
    public Vector3f() {x = 0; y = 0; z = 0;}
    public Vector3f(float a) {x = a; y = a; z = a;}
    public Vector3f(float a, float b, float c) {x = a; y = b; z = c;}
    public Vector3f(Vector3f a) {x = a.x; y = a.y; z = a.z;}
    
    public final Vector3f addAssign(Vector3f v) {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }
    
    public final Vector3f addAssign(float f) 
    {
        x += f;
        y += f;
        z += f;
        return this;
    }
    
    public Vector3f add(float a)
    {
        return new Vector3f(x + a, y + a, z + a);
    }
    
    public Vector3f add(Vector3f a) 
    {
        Vector3f dest = clone();
        dest.x = x + a.x;
        dest.y = y + a.y;
        dest.z = z + a.z;
        return dest;
    }  
    
    public Vector3f mul(Vector3f a) 
    {
        Vector3f dest = clone();
        dest.x = x * a.x;
        dest.y = y * a.y;
        dest.z = z * a.z;
        return dest;
    }
    public Vector3f div(Vector3f a) 
    {
        Vector3f dest = clone();
        dest.x = x / a.x;
        dest.y = y / a.y;
        dest.z = z / a.z;
        return dest;
    }
    
    public Vector3f mul(float a) 
    {
        Vector3f v = this.clone();
        v.x *= a;
        v.y *= a;
        v.z *= a;
        return v;
    }
    
    public void mulAssign(Vector3f a)
    {
        x *= a.x;
        y *= a.y;
        z *= a.z;
    }
    
    public void mulAssign(float a) 
    {        
        x *= a;
        y *= a;
        z *= a;       
    }
    
    public Vector3f div(float a) 
    {
        Vector3f v = this.clone();
        v.x /= a;
        v.y /= a;
        v.z /= a;
        return v;
    }    
    public Vector3f neg()        
    {
        Vector3f v = this.clone();
        v.x *= -1;
        v.y *= -1;
        v.z *= -1;
        return v;        
    }
    
    public static Vector3f cross(Vector3f a, Vector3f b)
    {
        return a.cross(b);
    }
        
    public Vector3f cross(Vector3f t)
    {
        Vector3f dest = clone();
        dest.x = y * t.z - z * t.y;
        dest.y = z * t.x - x * t.z;
        dest.z = x * t.y - y * t.x;
        return dest;
    }
    
    public void addAssign(int i, float value) {float oldValue = get(i); set(i, oldValue + value);}
    public void mulAssign(int i, float value) {float oldValue = get(i); set(i, oldValue * value);}
    
    public Vector3f normalize()
    {
        return normalize(this);
    }
    
    public static Vector3f add(Vector3f a, Vector3f b) {return a.add(b);}    
    public static Vector3f mul(Vector3f a, Vector3f b) {return a.mul(b);}
    public static Vector3f div(Vector3f a, Vector3f b) {return a.div(b);}
    public static float dot(Vector3f a, Vector3f b) {return a.x*b.x + a.y*b.y + a.z*b.z;}
    public static float absDot(Vector3f a, Vector3f b) {return Math.abs(a.x*b.x + a.y*b.y + a.z*b.z);}
    
    public static Vector3f normalize(Vector3f a)
    {
        Vector3f dest = a.clone();
        float lenSqr = Vector3f.dot(a, a);
        float len    = (float) java.lang.Math.sqrt(lenSqr);
        
        dest.x = a.x/len;
        dest.y = a.y/len;
        dest.z = a.z/len;
        return dest;        
    }
    
    public float lenSqr() { return Vector3f.dot(this, this);   }
    public float length() { return (float) java.lang.Math.sqrt(lenSqr());}

    
    public float get(int i)
    {
        if(i == 0) if((1f/x == Float.NEGATIVE_INFINITY)) return 0; else return x;
        else if(i == 1) if((1f/y == Float.NEGATIVE_INFINITY)) return 0; else return y;
        else if(i == 2)if((1f/z == Float.NEGATIVE_INFINITY)) return 0; else return z;
        else throw new UnsupportedOperationException("Invalid");
    }
    
    public void set(int i, float value)
    {
        if(i == 0) x = value;
        else if(i == 1) y = value;
        else if(i == 2) z = value;
        else throw new UnsupportedOperationException("Invalid");
    }
    
    public void set(float value)
    {
        x = y = z = value;
    }
    
    public void set(Vector3f v)
    {
        x = v.x; y = v.y; z = v.z;
    }
        
    public boolean isZero()
    {
        return x == 0 && y == 0 && z == 0;
    }
    
    public float max()
    {
        float a = x, b = y, c = z;
        if (a < b)
            a = b;
        if (a < c)
            a = c;
        return a;    
    }
    
    @Override
    public Vector3f clone() 
    {
        return new Vector3f(x, y, z);
    }
    
    @Override
    public final String toString() 
    {
        return String.format("(%.2f, %.2f, %.2f)", x, y, z);
    }
}
