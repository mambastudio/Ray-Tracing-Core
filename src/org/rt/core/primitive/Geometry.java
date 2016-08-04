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
package org.rt.core.primitive;

import org.rt.core.AbstractAccelerator;
import org.rt.core.AbstractBSDF;
import org.rt.core.AbstractPrimitive;
import org.rt.core.AbstractShape;
import org.rt.core.Intersection;
import org.rt.core.Material;
import org.rt.core.accelerator.BoundingVolume;
import org.rt.core.coordinates.Normal3f;
import org.rt.core.coordinates.Vector3f;
import org.rt.core.light.AreaLight;
import org.rt.core.math.BoundingBox;
import org.rt.core.math.Ray;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class Geometry extends AbstractPrimitive
{
    private final AbstractAccelerator accelerator;
    private final ArrayList<AbstractPrimitive> gPrimitives;
    private final Material material;
    
    public Geometry(Material material)
    {
        this.material = material;
        this.accelerator = new BoundingVolume();
        this.gPrimitives = new ArrayList<>();
    }
    
    public void addGeometryPrimitive(GeometryPrimitive prim)
    {
        gPrimitives.add(prim);
    }
    
    public void addGeometryPrimitive(AbstractShape shape)
    {
        ArrayList<AbstractShape> shapes = shape.refine();
        
        if(shapes != null)
            for(AbstractShape refinedShape : shapes)
                gPrimitives.add(new GeometryPrimitive(refinedShape, material));
        else
            gPrimitives.add(new GeometryPrimitive(shape, material));        
    }
        
    @Override
    public void build()
    {
        accelerator.build(gPrimitives);        
    }

    @Override
    public BoundingBox getWorldBounds() {
        return accelerator.getWorldBounds();
    }

    @Override
    public boolean intersect(Ray ray, Intersection isect) 
    {
        if(accelerator.intersect(ray, isect))
        {            
            isect.topPrimitive = this;
            return true;
        }
        else
            return false;        
    }

    @Override
    public boolean intersectP(Ray ray) {
        return accelerator.intersectP(ray);
    }

    @Override
    public AreaLight getAreaLight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public AbstractBSDF getBSDF(Normal3f worldNormal, Vector3f worldWi) {
       throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void refine(ArrayList<AbstractPrimitive> refined) {
        if(refined != null)
        {
            refined.addAll(gPrimitives);
        }
    }    
    
    @Override
    public boolean canIntersect() 
    {
        return false;
    }
    
    @Override
    public String toString()
    {
        return material.toString();
    }
}
