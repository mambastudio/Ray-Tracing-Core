/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.primitive;

import org.rt.core.AbstractAccelerator;
import org.rt.core.AbstractBSDF;
import org.rt.core.AbstractPrimitive;
import org.rt.core.AbstractShape;
import org.rt.core.Intersection;
import org.rt.core.Material;
import org.rt.core.accelerator.BoundingVolume;
import org.rt.core.accelerator.UniformGrid;
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
    public void init()
    {
        accelerator.setPrimitives(gPrimitives);        
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
