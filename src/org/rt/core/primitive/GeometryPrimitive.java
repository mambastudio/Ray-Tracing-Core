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

import org.rt.core.AbstractBSDF;
import org.rt.core.AbstractPrimitive;
import org.rt.core.AbstractShape;
import org.rt.core.Intersection;
import org.rt.core.Material;
import org.rt.core.image.Texture;
import org.rt.core.coordinates.Normal3f;
import org.rt.core.coordinates.Vector3f;
import org.rt.core.light.AreaLight;
import org.rt.core.math.BoundingBox;
import org.rt.core.math.Ray;
import org.rt.core.math.Transform;
import java.util.ArrayList;

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
        //No intersection
        if(!shape.intersect(ray, isect.dg))
            return false;        
        
        //skyportal material false intersection
        if(material.skyportal)
            return false;
                    
        isect.bsdf = material.getBSDF(isect.dg.n, ray.d);
                
        //has texture, set texture color to bsdf
        if(material.hasTexture())
        {
            Texture texture = material.getTexture();            
            isect.bsdf.setColor(texture.getTexelUV(isect.dg.u, isect.dg.v));            
        }
        
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
            return new AreaLight(material, shape, new Transform());
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

    @Override
    public void refine(ArrayList<AbstractPrimitive> refined) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String toString()
    {
        return shape.toString();
    }
}
