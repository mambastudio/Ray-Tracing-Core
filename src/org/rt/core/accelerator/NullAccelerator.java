/* 
 * The MIT License
 *
 * Copyright 2016 user.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
    public void build(ArrayList<AbstractPrimitive> prims) {
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
