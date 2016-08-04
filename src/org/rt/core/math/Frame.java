/* 
 * The MIT License
 *
 * Copyright 2016 user.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
