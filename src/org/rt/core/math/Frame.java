/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.math;

import org.rt.core.coordinates.Normal3f;
import org.rt.core.coordinates.Vector3f;


/**
 *
 * @author user
 */
public class Frame 
{
    public Vector3f mX, mY, mZ;
    
    public Frame()
    {
        mX = new Vector3f(1,0,0);
        mY = new Vector3f(0,1,0);
        mZ = new Vector3f(0,0,1);
    }
    
    public Frame(Vector3f x, Vector3f y, Vector3f z) 
    {
        mX = x;
        mY = y;
        mZ = z;
    }
    
    public Frame(Vector3f z)
    {
        setFromZ(z);
    }
    
    public final void setFromZ(Vector3f z)
    {
        Vector3f tmpZ = mZ = Vector3f.normalize((Vector3f)z);
        Vector3f tmpX = (Math.abs(tmpZ.x) > 0.99f) ? new Vector3f(0,1,0) : new Vector3f(1,0,0);
        mY = Vector3f.normalize( Vector3f.cross(tmpZ, tmpX) );
        mX = Vector3f.cross(mY, tmpZ);
    }
    
    public Vector3f toWorld(Vector3f a)
    {                
        return mX.mul(a.x).add(mY.mul(a.y)).add(mZ.mul(a.z));
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

    public Vector3f binormal(){ return mX; }
    public Vector3f tangent (){ return mY; }
    public Vector3f normal  (){ return mZ; }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("xdir ").append(mX).append("\n");
        builder.append("ydir ").append(mY).append("\n");
        builder.append("zdir ").append(mZ);
        return builder.toString();
    }
            
}
