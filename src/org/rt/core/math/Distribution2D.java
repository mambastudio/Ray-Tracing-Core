/* 
 * The MIT License
 *
 * Copyright 2016 user.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.rt.core.math;

import org.rt.core.coordinates.Point2f;
import org.rt.core.coordinates.Point2i;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class Distribution2D 
{
    private final ArrayList<Distribution1D> pConditionalV;
    private final Distribution1D pMarginal;
    
    public Distribution2D(float[] func, int nu, int nv) 
    {
        pConditionalV = new ArrayList<>(nv);
        for (int v = 0; v < nv; ++v) {
            // Compute conditional sampling distribution for $\tilde{v}$
            pConditionalV.add(new Distribution1D(func, v * nu, nu));
        }
        
        // Compute marginal sampling distribution $p[\tilde{v}]$
        float[] marginalFunc;
        marginalFunc = new float[nv];
        for (int v = 0; v < nv; ++v) {
            marginalFunc[v] = pConditionalV.get(v).funcInt;
        }        
        pMarginal = new Distribution1D(marginalFunc, 0, nv);        
    }
    
    public Point2f sampleContinous(float u0, float u1)
    {
        return sampleContinuous(u0, u1, null);
    }
    
    public Point2f sampleContinuous(float u0, float u1, FloatValue pdf)
    {
        Point2f uv = new Point2f();
        sampleContinuous(u0, u1, uv, pdf);
        return uv;
    }
    
    public void sampleContinuous(float u0, float u1, Point2f uv, FloatValue pdf)
    {
        float[] uvTemp = new float[2];
        
        sampleContinuous(u0, u1, uvTemp, pdf);
        
        uv.x = uvTemp[0];
        uv.y = uvTemp[1];
    }
    
    public void sampleContinuous(float u0, float u1, float[] uv,
            FloatValue pdf) 
    {        
        FloatValue pdfTemp = new FloatValue();        
        float[] pdfs = new float[2];
        
        int[] v = new int[1];
        uv[1] = pMarginal.sampleContinuous(u1, pdfTemp, v);
        pdfs[1] = pdfTemp.value;
        uv[0] = pConditionalV.get(v[0]).sampleContinuous(u0, pdfTemp);
        pdfs[0] = pdfTemp.value;
        
        if(pdf != null)
            pdf.value = pdfs[0] * pdfs[1];
    }
    
    public Point2i sampleDiscrete(float u0, float u1)
    {
        return sampleDiscrete(u0, u1, null);
    }
    
    public Point2i sampleDiscrete(float u0, float u1, FloatValue pdf)
    {
        Point2i uv = new Point2i();
        sampleDiscrete(u0, u1, uv, pdf);
        return uv;
    }
    
    public void sampleDiscrete(float u0, float u1, Point2i uv, FloatValue pdf)
    {
        int[] uvTemp = new int[2];
        
        sampleDiscrete(u0, u1, uvTemp, pdf);
                
        uv.x = uvTemp[0];
        uv.y = uvTemp[1];
    }
    
    public void sampleDiscrete(float u0, float u1, int[] uv,
            FloatValue pdf)
    {
        FloatValue pdfTemp = new FloatValue();
        float[] pdfs = new float[2];
        
        uv[1] = pMarginal.sampleDiscrete(u1, pdfTemp);        
        pdfs[1] = pdfTemp.value;
        uv[0] = pConditionalV.get(uv[1]).sampleDiscrete(u0, pdfTemp);        
        pdfs[0] = pdfTemp.value;
       
        if(pdf != null)
            pdf.value = pdfs[0] * pdfs[1];
    }
    
    public float pdfDiscrete(Point2i uv)
    {
        return Distribution2D.this.pdfDiscrete(uv.x, uv.y);
    }
    
    public float pdfContinuous(Point2f uv)
    {
        return Distribution2D.this.pdfContinuous(uv.x, uv.y);
    }
    
    public float pdfDiscrete(int u, int v)
    {       
        float pdfU = pConditionalV.get(v).pdfDiscrete(u);
        float pdfV = pMarginal.pdfDiscrete(v);
                
        return pdfU * pdfV;        
    }
    
    public float pdfContinuous(float u, float v) 
    {        
        int iv = (int)(v * pMarginal.count);
        
        float pdfU = pConditionalV.get(iv).pdfContinuous(u);
        float pdfV = pMarginal.pdfContinuous(v);
        
        return pdfU * pdfV;
    }
    
    public String pMarginalFuncString()
    {
        return pMarginal.funcString();
    }
    
    public String pMarginalCdfString()
    {
        return pMarginal.cdfString();
    }
    
    public String pConditionalVFuncString()
    {
        StringBuilder builder = new StringBuilder();
        
        for(int i = 0; i<pConditionalV.size(); i++)
        {
            Distribution1D dist = pConditionalV.get(i);
            
            if(i == pConditionalV.size()-1)
                builder.append(dist.funcString());
            else
                builder.append(dist.funcString()).append("\n");
        }      
        
        return builder.toString();
    }
    
    public String pConditionalVCdfString()
    {
        StringBuilder builder = new StringBuilder();
        
        for(Distribution1D dist : pConditionalV)        
            builder.append(dist.cdfString()).append("\n");
                    
        return builder.toString();
    }
}