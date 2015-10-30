/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.math;

import core.coordinates.Point3f;

/**
 *
 * @author user
 */
public class BoundingSphere 
{
    public Point3f c = null;
    public float r;
    public float rSqr;
    
    public BoundingSphere()
    {
        c = new Point3f();
        r = 0;
    }
    
    public BoundingSphere(Point3f c, float r)
    {
        this.c = c; this.r = r; rSqr = r * r;
    }
}
