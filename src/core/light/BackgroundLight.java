/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.light;

import core.AbstractBackground;
import core.Scene;
import core.coordinates.Point2f;
import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import core.color.Color;
import core.math.FloatValue;
import core.math.Frame;
import core.math.Ray;
import core.math.Utility;

/**
 *
 * @author user
 */
public class BackgroundLight extends AbstractBackground
{
    Color backgroundColor;    
    float scale = 1f;
    
    public BackgroundLight(Color color)
    {
        super();
        this.backgroundColor = color;        
    }

    @Override
    public Color illuminate(Scene scene, Point3f receivingPosition, Point2f rndTuple, Ray rayToLight, FloatValue cosAtLight) 
    {
        Vector3f directionToLight = Utility.sampleUniformSphereW(rndTuple, null);
        rayToLight.d.set(directionToLight);
        rayToLight.o.set(receivingPosition);
        rayToLight.setMax(1e36f);
        rayToLight.init();
        
        Color radiance = backgroundColor.mul(scale);
        
        if(cosAtLight != null)
            cosAtLight.value = 1.f;
        
        return radiance;
    }

    @Override
    public Color emit(Scene scene, Point2f dirRndTuple, Point2f posRndTuple, Ray rayFromLight, FloatValue cosAtLight) 
    {
        //direction from light
        Vector3f direction = Utility.sampleUniformSphereW(dirRndTuple, null);
        
        //radiance
        Color radiance = backgroundColor.mul(scale);
        
        //Sample uniform disk
        Point2f xy = Utility.sampleConcentricDisc(posRndTuple.x, posRndTuple.y);
        
        Frame frame = new Frame();
        frame.setFromZ(direction);
        
        Vector3f a = direction.neg().add(frame.binormal().mul(xy.x)).add(frame.tangent().mul(xy.y));
        
        //Position from light
        Point3f position = scene.getBoundingSphere().c.add(a.mul(scene.getBoundingSphere().r));
        
        rayFromLight.d.set(direction);
        rayFromLight.o.set(position);
        rayFromLight.init();
        
        // Not used for infinite or delta lights
        if(cosAtLight != null)
            cosAtLight.value = 1.f;

        return radiance;
    }

    @Override
    public Color radiance(Scene scene, Point3f hitPoint, Vector3f direction, FloatValue cosAtLight) 
    {
        //radiance        
        Color radiance = backgroundColor.mul(scale);
        return radiance;
    }

    @Override
    public boolean isFinite() {
        return false;
    }

    @Override
    public boolean isDelta() {
        return false;
    }

    @Override
    public boolean isAreaLight() {
        return false;
    }

    @Override
    public float directPdfW(Scene scene, Point3f p, Vector3f w) 
    {        
        return Utility.uniformSpherePdfW();
    }

    @Override
    public float directPdfA(Scene scene, Vector3f w) {
        return Utility.uniformSpherePdfW();
    }

    @Override
    public float emissionPdfW(Scene scene, Vector3f w, float cosAtLight) 
    {
        float directPdf = Utility.uniformSpherePdfW();
        float positionPdf = Utility.concentricDiscPdfA() /
            scene.getBoundingSphere().radiusSqr;
        
        return directPdf * positionPdf;
    }

    @Override
    public boolean isCompound() {
        return false;
    }
    
}
