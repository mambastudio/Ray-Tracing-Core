/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.math;

import core.coordinates.Normal3f;
import core.coordinates.Vector3;
import core.coordinates.Vector3f;


/**
 *
 * @author user
 */
public class Frame 
{
    public Vector3 mX, mY, mZ;
    
    public Frame()
    {
        mX = new Vector3f(1,0,0);
        mY = new Vector3f(0,1,0);
        mZ = new Vector3f(0,0,1);
    }
    
    public Frame(Vector3 x, Vector3 y, Vector3 z) 
    {
        mX = x;
        mY = y;
        mZ = z;
    }
    
    public void setFromZ(Vector3 z)
    {
        Vector3 tmpZ = mZ = Vector3.normalize(z);
        Vector3 tmpX = (Math.abs(tmpZ.x) > 0.99f) ? new Vector3f(0,1,0) : new Vector3f(1,0,0);
        mY = Vector3.normalize( Vector3.cross(tmpZ, tmpX) );
        mX = Vector3.cross(mY, tmpZ);
    }
    
    public Vector3f toWorld(Vector3f a)
    {        
        return (Vector3f) mX.mul(a.x).add(mY.mul(a.y)).add(mZ.mul(a.z));
    }
    
    public Normal3f toWorld(Normal3f a)
    {        
        return (Normal3f) mX.mul(a.x).add(mY.mul(a.y)).add(mZ.mul(a.z));
    }

    public Vector3f toLocal(Vector3f a)
    {
        Vector3f v = a.clone();
        v.x = Vector3f.dot(a, mX);
        v.y = Vector3f.dot(a, mY);
        v.z = Vector3f.dot(a, mZ);
        return v;
    }
    
    public Normal3f toLocal(Normal3f a)
    {
        Normal3f v = a.clone();
        v.x = Vector3f.dot(a, mX);
        v.y = Vector3f.dot(a, mY);
        v.z = Vector3f.dot(a, mZ);
        return v;
    }

    public Vector3 binormal(){ return mX; }
    public Vector3 tangent (){ return mY; }
    public Vector3 normal  (){ return mZ; }
}
