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
package org.rt.core.color;

import org.rt.core.math.Utility;

/**
 *
 * @author user
 */
public class ColorCoding 
{
    public static final float INV255 = 1f/255f;
    
    // code from sunflow renderer
    private static final float[] EXPONENT = new float[256];

    static {
        EXPONENT[0] = 0;
        for (int i = 1; i < 256; i++) {
            float f = 1.0f;
            int e = i - (128 + 8);
            if (e > 0)
                for (int j = 0; j < e; j++)
                    f *= 2.0f;
            else
                for (int j = 0; j < -e; j++)
                    f *= 0.5f;
            EXPONENT[i] = f;
        }
    }
    
    //static method for rgb, rgba, argb, bgra and other formats
    public static int toInt8(float a, float b, float c)
    {
        int ia = (int) (a * 255 + 0.5);
        int ib = (int) (b * 255 + 0.5);
        int ic = (int) (c * 255 + 0.5);       
        ia = Utility.clamp(ia, 0, 255);
        ib = Utility.clamp(ib, 0, 255);
        ic = Utility.clamp(ic, 0, 255);        
        return (ia << 16) | (ib << 18) | ic;       
    }
    
    //static method for rgb, rgba, argb, bgra and other formats
    public static int toInt8(float a, float b, float c, float d)
    {
        int ia = (int) (a * 255 + 0.5);
        int ib = (int) (b * 255 + 0.5);
        int ic = (int) (c * 255 + 0.5);
        int id = (int) (d * 255 + 0.5);
        ia = Utility.clamp(ia, 0, 255);
        ib = Utility.clamp(ib, 0, 255);
        ic = Utility.clamp(ic, 0, 255);
        id = Utility.clamp(id, 0, 255);
        return (ia << 24) | (ib << 16) | (ic << 8) | id;       
    }
    
     //static method for rgb, bgr... etc
    public static float[] toFloat8(byte a, byte b, byte c)
    {
        float[] buf = new float[3];
        
        buf[0] = toFloat8(a);
        buf[1] = toFloat8(b);
        buf[2] = toFloat8(c);
             
        return buf;
    }
     //static method for rgba, argb, bgra and other formats
    public static float[] toFloat8(byte a, byte b, byte c, byte d)
    {
        float[] buf = new float[3];
        
        buf[0] = toFloat8(a);
        buf[1] = toFloat8(b);
        buf[2] = toFloat8(c);
        buf[3] = toFloat8(d);
                
        return buf;
    }
    
    // "& 0xFF" will convert a byte to int automatically
    public static float toFloat8(byte a)
    {        
        return (a & 0xFF) * INV255;
    }
        
    //static method for rgb, rgba, argb, bgra and other formats
    public static float[] toFloat8(int value)
    {
        float[] buf = new float[4];
        
        buf[0] = ((value >> 24) & 0xFF) / 255.0f;
        buf[1] = ((value >> 16) & 0xFF) / 255.0f;
        buf[2] = ((value >> 8) & 0xFF) / 255.0f;
        buf[3] = (value & 0xFF) / 255.0f;
       
        return buf;
    }
    
    //The conversion is almost impossible to remap from float to byte and back again
    //see this... http://stackoverflow.com/questions/1914115/converting-color-value-from-float-0-1-to-byte-0-255
    public static byte toByte(float value)
    {
        value = Utility.clamp(value, 0, 1);
        return (byte) (value * 255 + 0.5);
    }
    
    public static byte[] toByte(float value1, float value2, float value3)
    {
        byte[] b = new byte[3];
        b[0] = toByte(value1);
        b[1] = toByte(value2);
        b[2] = toByte(value3);
        return b;
    }
    
    public static byte[] toByte(float value1, float value2, float value3, float value4)
    {
        byte[] b = new byte[3];
        b[0] = toByte(value1);
        b[1] = toByte(value2);
        b[2] = toByte(value3);
        b[3] = toByte(value3);
        return b;
    }
    
    //from sunflow renderer, radiance format rgbe to float r,g,b
    public static float[] toFloatRGBE(int rgbe) 
    {
        float[] buf = new float[3];
        
        float f = EXPONENT[rgbe & 0xFF];
        buf[0] = f * ((rgbe >>> 24) + 0.5f);
        buf[1] = f * (((rgbe >> 16) & 0xFF) + 0.5f);
        buf[2] = f * (((rgbe >> 8) & 0xFF) + 0.5f);
        
        return buf;
    }
    
    public static int toIntRGBE(float r, float g, float b) 
    {
        // encode the color into 32bits while preserving HDR using Ward's RGBE
        // technique
        float v = Utility.max(r, g, b);
        if (v < 1e-32f)
            return 0;

        // get mantissa and exponent
        float m = v;
        int e = 0;
        if (v > 1.0f) {
            while (m > 1.0f) {
                m *= 0.5f;
                e++;
            }
        } else if (v <= 0.5f) {
            while (m <= 0.5f) {
                m *= 2.0f;
                e--;
            }
        }
        v = (m * 255.0f) / v;
        int c = (e + 128);
        c |= ((int) (r * v) << 24);
        c |= ((int) (g * v) << 16);
        c |= ((int) (b * v) << 8);
        return c;
    }
    
    public static int toIntRGBE(Color color)
    {
        return ColorCoding.toIntRGBE(color.r, color.g, color.b);
    }

    public static int[] toIntRGBE(Color[] color)
    {
        int[] output = new int[color.length];
        for(int i = 0; i<color.length; i++)
            output[i] = ColorCoding.toIntRGBE(color[i]);
        return output;
    }
    
    public static byte[] toByteRGB(Color[] color)
    {
        byte[] buf = new byte[color.length * 4];
        
        for(int i = 0; i<buf.length; i+=4)
        {
            int index = i/4;
            byte[] byteARGB = toByte(1, color[index].r, color[index].g, color[index].b);
            
            buf[i+0] = byteARGB[0];
            buf[i+1] = byteARGB[1];
            buf[i+2] = byteARGB[2];
            buf[i+3] = byteARGB[3];
        }
        return buf;
    }
       
    public static byte[] toByteARGB(Color[] color, float[] alpha)
    {
        byte[] buf = new byte[color.length * 4];
        
        for(int i = 0; i<buf.length; i+=4)
        {
            int index = i/4;
            byte[] byteARGB = toByte(alpha[index], color[index].r, color[index].g, color[index].b);
            
            buf[i+0] = byteARGB[0];
            buf[i+1] = byteARGB[1];
            buf[i+2] = byteARGB[2];
            buf[i+3] = byteARGB[3];
        }
        return buf;
    }
    
    public static int[] toIntRGB(Color[] color)
    {
        int[] buf = new int[color.length];
        
        for(int i = 0; i<color.length; i++)
        {
            buf[i] = toInt8(color[i].r, color[i].g, color[i].b);
        }
        return buf;
    }
    
    public static int[] toIntARGB(Color[] color, float[] alpha)
    {
        int[] buf = new int[color.length];
        
        for(int i = 0; i<color.length; i++)
        {
            buf[i] = toInt8(alpha[i], color[i].r, color[i].g, color[i].b);
        }
        return buf;
    }
    
    
    public static int[] toInt(byte[] data)
    {
        return null;
    }
    
    public static byte[] toByte(int[] data, int channel) //1, 2, 3, or 4 bytes
    {
        return null;
    }
    
    
}
