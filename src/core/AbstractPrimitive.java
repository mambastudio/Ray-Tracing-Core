/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.light.AreaLight;
import core.coordinates.Normal3f;
import core.coordinates.Vector3f;
import core.math.BoundingBox;
import core.math.Ray;
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
    public abstract AbstractMaterial getMaterial();
    public abstract AbstractBSDF getBSDF(Normal3f worldNormal, Vector3f worldWi);
    
    public boolean canIntersect() 
    {
        return true;
    }
    
    public abstract void refine(ArrayList<AbstractPrimitive> refined);
}
