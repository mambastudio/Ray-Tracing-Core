/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.light;

import core.AbstractLight;
import core.AbstractShape;
import core.AbstractMaterial;
import core.coordinates.Normal3f;
import core.coordinates.Point2f;
import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import core.math.BoundingSphere;
import core.math.Color;
import core.math.FloatValue;
import core.math.Frame;
import core.math.Ray;
import core.math.Rng;
import core.math.Transform;
import core.math.Utility;
import static core.math.Utility.INV_PI_F;
import core.shape.Sphere;
import core.shape.Triangle;

/**
 *
 * @author user
 */
public class AreaLight extends AbstractLight
{
    public AbstractShape shape;
    public AbstractMaterial material;
    
    public AreaLight(AbstractMaterial material, AbstractShape shape, Transform l2w)
    {
        super(l2w);
        this.shape = shape;
        this.material = material;
    }
    
    @Override
    public Color illuminate(BoundingSphere sceneSphere, Point3f receivingPosition, Point2f rndTuple, Ray rayToLight, FloatValue cosAtLight) {
        
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
                
        return material.getEmission();
    }

    @Override
    public Color emit(BoundingSphere sceneSphere, Point2f dirRndTuple, Point2f posRndTuple, Ray rayFromLight, FloatValue cosAtLight) {
        Normal3f n = new Normal3f();
        Point3f p = shape.sampleA(posRndTuple.x, posRndTuple.y, n);
        Frame frame = new Frame(n);
        
        Vector3f localDirOut = Utility.sampleCosineHemisphereW(dirRndTuple.x, dirRndTuple.y, null);
        
        // cannot really not emit the particle, so just bias it to the correct angle
        localDirOut.z = Math.max(localDirOut.z, Utility.EPS_COSINE);
        
        //Ray from light
        rayFromLight.o = p;
        rayFromLight.d = frame.toWorld(localDirOut);
                
        if(cosAtLight != null)
            cosAtLight.value = localDirOut.z;
        
        return material.getEmission().mul(localDirOut.z);
    }

    @Override
    public Color radiance(BoundingSphere sceneSphere, Point3f hitPoint, Vector3f direction, FloatValue cosAtLight) 
    {
        float cosOutL = Math.max(0.f, Vector3f.dot(shape.getNormal(hitPoint), direction.neg()));
        
        if(cosOutL == 0)
            return new Color();
        
        if(cosAtLight != null)
            cosAtLight.value = cosOutL;
        
        return material.getEmission();
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
    public float directPdfW(Point3f p, Vector3f w) 
    {
        return shape.pdfW(p, w);
    }

    @Override
    public float directPdfA() {
        return shape.pdfA();
    }

    @Override
    public float emissionPdfW(float cosAtLight) 
    {
        return cosAtLight * INV_PI_F;
    }
}
