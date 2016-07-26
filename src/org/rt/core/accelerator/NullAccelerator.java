/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.accelerator;

import org.rt.core.AbstractAccelerator;
import org.rt.core.AbstractPrimitive;
import org.rt.core.Intersection;
import org.rt.core.math.BoundingBox;
import org.rt.core.math.Ray;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class NullAccelerator extends AbstractAccelerator
{
    private final ArrayList<AbstractPrimitive> primitives = new ArrayList<>();
    private final BoundingBox bound = new BoundingBox();
    
    @Override
    public void setPrimitives(ArrayList<AbstractPrimitive> prims) {
        for(AbstractPrimitive prim : prims)
        {            
            primitives.add(prim);
            bound.include(prim.getWorldBounds());
        }
    }

    @Override
    public boolean intersect(Ray r, Intersection isect) 
    {        
        boolean hasIntersected = false;
        for(AbstractPrimitive prim: primitives)
            hasIntersected |= prim.intersect(r, isect);        
        return hasIntersected;
    }

    @Override
    public boolean intersectP(Ray r) {        
        for(AbstractPrimitive prim: primitives)
            if(prim.intersectP(r))
                return true;
        return false;
    }

    @Override
    public BoundingBox getWorldBounds() {
        return bound;
    }    
}
