/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.color;

import static core.math.Utility.expf;

/**
 *
 * @author user
 */
public class CIE1931 
{
    /*
        Based on the paper "Simple Analytical Approximations to the CIE XYZ
                            Color Matching Functions" by Chris et al.
    
        Extend this class to provide array of xfits, yfits, zfits
    */
    
    // Inputs: Wavelength in nanometers
    public static float xFit_1931( float wave )
    {
        float t1 = (wave-442.0f)*((wave<442.0f)?0.0624f:0.0374f);
        float t2 = (wave-599.8f)*((wave<599.8f)?0.0264f:0.0323f);
        float t3 = (wave-501.1f)*((wave<501.1f)?0.0490f:0.0382f);
        return 0.362f*expf(-0.5f*t1*t1) + 1.056f*expf(-0.5f*t2*t2)
                                        - 0.065f*expf(-0.5f*t3*t3);
    }
    
    public static float yFit_1931( float wave )
    {
        float t1 = (wave-568.8f)*((wave<568.8f)?0.0213f:0.0247f);
        float t2 = (wave-530.9f)*((wave<530.9f)?0.0613f:0.0322f);
        return 0.821f*expf(-0.5f*t1*t1) + 0.286f*expf(-0.5f*t2*t2);
    }
    
    public static float zFit_1931( float wave )
    {
        float t1 = (wave-437.0f)*((wave<437.0f)?0.0845f:0.0278f);
        float t2 = (wave-459.0f)*((wave<459.0f)?0.0385f:0.0725f);
        return 1.217f*expf(-0.5f*t1*t1) + 0.681f*expf(-0.5f*t2*t2);
    }
    
    
    public static float getX(double radiance, double wavelength)
    {
        return (float) radiance * xFit_1931((float) wavelength);
    }
    
    public static float getY(double radiance, double wavelength)
    {
        return (float) radiance * yFit_1931((float) wavelength);
    }
    
    public static float getZ(double radiance, double wavelength)
    {
        return (float) radiance * zFit_1931((float) wavelength);
    }
    
    public static void main(String [] args)
    {
        for(int i = 300; i<= 825; i+=5)
        {
            System.out.println( String.format("(%.9f, %.9f, %.9f)", xFit_1931(i), yFit_1931(i), zFit_1931(i)));
        }
    }
}
