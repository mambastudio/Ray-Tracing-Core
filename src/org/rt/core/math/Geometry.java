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

import org.rt.core.coordinates.Normal3f;
import org.rt.core.coordinates.Point3f;
import org.rt.core.coordinates.Vector3f;
import static org.rt.core.math.Utility.sqr;

/**
 *
 * @author user
 */
public class Geometry 
{
    public static float factor(Normal3f n1, Point3f p1, Normal3f n2, Point3f p2)
    {
        Vector3f w = dirWo(p1, p2);
        
        float d2        = p1.distanceTo(p2);
        float cosTheta0 = Math.abs(Vector3f.dot(n1, w));
        float cosTheta1 = Math.abs(Vector3f.dot(n2, w));
        
        return cosTheta0 * cosTheta1 / d2;
    }   
    
    public static Vector3f dirWo(Point3f p1, Point3f p2)
    {
        return Point3f.sub(p1, p2).normalize();
    }   
    
     //////////////////////////////////////////////////////////////////////////
    // Utilities for converting PDF between Area (A) and Solid angle (W)
    // WtoA = PdfW * cosine / distance_squared
    // AtoW = PdfA * distance_squared / cosine

    public static float pdfWtoA(
        float aPdfW,
        float aDist,
        float aCosThere)
    {
        return aPdfW * Math.abs(aCosThere) / sqr(aDist);
    }

    public static float pdfAtoW(
        float aPdfA,
        float aDist,
        float aCosThere)
    {
        return aPdfA * sqr(aDist) / Math.abs(aCosThere);
    }  
    
    // Mis power (1 for balance heuristic)
    public static float mis(float aPdf)
    {
        return aPdf;
    }

    // Mis weight for 2 pdfs
    public static float mis2(float aSamplePdf, float aOtherPdf) 
    {
        return mis(aSamplePdf) / (mis(aSamplePdf) + mis(aOtherPdf));
    }
}
