/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core;

import org.rt.core.math.BoundingBox;
import org.rt.core.math.Ray;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public abstract class AbstractAccelerator 
{
    public abstract void build(ArrayList<AbstractPrimitive> primitives);
    public abstract boolean intersect(Ray r, Intersection isect);
    public abstract boolean intersectP(Ray r);
    
    public abstract BoundingBox getWorldBounds();
}
