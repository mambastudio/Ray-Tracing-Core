/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.accelerator;

import core.AbstractAccelerator;
import core.AbstractPrimitive;
import core.Intersection;
import core.math.BoundingBox;
import core.math.Ray;
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
