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
package org.rt.core.image.tonemap;

import org.rt.core.AbstractTonemap;
import org.rt.core.color.Color;
import org.rt.core.color.RGBSpace;
import org.rt.core.color.XYZ;
import org.rt.core.math.Utility;

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
