/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core;

import org.rt.core.accelerator.BoundingVolume;
import org.rt.core.color.Color;
import org.rt.core.coordinates.Point3f;
import org.rt.core.coordinates.Vector3f;
import org.rt.core.math.BoundingBox;
import org.rt.core.math.BoundingSphere;
import org.rt.core.math.FloatValue;
import static org.rt.core.math.Geometry.mis2;
import org.rt.core.math.Ray;
import org.rt.core.math.Rng;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class Scene 
{    
    public Camera camera = null;
    public AbstractAccelerator accelerator = null;
    public ArrayList<AbstractPrimitive> primitives = null;
    public LightCache lights = null;    
    
    public Scene()
    {
        lights = new LightCache();        
    }
    
    public Camera getCamera()
    {
        return camera;
    }
    
    public void setCamera(Camera camera)
    {
        this.camera = camera;
    }
    
    public void prepareToRender()
    {
        camera.setUp();
        initLights();
    }
            
    public void setPrimitives(ArrayList<AbstractPrimitive> primitives)
    {        
        this.primitives = new ArrayList<>();
        this.primitives.clear();
        this.primitives.addAll(primitives);
        this.accelerator = new BoundingVolume(this.primitives);
        
    }
    
    public boolean intersect(Ray ray, Intersection isect)
    {
        boolean hit = accelerator.intersect(ray, isect);
        
        return hit;
    }
    
    public boolean intersectP(Ray ray)
    {
        return accelerator.intersectP(ray);
    }
    
    public boolean occluded(Ray ray)
    {
        return accelerator.intersectP(ray);
    }
    
    public BoundingBox getWorldBounds()
    {
        return accelerator.getWorldBounds();
    }
    
    public BoundingSphere getBoundingSphere()
    {
        return getWorldBounds().getBoundingSphere();
    }
        
    //invoke this when lighting has changed
    public void initLights()
    {        
        lights.clear();
                
        for(AbstractPrimitive prim : primitives)
           lights.add(prim);
        
        lights.init();       
    }    
    
    public Color directLightSampling(Intersection isect, FloatValue misWeight)
    {
        // We sample lights uniformly
        int   lightCount    = lights.getSize();
        float lightPickProb = 1.f / lightCount;
        
        // Bsdf should not be delta
        if(isect.bsdf.isDelta())
            return new Color();
        
        //Sample light
        AbstractLight light = lights.getRandomLight();
        Ray rayToLight = new Ray();
        Color radiance = light.illuminate(this, isect.dg.p, Rng.getPoint2f(), rayToLight, null);        
        if(radiance.isBlack())
            return new Color();
        
        //Calculate bsdf factor
        FloatValue bsdfPdfW = new FloatValue();
        FloatValue cosThetaOut = new FloatValue();
        Color bsdfFactor = isect.bsdf.evaluate(rayToLight.d, cosThetaOut, bsdfPdfW, null);         
        if(bsdfFactor.isBlack())
            return new Color();
        
        //Calculate direct Pdf with respect to receiving point
        float directPdfW = light.directPdfW(this, isect.dg.p, rayToLight.d);               
        if(directPdfW < Float.MIN_VALUE)
            return new Color();
        
        //Calculate MIS
        float weight = 1.f;
        if(!isect.bsdf.isDelta())        
            weight = mis2(directPdfW * lightPickProb, bsdfPdfW.value);
        if(misWeight != null)
            misWeight.value = weight;
                
        if(occluded(rayToLight))
            return new Color();
        
        //Calculate final color contribution
        Color contrib = radiance.mul(cosThetaOut.value / (lightPickProb * directPdfW))
                                    .mul(bsdfFactor);                              
        return contrib;
    }
    
    public Color brdfLightSampling(Intersection isect, FloatValue misWeight)
    {
        // We sample lights uniformly
        int   lightCount    = lights.getSize();
        float lightPickProb = 1.f / lightCount;
        
        // Sample brdf
        Vector3f brdfWo = new Vector3f();
        FloatValue bsdfPdfW = new FloatValue(); 
        FloatValue cosWo = new FloatValue();          
        Color bsdfFactor = isect.bsdf.sample(Rng.getPoint2f(), brdfWo, bsdfPdfW, cosWo);                                 
        if(bsdfFactor.isBlack())
            return new Color();      
        
        // Intersect emitter first
        Intersection lsect = new Intersection();
        Ray rayToLight = new Ray(isect.dg.p, brdfWo);
        if(!(intersect(rayToLight, lsect) && lsect.isEmitter()))
            return new Color();
        
        // Deal with the direct light hit
        AbstractLight light = lsect.primitive.getAreaLight();      
        Color radiance = light.radiance(this, lsect.dg.p, rayToLight.d, null);        
        if(radiance.isBlack())
            return new Color();
        
        //Calculate directPdW
        float directPdfW = light.directPdfW(this, isect.dg.p, rayToLight.d);
        if(directPdfW < Float.MIN_VALUE)
            return new Color();
        
        //Calculate mis weight
        float weight = 1.f;
        if(!isect.bsdf.isDelta())                   
            weight = mis2(bsdfPdfW.value, directPdfW * lightPickProb);   
        if(misWeight != null)
            misWeight.value = weight;
                
        if(occluded(rayToLight))
            return new Color();
        
        //Calculate total power output        
        Color color = radiance.mul(bsdfFactor.mul(cosWo.value / bsdfPdfW.value));           
        return color;        
    }
    
    public int getThreads()
    {
        return 1;
    }
}
