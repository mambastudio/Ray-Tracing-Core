/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.primitive;

import core.AbstractAccelerator;
import core.AbstractBSDF;
import core.AbstractPrimitive;
import core.AbstractShape;
import core.Intersection;
import core.AbstractMaterial;
import core.accelerator.BoundingVolume;
import core.coordinates.Normal3f;
import core.coordinates.Vector3f;
import core.light.AreaLight;
import core.math.BoundingBox;
import core.math.Ray;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class Geometries extends AbstractPrimitive
{
    private final AbstractAccelerator accelerator;
    private final ArrayList<AbstractPrimitive> gPrimitives;
    private final AbstractMaterial material;
    
    public Geometries(AbstractMaterial material)
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
        gPrimitives.add(new GeometryPrimitive(shape, material));
    }
        
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
        return accelerator.intersect(ray, isect);
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
    public AbstractMaterial getMaterial() {
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
