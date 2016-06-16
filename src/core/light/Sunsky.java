/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.light;

import core.AbstractBackground;
import core.AbstractLight;
import core.Scene;
import core.color.Color;
import core.color.sun.HosekWilkie;
import core.color.sun.data.ArHosekSkyModelState;
import core.coordinates.Point2f;
import core.coordinates.Point2i;
import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import core.image.HDR;
import core.math.Distribution2D;
import core.math.FloatValue;
import core.math.Ray;
import core.math.Rng;
import core.math.SphericalCoordinate;
import core.math.Utility;
import static core.math.Utility.PI_F;
import static core.math.Utility.PI_F_TWO;
import static core.math.Utility.cosf;
import static core.math.Utility.sinf;
import static core.math.Utility.sqrtf;
import static java.lang.Math.max;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public final class Sunsky extends AbstractBackground 
{   
    //The sun
    DirectionalLight sun = null;
    
    //The sky
    public HDR sky = null;    
    public Distribution2D distribution2D = null;    
        
    public Sunsky(int size, ArHosekSkyModelState state, double exposure, double tonemapGamma)
    {     
       sky = HosekWilkie.getHDR(size, state, exposure, tonemapGamma);
       sun = new DirectionalLight(HosekWilkie.getSunColor(state, exposure * 0.001), state.getSolarDirection().neg());
              
       init();
    }
    
    public final void init()
    {        
        int nu = (int) sky.getWidth();
        int nv = (int) sky.getHeight();
        
        distribution2D = new Distribution2D(sky.getLuminanceArray(), nu, nv);
    }
        
    @Override
    public Color illuminate(Scene scene, Point3f receivingPosition, Point2f rndTuple, Ray rayToLight, FloatValue cosAtLight) {
        Vector3f directionToLight = sampleDirection(null);
           
        rayToLight.d.set(directionToLight);
        rayToLight.o.set(receivingPosition);
        rayToLight.setMax(1e36f);
        rayToLight.init();
        
        Color radiance = getColor(directionToLight);
        
        if(cosAtLight != null)
            cosAtLight.value = 1.f;
       
        return radiance;
    }

    @Override
    public Color emit(Scene scene, Point2f dirRndTuple, Point2f posRndTuple, Ray rayFromLight, FloatValue cosAtLight) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Color radiance(Scene scene, Point3f hitPoint, Vector3f direction, FloatValue cosAtLight) {
        return getColor(direction);
    }

    @Override
    public boolean isFinite() {
        return true;
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
    public float directPdfW(Scene scene, Point3f p, Vector3f w) {
        return pdfW(w);
    }

    @Override
    public float directPdfA(Scene scene, Vector3f w) {
        return pdfW(w);
    }

    @Override
    public float emissionPdfW(Scene scene, Vector3f w, float cosAtLight) {
        float directPdf = pdfW(w);
        float positionPdf = Utility.concentricDiscPdfA() /
            scene.getBoundingSphere().radiusSqr;
        
        return directPdf * positionPdf;
    }

    @Override
    public boolean isCompound() {
        return true;
    }
    
    public float pdfW(Vector3f d)
    {
        float sinTheta = sinTheta(d);
        if(sinTheta == 0f)
            return 0;
        
        Point2i uv = getUV(d);
        Point2f uv_f = new Point2f(uv.x / sky.getWidth(), uv.y / sky.getHeight());
        
        float pdfW =  distribution2D.pdfContinuous(uv_f) / (2f * PI_F_TWO * sinTheta );           
        return pdfW;        
    }
    
    private float sinTheta(Vector3f v)
    {
        float cosTheta = v.y;
        float sinTheta2 = max(0f, 1f - cosTheta * cosTheta);
        return sqrtf(sinTheta2);
    }
    
    public Color getColor(Point2i uv)
    {
        return sky.getColor(uv.x, uv.y);
    }
            
    public Point2i getUV(Vector3f d)
    {
        return SphericalCoordinate.getRange2i(d, sky.getWidth(), sky.getHeight());        
    }
    
    public Color getColor(Vector3f d)
    {       
        Point2i uv = getUV(d);                
        return sky.getColor(uv.x, uv.y);
    }
    
    public Point2f toSpherical(Point2i uv)
    {
        Point2f phitheta = new Point2f();      
        
        // transform to range [0, 1]
        float scaleY = uv.y / sky.getHeight();
        float scaleX = uv.x / sky.getWidth();
        
        phitheta.x = PI_F * scaleY;                     // phi
        phitheta.y = PI_F * (2 * scaleX - 1f);          // theta
        
        return phitheta;
    }
    
    public Vector3f toDirection(Point2i uv)
    {
        Vector3f dest = new Vector3f();
        Point2f phitheta = toSpherical(uv);
        
        float phi = phitheta.x;
        float theta = phitheta.y;
                
        dest.x = sinf(phi) * sinf(theta);
        dest.y = cosf(phi);
        dest.z = -sinf(phi) * cosf(theta);
                    
        return dest;
    }
        
    public Vector3f sampleDirection(FloatValue pdf)
    {       
        Point2i uv = sampleUV(pdf);        
        return toDirection(uv);
    }

    public Point2i sampleUV(FloatValue pdf)
    {        
       return distribution2D.sampleDiscrete(Rng.getFloat(), Rng.getFloat(), pdf);        
    }    
    
    @Override
    public ArrayList<AbstractLight> getLights()
    {
        ArrayList<AbstractLight> lights = new ArrayList<>();        
        lights.add(sun);        
        return lights;
    }    
}
