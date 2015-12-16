/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.math;

import core.AbstractShape;
import core.coordinates.Normal3f;
import core.coordinates.Point3f;
import core.coordinates.Vector3f;

/**
 *
 * @author user
 */
public class DifferentialGeometry 
{
    public Point3f p = new Point3f();
    public Normal3f n = new Normal3f();
    public float u, v;
    public AbstractShape shape;
    
    public Vector3f dpdu, dpdv;
    public Normal3f dndu, dndv;
        
    public Normal3f nn;
    
    public DifferentialGeometry()
    {
        
    }
    
    public DifferentialGeometry(Point3f p, Normal3f n)
    {
        this.p = p; this.n = n;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("point  ").append(p).append("\n");
        builder.append("normal ").append(n);
        return builder.toString();
    }
}
