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
import core.math.BoundingSphere;
import core.math.FloatArray;
import core.math.FloatValue;
import core.math.Ray;
import static core.math.Utility.PI_F;
import static core.math.Utility.TWO_PI_F;
import static core.math.Utility.sphericalDirection;
import java.io.File;
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
    
    HDR hdr;

    int sampleX, sampleY;
    float pdfX, pdfY;
    
    public Envmap(URI uri)
    {
        hdr = new HDR(uri.toString());       
    }
    
    public Envmap()
    {
        String fileName = "memorial.hdr";
        String filePath = "C:\\Users\\user\\Documents\\hdr\\";
        String file = filePath+fileName;
        
        hdr = new HDR(file);  
        init();
    }
    
    public final void init()
    {        
        nu = hdr.getWidth();
        nv = hdr.getHeight();
        
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Color emit(BoundingSphere sceneSphere, Point2f dirRndTuple, Point2f posRndTuple, Ray rayFromLight, FloatValue cosAtLight) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Color radiance(BoundingSphere sceneSphere, Point3f hitPoint, Vector3f direction, FloatValue cosAtLight) {
        return hdr.getColor(direction);
    }

    @Override
    public boolean isFinite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isDelta() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isAreaLight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float directPdfW(BoundingSphere sceneSphere, Point3f p, Vector3f w) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float directPdfA(BoundingSphere sceneSphere) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float emissionPdfW(BoundingSphere sceneSphere, float cosAtLight) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Color getColor(Point2i uv)
    {
        return hdr.getColor(uv);
    }
    
    public Color getColor(Vector3f d)
    {
        return hdr.getColor(d);
    }
    
    public Point2f toSpherical(Point2i uv)
    {
        Point2f phitheta = new Point2f();
        phitheta.x = PI_F * uv.x / nu;
        phitheta.y = TWO_PI_F * uv.y / nv;
        return phitheta;
    }
    
    public Vector3f toDirection(Point2i uv)
    {
        Point2f phitheta = toSpherical(uv);
        Vector3f dir = sphericalDirection(phitheta.y, phitheta.x);
        return dir;
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

    public int sample1D(float pf[],
                         float Pf[],
                         float random,
                         FloatValue pdf
                         )
    {
        int i = binarySearch(random, Pf); // Pf[i]<=unif<Pf[i+1]
        float t = (Pf[i+1] - random) / (Pf[i+1] - Pf[i]);
        
        if(pdf != null)
            pdf.value = Pf[i];
        
        return (int) ((1 - t) * i + t * (i + 1));        
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
        
        int u = sample1D(pu, Pu, r1, pdfU);        
        int v = sample1D(pv[u], Pv[u], r2, pdfV);

        if(pdf != null)
            pdf.value = pdfU.value * pdfV.value;
        
        Point2i sample = new Point2i();
        sample.x = u; sample.y = v;
        return sample;
    }

    public Point2i sample(FloatValue pdf)
    {
        return this.sample2D(pu,
                             Pu,
                             pv,
                             Pv,
                             (float)Math.random(),
                             (float)Math.random(), pdf);
    }
    

    public int binarySearch(float key, float [] array)
    {
        if(key <= array[0])
        {
            return 0;
        }

        for(int i = 0; i<(array.length - 1); i++)
        {
            if(array[i] <= key && key < array[i+1])
                return i;
        }
        return array.length - 1;
    }
    
}
