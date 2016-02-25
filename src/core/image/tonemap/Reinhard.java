/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.image.tonemap;

import core.AbstractTonemap;
import core.color.Color;
import core.color.RGBSpace;
import core.color.XYZ;
import core.math.Utility;

/**
 *
 * @author user
 */
public class Reinhard extends AbstractTonemap
{
    float prescale = 1.0f;
    float postscale = 1.0f;
    
    public Color toneMap(float a, float aveL, float Lwhite, Color c)
    {        
        float L = c.luminance();
        float Lscaled = a * (L/aveL);
        
       float Y = (Lscaled * (1 + Lscaled / (Lwhite * Lwhite)))/
                 (1 + Lscaled);
        
        //float Y = Lscaled;
        
        Y = Utility.check(Y);
        
        XYZ xyzColor = RGBSpace.convertRGBtoXYZ(c);
        xyzColor.xyz();
        xyzColor.Y = Y;
        xyzColor.xyYtoXYZ();

        Color c2 = RGBSpace.convertXYZtoRGB(xyzColor);  
        
        
        return c2.mul(postscale);
    }
}
