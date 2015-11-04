/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.coordinates;

/**
 *
 * @author user
 * @param <T>
 */
public abstract class Vector3<T extends Vector3> implements Cloneable
{
    public float x, y, z;
    
    public Vector3() {x = 0; y = 0; z = 0;}
    public Vector3(float a) {x = a; y = a; z = a;}
    public Vector3(float a, float b, float c) {x = a; y = b; z = c;}
    public Vector3(Vector3 a) {x = a.x; y = a.y; z = a.z;}
    
    public final T addAssign(Vector3 v) {
        x += v.x;
        y += v.y;
        z += v.z;
        return (T) this;
    }
    
    public final T addAssign(float f) 
    {
        x += f;
        y += f;
        z += f;
        return (T) this;
    }
    
    public T add(Vector3 a) 
    {
        Vector3 dest = clone();
        dest.x = x + a.x;
        dest.y = y + a.y;
        dest.z = z + a.z;
        return (T) dest;
    }  
    
    public T mul(Vector3 a) 
    {
        Vector3 dest = clone();
        dest.x = x * a.x;
        dest.y = y * a.y;
        dest.z = z * a.z;
        return (T) dest;
    }
    public T div(Vector3 a) 
    {
        Vector3 dest = clone();
        dest.x = x / a.x;
        dest.y = y / a.y;
        dest.z = z / a.z;
        return (T) dest;
    }
    
    public T mul(float a) 
    {
        Vector3 v = this.clone();
        v.x *= a;
        v.y *= a;
        v.z *= a;
        return (T) v;
    }
    public T div(float a) 
    {
        Vector3 v = this.clone();
        v.x /= a;
        v.y /= a;
        v.z /= a;
        return (T) v;
    }    
    public T neg()        
    {
        Vector3 v = this.clone();
        v.x *= -1;
        v.y *= -1;
        v.z *= -1;
        return (T) v;        
    }
    
    public static <T extends Vector3> T cross(T a, T b)
    {
        return (T) a.cross(b);
    }
    
    public T cross(T t)
    {
        Vector3 dest = clone();
        dest.x = y * t.z - z * t.y;
        dest.y = z * t.x - x * t.z;
        dest.z = x * t.y - y * t.x;
        return (T) dest;
    }
    
    public void addAssign(int i, float value) {float oldValue = get(i); set(i, oldValue + value);}
    public void mulAssign(int i, float value) {float oldValue = get(i); set(i, oldValue * value);}
    
    public T normalize()
    {
        return normalize((T) this);
    }
    
    public static <T extends Vector3> T add(T a, T b) {return (T) a.add(b);}    
    public static <T extends Vector3> T mul(T a, T b) {return (T) a.mul(b);}
    public static <T extends Vector3> T div(T a, T b) {return (T) a.div(b);}
    public static float dot(Vector3 a, Vector3 b) {return a.x*b.x + a.y*b.y + a.z*b.z;}
        
    public static <T extends Vector3> T normalize(T a)
    {
        T dest = (T) a.clone();
        float lenSqr = Vector3f.dot(a, a);
        float len    = (float) java.lang.Math.sqrt(lenSqr);
        
        dest.x = a.x/len;
        dest.y = a.y/len;
        dest.z = a.z/len;
        return dest;        
    }
    
    public float lenSqr() { return Vector3.dot(this, this);   }
    public float length() { return (float) java.lang.Math.sqrt(lenSqr());}

    
    public float get(int i)
    {
        if(i == 0) return x;
        else if(i == 1) return y;
        else if(i == 2) return z;
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
    public final T clone() 
    {
        try 
        {
            return (T) super.clone();
        } 
        catch (CloneNotSupportedException ex) 
        {
            throw new InternalError(ex);
        }
    }
    
    @Override
    public final String toString() 
    {
        return String.format("(%.2f, %.2f, %.2f)", x, y, z);
    }
}
