/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.light;

import core.AbstractBackground;
import core.coordinates.Point2f;
import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import core.color.Color;
import core.coordinates.Point2i;
import core.image.HDR;
import core.image.reader.HDRBitmapReader;
import core.math.BoundingSphere;
import core.math.Distribution2D;
import core.math.FloatValue;
import core.math.Frame;
import core.math.Ray;
import core.math.Rng;
import core.math.Utility;
import static core.math.Utility.INV_PI_F;
import static core.math.Utility.PI_F;
import static core.math.Utility.PI_F_TWO;
import static core.math.Utility.acosf;
import static core.math.Utility.atan2f;
import static core.math.Utility.cosf;
import static core.math.Utility.sinf;
import static core.math.Utility.sqrtf;
import java.io.File;
import static java.lang.Math.max;
import java.net.URI;

/**
 *
 * @author user
 */
public class Envmap extends AbstractBackground
{    
    public HDR hdr;
    public Distribution2D distribution2D;
    
    float power = 100;
    
    public Envmap(URI uri)
    {
        super();
        hdr = HDRBitmapReader.load(uri.toString());
        init();
    }
    
    public Envmap(File file)
    {
        super();
        hdr = HDRBitmapReader.load(file.toURI());
        init();
    }
    
    public Envmap()
    {
        //super();
        String fileName = "schoenbrunn.hdr";
        String filePath = "C:\\Users\\user\\Documents\\hdr\\";
        String file = filePath+fileName;
        
        hdr = HDRBitmapReader.load(file).bestResizeFit(2000);
        init();
    }
    
    public final void init()
    {        
        int nu = (int) hdr.getWidth();
        int nv = (int) hdr.getHeight();
        
        distribution2D = new Distribution2D(hdr.getLuminanceArray(), nu, nv);
    }
    
    @Override
    public Color illuminate(BoundingSphere sceneSphere, Point3f receivingPosition, Point2f rndTuple, Ray rayToLight, FloatValue cosAtLight) {
       
        Vector3f directionToLight = sampleDirection(null);
           
        rayToLight.d.set(directionToLight);
        rayToLight.o.set(receivingPosition);
        rayToLight.setMax(1e36f);
        rayToLight.init();
        
        Color radiance = getColor(directionToLight);
                       
        if(cosAtLight != null)
            cosAtLight.value = 1.f;
       
        return radiance.mul(power);
    }

    //Please FIX ME: Sampling is wrong
    @Override
    public Color emit(BoundingSphere sceneSphere, Point2f dirRndTuple, Point2f posRndTuple, Ray rayFromLight, FloatValue cosAtLight) {
        //direction from light
        Vector3f direction = sampleDirection(null);
        
        //radiance
        Color radiance = getColor(direction);
        
        //Sample uniform disk
        Point2f xy = Utility.sampleConcentricDisc(posRndTuple.x, posRndTuple.y);
        
        Frame frame = new Frame();
        frame.setFromZ(direction);
        
        Vector3f a = direction.neg().add(frame.binormal().mul(xy.x)).add(frame.tangent().mul(xy.y));
        
        //Position from light
        Point3f position = sceneSphere.c.add(a.mul(sceneSphere.r));
        
        rayFromLight.d.set(direction);
        rayFromLight.o.set(position);
        rayFromLight.init();
        
        // Not used for infinite or delta lights
        if(cosAtLight != null)
            cosAtLight.value = 1.f;

        return radiance.mul(power);
    }

    @Override
    public Color radiance(BoundingSphere sceneSphere, Point3f hitPoint, Vector3f direction, FloatValue cosAtLight) {
        
        //System.out.println("bu");
        return getColor(direction);
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
    public float directPdfW(BoundingSphere sceneSphere, Point3f p, Vector3f w) {
        return pdfW(w);
    }

    @Override
    public float directPdfA(BoundingSphere sceneSphere, Vector3f w) {
        return pdfW(w);
    }

    @Override
    public float emissionPdfW(BoundingSphere sceneSphere, Vector3f w, float cosAtLight) {
        float directPdf = pdfW(w);
        float positionPdf = Utility.concentricDiscPdfA() /
            sceneSphere.rSqr;
        
        return directPdf * positionPdf;
    }
    
    public float pdfW(Vector3f d)
    {
        float sinTheta = sinTheta(d);
        if(sinTheta == 0f)
            return 0;
        
        Point2i uv = getUV(d);
        return distribution2D.pdfDiscrete(uv) / (2f * PI_F_TWO * sinTheta);                
    }
    
    private float sinTheta(Vector3f v)
    {
        float cosTheta = v.y;
        float sinTheta2 = max(0f, 1f - cosTheta * cosTheta);
        return sqrtf(sinTheta2);
    }
    
    public Color getColor(Point2i uv)
    {
        return hdr.getColor(uv.x, uv.y);
    }
            
    public Point2i getUV(Vector3f d)
    {
        float u, v;
                
        // assume lon/lat format, here the y coordinate is up        
        u = (0.5f  + 0.5f*INV_PI_F * atan2f(d.x, -d.z));
        v = (INV_PI_F * acosf(d.y));
        
        // scale to image space
        u *= hdr.getWidth();
        v *= hdr.getHeight();
       
        return new Point2i((int)u, (int)v);
    }
    
    public Color getColor(Vector3f d)
    {       
        Point2i uv = getUV(d);                
        return hdr.getColor(uv.x, uv.y);
    }
    
    public Point2f toSpherical(Point2i uv)
    {
        Point2f phitheta = new Point2f();      
        
        // transform to range [0, 1]
        float scaleY = uv.y / hdr.getHeight();
        float scaleX = uv.x / hdr.getWidth();
        
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
}
