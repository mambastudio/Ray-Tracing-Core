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
import core.image.HighDynamicImage;
import core.image.reader.HDRBitmapReader;
import core.math.BoundingSphere;
import core.math.FloatValue;
import core.math.Frame;
import core.math.Ray;
import core.math.Utility;
import static core.math.Utility.INV_PI_F;
import static core.math.Utility.PI_F;
import static core.math.Utility.PI_F_TWO;
import static core.math.Utility.acosf;
import static core.math.Utility.atan2f;
import static core.math.Utility.clamp;
import static core.math.Utility.cosf;
import static core.math.Utility.sinf;
import static core.math.Utility.sqrtf;
import static java.lang.Math.max;
import java.net.URI;

/**
 *
 * @author user
 */
public class Envmap extends AbstractBackground
{
    float [][]f = null;

    float [] pu = null;
    float [] Pu = null;

    float [][]pv = null;
    float [][]Pv = null;

    int nu, nv;
    
    public HighDynamicImage hdr;

    int sampleX, sampleY;
    float pdfX, pdfY;
    
    float power = 100;
    
    public Envmap(URI uri)
    {
        super();
        hdr = HDRBitmapReader.load(uri.toString());
    }
    
    public Envmap()
    {
        //super();
        String fileName = "memorial.hdr";
        String filePath = "C:\\Users\\user\\Documents\\hdr\\";
        String file = filePath+fileName;
        
        hdr = HDRBitmapReader.load(file).bestResizeFit(3000).tonemap();
        init();
    }
    
    public final void init()
    {        
        nu = (int) hdr.getWidth();
        nv = (int) hdr.getHeight();
        
        f = new float[nu][nv];

        pu = new float[nu];
        Pu = new float[nu + 1];

        pv = new float[nu][nv];
        Pv = new float[nu][nv + 1];

        for(int y = 0; y < nv; y++)
            for(int x = 0; x < nu; x++)
            {
                f[x][y] = hdr.luminance(x, y);                
            }
        this.precompute2D(f, pu, Pu, pv, Pv);    
        
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
    
    public float pdfW(Vector3f v)
    {
        float sinTheta = sinTheta(v);
        if(sinTheta == 0f)
            return 0;
        
        return pdfUV(v) * nu * nv / (2f * PI_F_TWO * sinTheta);                
    }
    
    private float sinTheta(Vector3f v)
    {
        float cosTheta = v.y;
        float sinTheta2 = max(0f, 1f - cosTheta * cosTheta);
        return sqrtf(sinTheta2);
    }
    
    public Color getColor(Point2i uv)
    {
        return hdr.getColor(uv);
    }
            
    public Point2i getUV(Vector3f d)
    {
        int u, v;
                
        // assume lon/lat format, here the y coordinate is up        
        u = (int) ((0.5f  + 0.5f*INV_PI_F * atan2f(d.x, -d.z)) * (float)nu);
        v = (int) ((INV_PI_F * acosf(d.y)) * nv);
       
        return new Point2i(u, v);
    }
    
    public Color getColor(Vector3f d)
    {       
        Point2i uv = getUV(d);                
        return hdr.getColor(uv.x, uv.y);
    }
    
    public float pdfUV(Vector3f d)
    {
        Point2i uv = getUV(d);
        return pdfUV(uv.x, uv.y);
    }
    
    public float pdfUV(int u, int v)
    {
        u = clamp(u, 0, pu.length - 1);
        v = clamp(v, 0, pv[0].length - 1);
        
        return pdfU(u) * pdfV(u, v);
    }
    
    public float pdfU(int u)
    {       
        return pu[u];
    }
    
    public float pdfV(int u, int v)
    {        
        return pv[u][v];
    }
    
    public Point2f toSpherical(Point2i uv)
    {
        Point2f phitheta = new Point2f();                
        phitheta.x = PI_F * uv.y / (float)nv;
        phitheta.y = PI_F * (2 * uv.x/(float)nu - 1f);    
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
    
    public float degreePhi(Point2i uv)
    {
        Point2f phitheta = toSpherical(uv);        
        float phi = phitheta.x;
        return (float) Math.toDegrees(phi);
    }
    
    public float degreeTheta(Point2i uv)
    {
        Point2f phitheta = toSpherical(uv);        
        float theta = phitheta.y;
        return (float) Math.toDegrees(theta);
    }
    
    
    
    public float precompute1D(float []f,
                             float []pf,
                             float []Pf)
    {
        float I = 0;
        int nf = f.length;

        for(float L : f)
            I+=L;
        
        int i = 0;
        for(float L : f)
        {            
            if(I <= 0)  //major bug if we ignore this (a day's debugging as a result)
                pf[i] = 0;
            else
                pf[i] = L / I; i++;
        }
                
        Pf[0] = 0; i = 1;
        for(float p : pf)
        {
            Pf[i] = Pf[i - 1] + p; i++;            
        }
        Pf[nf] = 1;

        return I;
    }

    public int sampleDiscrete1D(float pf[],
                         float Pf[],
                         float random,
                         FloatValue pdf
                         )
    {       
        
        int ptr = upper_bound(Pf, 0, Pf.length-1, random); // Pf[i]<=unif<Pf[i+1]  
        int offset = max(0, ptr-1);
        
        //float t = (random - Pf[offset+1]) / (Pf[offset+1] - Pf[offset]);
        
        if(pdf != null)
            pdf.value = pf[offset];
       
        return offset;
        //return (int) ((1 - t) * i + t * (i + 1));        
    }

    public void precompute2D(float f[][],
                             float pu[],
                             float Pu[],
                             float pv[][],
                             float Pv[][])
    {
        float colsum [] = new float[pu.length];

        for(int u = 0; u<pu.length; u++)
        {
            colsum[u] = precompute1D(f[u], pv[u], Pv[u]);
        }
        precompute1D(colsum, pu, Pu);
    }

    public Point2i sample2D(float pu[],
                         float Pu[],
                         float pv[][],
                         float Pv[][],
                         float r1,
                         float r2,
                         FloatValue pdf
                         )
    {       
        FloatValue pdfU = new FloatValue(), pdfV = new FloatValue();
                       
        
        int u = sampleDiscrete1D(pu, Pu, r1, pdfU);        
        int v = sampleDiscrete1D(pv[u], Pv[u], r2, pdfV);
               
        
        if(pdf != null)
            pdf.value = pdfU.value * pdfV.value;
        
        Point2i sample = new Point2i();
        sample.x = u; sample.y = v;
        return sample;
    }
    
    public Vector3f sampleDirection(FloatValue pdf)
    {       
        Point2i uv = sampleUV(pdf);        
        return toDirection(uv);
    }

    public Point2i sampleUV(FloatValue pdf)
    {
        return this.sample2D(pu,
                             Pu,
                             pv,
                             Pv,
                             (float)Math.random(),
                             (float)Math.random(), pdf);
    }
    
    private int upper_bound(float[] a, int first, int last, float value) 
    {
        int i;
        for (i = first; i < last; i++) {
            if (a[i] > value) {
                break;
            }
        }
        return i;
    }
}
