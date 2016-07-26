/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.light;

import org.rt.core.AbstractLight;
import org.rt.core.AbstractShape;
import org.rt.core.Material;
import org.rt.core.Scene;
import org.rt.core.coordinates.Normal3f;
import org.rt.core.coordinates.Point2f;
import org.rt.core.coordinates.Point3f;
import org.rt.core.coordinates.Vector3f;
import org.rt.core.color.Color;
import org.rt.core.math.FloatValue;
import org.rt.core.math.Frame;
import org.rt.core.math.Ray;
import org.rt.core.math.Transform;
import org.rt.core.math.Utility;
import static org.rt.core.math.Utility.INV_PI_F;

/**
 *
 * @author user
 */
public class AreaLight extends AbstractLight
{
    public AbstractShape shape;
    public Material material;
    
    public AreaLight(Material material, AbstractShape shape, Transform l2w)
    {
        super(l2w);
        this.shape = shape;
        this.material = material;
    }
    
    @Override
    public Color illuminate(Scene scene, Point3f receivingPosition, Point2f rndTuple, Ray rayToLight, FloatValue cosAtLight) {
        
        Normal3f n = new Normal3f();
        Point3f  p = shape.sampleW(receivingPosition, rndTuple.x, rndTuple.y, n);
                
        Vector3f directionToLight = p.subV(receivingPosition).normalize();
        float distanceToLight = receivingPosition.distanceTo(p);        
        float cosNormalDir = Vector3f.dot(n, directionToLight.neg());
                
        // too close to, or under, tangent
        if(cosNormalDir < Utility.EPS_COSINE)
        {
            return new Color();
        }       
                                      
        if(cosAtLight != null)
            cosAtLight.value = cosNormalDir;
                                
        rayToLight.d.set(directionToLight);
        rayToLight.o.set(receivingPosition);
        rayToLight.setMax(distanceToLight - 2 * Ray.EPSILON);
        rayToLight.init();
                
        return material.getPoweredEmission();
    }

    @Override
    public Color emit(Scene scene, Point2f dirRndTuple, Point2f posRndTuple, Ray rayFromLight, FloatValue cosAtLight) {
        Normal3f n = new Normal3f();
        Point3f p = shape.sampleA(posRndTuple.x, posRndTuple.y, n);
        Frame frame = new Frame(n);
        
        Vector3f localDirOut = Utility.sampleCosineHemisphereW(dirRndTuple.x, dirRndTuple.y, null);
        
        // cannot really not emit the particle, so just bias it to the correct angle
        localDirOut.z = Math.max(localDirOut.z, Utility.EPS_COSINE);
        
        //Ray from light
        rayFromLight.o = p;
        rayFromLight.d = frame.toWorld(localDirOut);
        rayFromLight.init();
                
        if(cosAtLight != null)
            cosAtLight.value = localDirOut.z;
        
        return material.getPoweredEmission().mul(localDirOut.z);
    }

    @Override
    public Color radiance(Scene scene, Point3f hitPoint, Vector3f direction, FloatValue cosAtLight) 
    {
        float cosOutL = Math.max(0.f, Vector3f.dot(shape.getNormal(hitPoint), direction.neg()));
                
        if(cosOutL == 0)
            return new Color();
        
        if(cosAtLight != null)
            cosAtLight.value = cosOutL;
        
        return material.getPoweredEmission();
    }

    @Override
    public boolean isFinite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isDelta() {
        return false;
    }

    @Override
    public boolean isAreaLight() {
        return true;
    }        

    @Override
    public float directPdfW(Scene scene, Point3f p, Vector3f w) 
    {
        return shape.pdfW(p, w);
    }

    @Override
    public float directPdfA(Scene scene, Vector3f w) {
        return shape.pdfA();
    }

    @Override
    public float emissionPdfW(Scene scene, Vector3f w, float cosAtLight) 
    {
        return cosAtLight * INV_PI_F;
    }

    @Override
    public boolean isCompound() {
        return false;
    }
}
