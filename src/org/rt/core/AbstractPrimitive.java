/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core;

import org.rt.core.light.AreaLight;
import org.rt.core.coordinates.Normal3f;
import org.rt.core.coordinates.Vector3f;
import org.rt.core.math.BoundingBox;
import org.rt.core.math.Ray;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public abstract class AbstractPrimitive 
{
    public abstract BoundingBox getWorldBounds();
    public abstract boolean intersect(Ray ray, Intersection isect);
    public abstract boolean intersectP(Ray ray);
    public abstract AreaLight getAreaLight();
    public abstract Material getMaterial();
    public abstract AbstractBSDF getBSDF(Normal3f worldNormal, Vector3f worldWi);
    
    public boolean canIntersect() 
    {
        return true;
    }
    
    public abstract void refine(ArrayList<AbstractPrimitive> refined);
    
    @Override
    public String toString()
    {
        return "primitive";
    }
    
    public void init()
    {
        
    }
}
