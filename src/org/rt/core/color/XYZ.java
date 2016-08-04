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
package org.rt.core.color;

/**
 *
 * @author user
 */
public class XYZ {
    public float X, Y, Z;
    public float x, y, z;

    public XYZ()
    {

    }

    public XYZ(float X, float Y, float Z)
    {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
        xyz();
    }
    
    public XYZ(double X, double Y, double Z)
    {
        this.X = (float) X;
        this.Y = (float) Y;
        this.Z = (float) Z;
        xyz();
    }

    public final void setY(float Y)
    {
        this.Y = Y;
    }

    public final void xyYtoXYZ()
    {
        X = y < 1e-6f ? 0 : Y*(x/y);
        Z = y < 1e-6f ? 0 : (Y * (1 - x - y))/y;

    }

    public final XYZ mul(float s)
    {
        X *= s;
        Y *= s;
        Z *= s;
        return this;
    }

    public final void xyz()
    {
        float XYZ = X + Y + Z;
        if (XYZ < 1e-6f)
            return;
        float s = 1f / XYZ;
        x = X * s;
        y = Y * s;
        z = Z * s;
    }
}
