/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.primitive;

import core.AbstractBSDF;
import core.AbstractPrimitive;
import core.AbstractShape;
import core.Intersection;
import core.Material;
import core.coordinates.Normal3f;
import core.coordinates.Vector3f;
import core.light.AreaLight;
import core.math.BoundingBox;
import core.math.Ray;
import core.math.Transform;

/**
 *
 * @author user
 */

public class GeometryPrimitive extends AbstractPrimitive
{
    
    private final AbstractShape shape;
    private final Material material;
    
    public GeometryPrimitive(AbstractShape shape, Material material)
    {
        this.shape = shape;
        this.material = material;
    }

    @Override
    public BoundingBox getWorldBounds() {
        return shape.getWorldBounds();
    }

    @Override
    public boolean intersect(Ray ray, Intersection isect) 
    {
        if(!shape.intersect(ray, isect.dg))
            return false;
        
        isect.primitive = this;
        return true;
    }

    @Override
    public boolean intersectP(Ray ray) 
    {
        return shape.intersectP(ray);
    }

    @Override
    public AreaLight getAreaLight() 
    {
        if(material.isEmitter())
            return new AreaLight(shape, new Transform());
        else
            return null;
    }

    @Override
    public Material getMaterial() 
    {
        return material;
    }

    @Override
    public AbstractBSDF getBSDF(Normal3f worldNormal, Vector3f worldWi) 
    {
        return material.getBSDF(worldNormal, worldWi);
    }
}
