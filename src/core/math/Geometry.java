/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.math;

import core.coordinates.Normal3f;
import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import static core.math.Utility.sqr;

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
}
