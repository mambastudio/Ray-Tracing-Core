/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.color.sun;

import org.rt.core.color.Color;
import org.rt.core.color.RGBSpace;
import org.rt.core.coordinates.Vector3f;
import org.rt.core.image.HDR;
import org.rt.core.math.SphericalCoordinate;
import static org.rt.core.math.Utility.PI_F;
import static org.rt.core.math.Utility.cosf;
import static org.rt.core.math.Utility.expf;
import static org.rt.core.math.Utility.powf;
import static org.rt.core.math.Utility.tanf;
import static org.rt.core.math.Utility.toRadians;
import static java.lang.Math.min;

/**
 *
 * @author user
 */
public final class Preetham 
{
    Vector3f sunPosition = new Vector3f(0, 0.26f, -0.97f).normalize();
    
    Coefficients coeffsY = new Coefficients();
    Coefficients coeffsx = new Coefficients();
    Coefficients coeffsy = new Coefficients();
    
    float Yabsolute, xabsolute, yabsolute; 
    
    float turbidity = 2.9f;
    
    public Preetham()
    {
        this.sunPosition = SphericalCoordinate.directionDegrees(-90, 0);
        calculateZenithAbsolutes();
        calculateCoefficients();
    }
    
    public Preetham(Vector3f sunPosition)
    {        
        this.sunPosition = sunPosition;
        calculateZenithAbsolutes();
        calculateCoefficients();
    }
    
    public void setTurbidity(float turbidity)
    {
        this.turbidity = turbidity;
        calculateZenithAbsolutes();
        calculateCoefficients();
    }
    
    public void setSunPosition(Vector3f sunPosition)
    {
        this.sunPosition = sunPosition;
        calculateZenithAbsolutes();
        calculateCoefficients();
    }
    
    private class Coefficients
    {
        float A, B, C, D, E;
    }
    
    public void calculateZenithAbsolutes()
    {
        float solarZenith   = solarZenith();
        float solarZenith2  = solarZenith * solarZenith;
        float solarZenith3  = solarZenith * solarZenith2;
        
        Vector3f turbidity_v = new Vector3f(turbidity * turbidity, turbidity, 1);
        Vector3f xv = new Vector3f( 0.00166f * solarZenith3 - 0.00375f * solarZenith2 + 0.00209f * solarZenith,
                                    -0.02903f * solarZenith3 + 0.06377f * solarZenith2 - 0.03202f * solarZenith + 0.00394f,
                                    0.11693f * solarZenith3 - 0.21196f * solarZenith2 + 0.06052f * solarZenith + 0.25886f);
        Vector3f yv = new Vector3f(0.00275f * solarZenith3 - 0.00610f * solarZenith2 + 0.00317f * solarZenith,
                                    -0.04214f * solarZenith3 + 0.08970f * solarZenith2 - 0.04153f * solarZenith + 0.00516f,
                                    0.15346f * solarZenith3 - 0.26756f * solarZenith2 + 0.06670f * solarZenith + 0.26688f);
    
        
        Yabsolute =  (4.0453f * turbidity - 4.9710f) * tanf((4.0f / 9f - turbidity / 120f) * (PI_F - 2 * solarZenith)) - 0.2155f * turbidity + 2.4192f;
        
        
        xabsolute = Vector3f.dot(xv, turbidity_v);
        yabsolute = Vector3f.dot(yv, turbidity_v);
    }
    
    public void calculateCoefficients()
    {
        coeffsY.A =  0.1787f * turbidity - 1.4630f;
        coeffsY.B = -0.3554f * turbidity + 0.4275f;
        coeffsY.C = -0.0227f * turbidity + 5.3251f;
        coeffsY.D =  0.1206f * turbidity - 2.5771f;
        coeffsY.E = -0.0670f * turbidity + 0.3703f;
        
        coeffsx.A = -0.0193f * turbidity - 0.2592f;
        coeffsx.B = -0.0665f * turbidity + 0.0008f;
        coeffsx.C = -0.0004f * turbidity + 0.2125f;
        coeffsx.D = -0.0641f * turbidity - 0.8989f;
        coeffsx.E = -0.0033f * turbidity + 0.0452f;
        
        coeffsy.A = -0.0167f * turbidity - 0.2608f;
        coeffsy.B = -0.0950f * turbidity + 0.0092f;
        coeffsy.C = -0.0079f * turbidity + 0.2102f;
        coeffsy.D = -0.0441f * turbidity - 1.6537f;
        coeffsy.E = -0.0109f * turbidity + 0.0529f;
    }
    
    float perez(float zenith, float gamma, Coefficients coeffs)
    {
        return  (1 + coeffs.A*expf(coeffs.B/cosf(zenith)))*
                (1 + coeffs.C*expf(coeffs.D*gamma)+coeffs.E*powf(cosf(gamma),2));
    }
    
    float solarZenith()
    {
        return SphericalCoordinate.thetaRadians(sunPosition);
    }
    
    float zenith(Vector3f v)
    {
        return SphericalCoordinate.thetaRadians(v);
    }
    
    float gamma(Vector3f v)
    {
        return SphericalCoordinate.getRadiansBetween(v, sunPosition);
    }
    
    public Color getColor(Vector3f v)
    {
        float gamma         = gamma(v);
        float zenith        = zenith(v);
        float solar_zenith  = solarZenith();
        
        if(Math.toDegrees(zenith) > 90)
            return new Color();
        
        float Yp = Yabsolute * perez(zenith, gamma, coeffsY) / perez(0, solar_zenith, coeffsY);
        float xp = xabsolute * perez(zenith, gamma, coeffsx) / perez(0, solar_zenith, coeffsx);
        float yp = yabsolute * perez(zenith, gamma, coeffsy) / perez(0, solar_zenith, coeffsy);
        
        Yp *= 0.05f;
        
        Color color =  RGBSpace.xyYtoRGB(xp, yp, Yp);
        //System.out.println(toDegrees(zenith)+ " " +toDegrees(gamma));
        return color;
    }
    
    public HDR getHDR(int size)
    {
        HDR hdr = new HDR(size, size);
        
        for(int j = 0; j<size; j++)
            for(int i = 0; i<size; i++)
            {
                Color color = getColor(SphericalCoordinate.sphericalDirection(i, j, size, size));
                hdr.setColor(i, j, color);
            }
                
        return hdr;
    }
    
    public static Preetham TIME_6_AM()
    {
        return new Preetham(SphericalCoordinate.directionDegrees(90, 0));
    }
    public static Preetham TIME_7_AM()
    {
        return new Preetham(SphericalCoordinate.directionDegrees(90 - 15, 0));
    }
    public static Preetham TIME_8_AM()
    {
        return new Preetham(SphericalCoordinate.directionDegrees(90 - 30, 0));
    }
    public static Preetham TIME_9_AM()
    {
        return new Preetham(SphericalCoordinate.directionDegrees(90 - 45, 0));
    }
    public static Preetham TIME_10_AM()
    {
        return new Preetham(SphericalCoordinate.directionDegrees(90 - 60, 0));
    }
    public static Preetham TIME_11_AM()
    {
        return new Preetham(SphericalCoordinate.directionDegrees(90 - 75, 0));
    }
    public static Preetham TIME_12_PM()
    {
        return new Preetham(SphericalCoordinate.directionDegrees(90 - 90, 0));
    }
    public static Preetham TIME_1_PM()
    {
        return new Preetham(SphericalCoordinate.directionDegrees(-15, 0));
    }
    public static Preetham TIME_2_PM()
    {
        return new Preetham(SphericalCoordinate.directionDegrees(-30, 0));
    }
    public static Preetham TIME_3_PM()
    {
        return new Preetham(SphericalCoordinate.directionDegrees(-45, 0));
    }
    public static Preetham TIME_4_PM()
    {
        return new Preetham(SphericalCoordinate.directionDegrees(-60, 0));
    }
    public static Preetham TIME_5_PM()
    {
        return new Preetham(SphericalCoordinate.directionDegrees(-75, 0));
    }
    public static Preetham TIME_6_PM()
    {
        return new Preetham(SphericalCoordinate.directionDegrees(-90, 0));
    }
}
