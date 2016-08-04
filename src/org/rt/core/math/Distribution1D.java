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

import static org.rt.core.math.Utility.clamp;
import static java.lang.Math.max;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class Distribution1D {
    float [] func, cdf;
    float funcInt;
    int count;
    
    public Distribution1D(float [] f, int offset, int n)
    {
        count = n;
        func = new float[n];
        System.arraycopy(f, offset, func, 0, n);
        cdf = new float[n + 1];
        
        //Compute integral of step function at xi
        cdf[0] = 0;
        for(int i = 1; i < count+1; ++i)
            cdf[i] = cdf[i-1] + func[i-1] / n;
        
        //Transform step function integral into CDF
        funcInt = cdf[count];
        
        if(funcInt > 0)
        {
            for (int i = 1; i < n+1; ++i)
                cdf[i] /= funcInt;
        }    
        
        Utility.debugArray(cdf);
    }
    
    public float sampleContinuous(float u, FloatValue pdf, int[] off)
    {
        // Find surrounding CDF segments and _offset_
        int ptr = upper_bound(cdf, 0, count, u);
        int offset = max(0, ptr - 1);
        if (off != null) {
            off[0] = offset;
        }
        
        // Compute offset along CDF segment
        float du = (u - cdf[offset]) / (cdf[offset + 1] - cdf[offset]);
        
        // Compute PDF for sampled offset
        if (pdf != null) {
            pdf.value = func[offset] / funcInt;
        }

        // Return $x\in{}[0,1)$ corresponding to sample
        return (offset + du) / count;        
    }
    
    public float pdfDiscrete(int u)
    {
        int offset = clamp(u, 0, count-1);  
        
        if(funcInt == 0)
            return 0f;
        else
            return func[offset] / (funcInt * count);   
    }
    
    public float pdfContinuous(float u) 
    {
        // Find surrounding CDF segments and _offset_
        int ptr = upper_bound(cdf, 0, count, u);
        int offset = max(0, ptr - 1);
        
        // Compute PDF for sampled offset
        if(funcInt == 0)
            return 0f;
        else
            return func[offset] / funcInt;        
    }
    
    public float sampleContinuous(float u, FloatValue pdf)
    {
        return sampleContinuous(u, pdf, null);
    }
    
    public int sampleDiscrete(float u, FloatValue pdf)
    {
        // Find surrounding CDF segments and _offset_
        int ptr = upper_bound(cdf, 0, count, u);
        int offset = max(0, ptr - 1);
        
        if (pdf != null) {
            pdf.value = func[offset] / (funcInt * count);
        }
        return offset;        
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
    
    public String funcString()
    {
        return Arrays.toString(func);
    }
    
    public String cdfString()
    {
        return Arrays.toString(cdf);
    }
}
