/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.light;

import core.AbstractLight;
import core.Scene;
import core.color.Color;
import core.coordinates.Point2f;
import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import core.math.FloatValue;
import core.math.Frame;
import core.math.Ray;
import core.math.Transform;
import core.math.Utility;

/**
 *
 * @author user
 */
public class DirectionalLight extends AbstractLight
{
    Frame frame = null;
    Color intensity = null;
    
    public DirectionalLight(Color color, Vector3f direction)
    {
        super(new Transform());
        this.frame = new Frame(direction.normalize());
        this.intensity = color;
        
    }
    @Override
    public Color illuminate(Scene scene, Point3f receivingPosition, Point2f rndTuple, Ray rayToLight, FloatValue cosAtLight) {
        
        rayToLight.d.set(frame.normal().neg());
        rayToLight.o.set(receivingPosition);
        rayToLight.setMax(1e36f);
        rayToLight.init();
        
        if(cosAtLight != null)
            cosAtLight.value = 1.f;
       
        return intensity.clone();
    }

    @Override
    public Color emit(Scene scene, Point2f dirRndTuple, Point2f posRndTuple, Ray rayFromLight, FloatValue cosAtLight) 
    {
        //Sample uniform disk
        Point2f xy = Utility.sampleConcentricDisc(posRndTuple.x, posRndTuple.y);
            
        //Direction from light
        Vector3f direction = frame.normal().clone();
               
        //Position from light        
        Vector3f a = direction.add(frame.binormal().mul(xy.x)).add(frame.tangent().mul(xy.y));
        Point3f position = scene.getBoundingSphere().c.add(a.mul(scene.getBoundingSphere().r));
        
        //Ray from light setup
        rayFromLight.d.set(direction);
        rayFromLight.o.set(position);
        rayFromLight.init();
        
        // Not used for infinite or delta lights
        if(cosAtLight != null)
            cosAtLight.value = 1.f;

        return intensity.clone();
    }

    @Override
    public Color radiance(Scene scene, Point3f hitPoint, Vector3f direction, FloatValue cosAtLight) {
        return new Color();
    }

    @Override
    public boolean isFinite() {
        return false;
    }

    @Override
    public boolean isDelta() {
        return true;
    }

    @Override
    public boolean isAreaLight() {
        return false;
    }

    @Override
    public float directPdfW(Scene scene, Point3f p, Vector3f w) {
        return 1f;
    }

    @Override
    public float directPdfA(Scene scene, Vector3f w) {
        return 1f;
    }

    @Override
    public float emissionPdfW(Scene scene, Vector3f w, float cosAtLight) {
        return Utility.concentricDiscPdfA() * scene.getBoundingSphere().invRadiusSqr;
    }

    @Override
    public boolean isCompound() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
