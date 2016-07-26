/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
