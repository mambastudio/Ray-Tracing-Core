/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.math;

import org.rt.core.coordinates.Point3f;
import static org.rt.core.math.Utility.PI_F;

/**
 *
 * @author user
 */
public class BoundingSphere 
{
    public Point3f c = null;
    public float r;
    public float radiusSqr;
    public float invRadiusSqr;
    
    public BoundingSphere()
    {
        c = new Point3f();
        r = 0;
    }
    
    public BoundingSphere(Point3f c, float r)
    {
        this.c = c; this.r = r; radiusSqr = r * r;
    }
    
    public float getAreaProbability()
    {
        return 1f/getArea();
    }
    
    public float getArea()
    {
        return 4 * PI_F * radiusSqr;
    }
}
