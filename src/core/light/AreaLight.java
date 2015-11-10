/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.light;

import core.AbstractLight;
import core.AbstractShape;
import core.Material;
import core.coordinates.Normal3f;
import core.coordinates.Point2f;
import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import core.math.BoundingSphere;
import core.math.Color;
import core.math.FloatValue;
import core.math.Frame;
import core.math.Ray;
import core.math.Transform;
import core.math.Utility;

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
    public Color illuminate(BoundingSphere sceneSphere, Point3f receivingPosition, Point2f rndTuple, Ray rayToLight, FloatValue directPdfW, FloatValue emissionPdfW, FloatValue cosAtLight) {
        
        Normal3f n = new Normal3f();
        Point3f p = shape.sampleA(rndTuple.x, rndTuple.y, n);
        Vector3f directionToLight = p.sub(receivingPosition).normalize();
        float distanceToLight = receivingPosition.distanceTo(p);
        float distSqr = distanceToLight * distanceToLight;
        float cosNormalDir = Vector3f.dot(n, directionToLight.neg());
                
        // too close to, or under, tangent
        if(cosNormalDir < Utility.EPS_COSINE)
        {
            return new Color();
        }
        
        if(directPdfW != null)
            directPdfW.value = shape.inverseArea() * distSqr / cosNormalDir;
              
        if(cosAtLight != null)
            cosAtLight.value = cosNormalDir;

        if(emissionPdfW != null)
            emissionPdfW.value = shape.inverseArea() * cosNormalDir * Utility.INV_PI_F;
                
        rayToLight.d = directionToLight;
        rayToLight.o = receivingPosition;
        rayToLight.setMax(distanceToLight);
        
        return material.getEmission();
    }

    @Override
    public Color emit(BoundingSphere sceneSphere, Point2f dirRndTuple, Point2f posRndTuple, Point3f position, Vector3f direction, FloatValue emissionPdfW, FloatValue directPdfA, FloatValue cosThetaLight) {
        Normal3f n = new Normal3f();
        Point3f p = shape.sampleA(posRndTuple.x, posRndTuple.y, n);
        Frame frame = new Frame(n);
        
        Vector3f localDirOut = Utility.sampleCosineHemisphereW(dirRndTuple.x, dirRndTuple.y, emissionPdfW);
        
        // cannot really not emit the particle, so just bias it to the correct angle
        localDirOut.z = Math.max(localDirOut.z, Utility.EPS_COSINE);
        direction.set(frame.toWorld(localDirOut));
        
        if(directPdfA != null)
            directPdfA.value = shape.inverseArea();

        if(cosThetaLight != null)
            cosThetaLight.value = localDirOut.z;
        
        return material.getEmission().mul(localDirOut.z);
    }

    @Override
    public Color radiance(BoundingSphere sceneSphere, Vector3f rayDirection,Point3f hitPoint, Normal3f hitNormal, FloatValue directPdfA, FloatValue emissionPdfW) 
    {
        float cosOutL = Math.max(0.f, Vector3f.dot(hitNormal, rayDirection.neg()));
        
        if(cosOutL == 0)
            return new Color();

        if(directPdfA != null)
            directPdfA.value = shape.inverseArea();
        
        if(emissionPdfW != null)
        {
            emissionPdfW.value = Utility.cosHemispherePdfW(hitNormal, rayDirection.neg());
            emissionPdfW.value *= shape.inverseArea();
        }

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
    
}
