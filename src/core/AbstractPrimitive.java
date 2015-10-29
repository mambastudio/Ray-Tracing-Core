/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.coordinates.Normal3f;
import core.coordinates.Vector3f;
import core.math.BBox;
import core.math.Ray;

/**
 *
 * @author user
 */
public abstract class AbstractPrimitive 
{
    public abstract BBox getWorldBounds();
    public abstract boolean intersect(Ray ray, Intersection isect);
    public abstract boolean intersectP(Ray ray);
    public abstract AreaLight getAreaLight();
    public abstract Material getMaterial();
    public abstract AbstractBSDF getBSDF(Normal3f worldNormal, Vector3f worldWi);
    
    public boolean canIntersect() 
    {
        return true;
    }
}
