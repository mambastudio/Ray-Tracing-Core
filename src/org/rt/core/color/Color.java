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
import java.io.Serializable;
import static java.lang.Double.min;
import static java.lang.Math.floor;
import static java.lang.Math.pow;
import java.nio.ByteBuffer;

/**
 *
 * @author user
 */
public final class Color implements Serializable
{
    public float r, g, b, a;
    
    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color WHITE = new Color(1, 1, 1);
    public static final Color RED = new Color(1, 0, 0);
    public static final Color GREEN = new Color(0, 1, 0);
    public static final Color BLUE = new Color(0, 0, 1);
    public static final Color YELLOW = new Color(1, 1, 0);
    public static final Color CYAN = new Color(0, 1, 1);
    public static final Color MAGENTA = new Color(1, 0, 1);
    public static final Color GRAY = new Color(0.5f, 0.5f, 0.5f);
    public static final Color LIGHTGRAY = new Color(0.8f);

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

    public Color()
    {
        r = g = b = 0;
        a = 1;
    }
    
    public Color(float value)
    {
        r = g = b = value;
        a = 1;
    }

    public Color(float r, float g, float b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        
        this.r = zeroCeiling(r);
        this.g = zeroCeiling(g);
        this.b = zeroCeiling(b);
    }
    
    public Color(float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        
        this.r = zeroCeiling(r);
        this.g = zeroCeiling(g);
        this.b = zeroCeiling(b);
        this.a = zeroCeiling(a);
    }
    
    public Color(double r, double g, double b)
    {
        this((float)r, (float)g, (float)b);
    }
    
    public Color(Color color)
    {
        this.r = color.r; this.g = color.g; this.b = color.b;
    }
    
    public float zeroCeiling(float value)
    {
        if(value < 0) return 0;
        else return value;
    }
    
    public Color(javafx.scene.paint.Color color)
    {
        r = (float)color.getRed();
        g = (float)color.getGreen();
        b = (float)color.getBlue();
    }
        
    public Color(int rgb)
    {
        r = ((rgb >> 16) & 0xFF) / 255.0f;
        g = ((rgb >> 8) & 0xFF) / 255.0f;
        b = (rgb & 0xFF) / 255.0f;
    }
    
    public final void init()
    {
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
    
    public float getMax()
    {
        return Utility.max(r, g, b);
    }
    
    public float getMin()
    {
        return Utility.min(r, g, b);
    }
    
    public final boolean isBad()
    {
        return (Float.isNaN(this.r)) || (Float.isNaN(this.g)) || (Float.isNaN(this.b)) ||
               (Float.isInfinite(this.r)) || (Float.isInfinite(this.g)) || (Float.isInfinite(this.b));
    }
    
    public final Color simpleGamma()
    {
        float gamma = 2.2f;        
        Color color = new Color((float) Math.pow(r, 1f/gamma),
                         (float) Math.pow(g, 1f/gamma),
                         (float) Math.pow(b, 1f/gamma));
        
        if(color.isBad())
            System.out.println(r+ " " +g+ " " +b);
        
        return color;
    }

    public final Color simpleGamma(float gamma)
    {
        return new Color((float) Math.pow(r, 1f/gamma),
                         (float) Math.pow(g, 1f/gamma),
                         (float) Math.pow(b, 1f/gamma));
    }
    
    public void setBlack()
    {
        this.r = this.g = this.b = 0;
    }

    public static Color add(Color c1, Color c2)
    {
        Color c = new Color();
        c.r = c1.r + c2.r;
        c.g = c1.g + c2.g;
        c.b = c1.b + c2.b;

        return c;
    }

    public final Color madd(float s, Color c)
    {
        r += (s * c.r);
        g += (s * c.g);
        b += (s * c.b);
        return this;
    }
    
    public final Color add(Color c)
    {
        return new Color(r + c.r, g + c.g, b + c.b);
    }
    
    public final void addAssign(Color c)
    {
        r += c.r;
        g += c.g;
        b += c.b;
    }
    
    public final void mulAssign(float c)
    {
        r *= c;
        g *= c;
        b *= c;
    }
    
    public final void mulAssign(Color c)
    {
        r *= c.r;
        g *= c.g;
        b *= c.b;
    }

    public final Color clamp()
    {
        Color c = new Color();

        c.r = Utility.clamp(r, 0, 1);
        c.g = Utility.clamp(g, 0, 1);
        c.b = Utility.clamp(b, 0, 1);

        return c;
    }
    
    public float max()
    {        
        if (r < g)
            r = g;
        if (r < b)
            r = b;
        return r;    
    }
    
    public final Color mul(float s)
    {
        return new Color(r * s,
                         g * s,
                         b * s);
    }
    
    public final Color div(float s)
    {
        return new Color(r / s,
                         g / s,
                         b / s);
    }

    public final Color mul(Color c)
    {
        return new Color(r * c.r,
                         g * c.g,
                         b * c.b);
    }

    public static Color mul(Color c1, Color c2)
    {
        return Color.mul(c1, c2, new Color());
    }

    public static Color mul(Color c1, Color c2, Color dest)
    {
        dest.r = c1.r * c2.r;
        dest.g = c1.g * c2.g;
        dest.b = c1.b * c2.b;
        return dest;
    }

    public static Color mul(float s, Color c, Color dest)
    {
        dest.r = s * c.r;
        dest.g = s * c.g;
        dest.b = s * c.b;
        return dest;
    }

    public static Color mul(float s, Color c)
    {
        return Color.mul(s, c, new Color());
    }

    public final boolean isBlack()
    {
        return r <= 0 && g <= 0 && b <= 0;
    }
    
    public final boolean isNan()
    {
        return Float.isNaN(r) || Float.isNaN(r) || Float.isNaN(r);
    }

    public final void setColor(Color c)
    {
        r = c.r;
        g = c.g;
        b = c.b;
    }
    public final Color setRGB(int rgb)
    {
        r = ((rgb >> 16) & 0xFF) / 255.0f;
        g = ((rgb >> 8) & 0xFF) / 255.0f;
        b = (rgb & 0xFF) / 255.0f;
        return this;
    }

    public final Color setRGBE(int rgbe)
    {
        float f = EXPONENT[rgbe & 0xFF]*2;
        r = f * ((rgbe >> 24) & 0xFF);
        g = f * ((rgbe >> 16) & 0xFF);
        b = f * ((rgbe >> 8)& 0xFF);
        return this;
    }
    
    public final byte[] toByteBgraPre()
    {
        int ia = (int) (255 + 0.5);
        int ir = (int) (r * 255 + 0.5);
        int ig = (int) (g * 255 + 0.5);
        int ib = (int) (b * 255 + 0.5);
        ir = Utility.clamp(ir, 0, 255);
        ig = Utility.clamp(ig, 0, 255);
        ib = Utility.clamp(ib, 0, 255);
        ia = Utility.clamp(ia, 0, 255);
        
        int rgba = (ib << 24) | (ig << 16) | (ir << 8) | ia;
        
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(rgba);
        return bb.array();
    }
    
    public static byte[] getByteBGRA_Pre_NullAlpha()
    {
        int ia = (int) (0);
        int ir = (int) (0);
        int ig = (int) (0);
        int ib = (int) (0);
        ir = Utility.clamp(ir, 0, 255);
        ig = Utility.clamp(ig, 0, 255);
        ib = Utility.clamp(ib, 0, 255);
        ia = Utility.clamp(ia, 0, 255);
        
        int rgba = (ib << 24) | (ig << 16) | (ir << 8) | ia;
        
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(rgba);
        return bb.array();
    }
    
    public final int toRGB()
    {
        int ir = (int) (r * 255 + 0.5);
        int ig = (int) (g * 255 + 0.5);
        int ib = (int) (b * 255 + 0.5);
        ir = Utility.clamp(ir, 0, 255);
        ig = Utility.clamp(ig, 0, 255);
        ib = Utility.clamp(ib, 0, 255);
        return (ir << 16) | (ig << 8) | ib;
    }

    public final int toRGBA(float a)
    {
        int ia = (int) (a * 255 + 0.5);
        int ir = (int) (r * 255 + 0.5);
        int ig = (int) (g * 255 + 0.5);
        int ib = (int) (b * 255 + 0.5);
        ir = Utility.clamp(ir, 0, 255);
        ig = Utility.clamp(ig, 0, 255);
        ib = Utility.clamp(ib, 0, 255);
        ia = Utility.clamp(ia, 0, 255);

        return (ia << 24) | (ir << 16) | (ig << 8) | ib;
    }

    public final int toRGBE() {
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
    
    public double clamp(float x) 
    {
        return x<0 ? 0 : x>1 ? 1 : x;
    }

    public int toInt(float x)
    { 
        return (int)(pow(clamp(x),1/2.2)*255+.5);
    }
    
    public int intRGBGamma()
    {
        int x = toInt(r);
        int y = toInt(g);
        int z = toInt(b);
        int a = toInt(1);
        
        return (a << 24) | (x << 16) | (y << 8) | z;        
    }
    
    public float luminance()
    {
        return 0.212671f * r + 
               0.715160f * g +
               0.072169f * b;
    }
    
    @Override
    public Color clone()
    {
        return new Color(r, g, b);
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
    public static float[] toFloat8(byte a, byte b, byte c, byte d)
    {
        float INV255 = 1f/255;
        float[] buf = new float[4];
        
        buf[0] = (a & 0xFF) * INV255;
        buf[1] = (b & 0xFF) * INV255;
        buf[2] = (c & 0xFF) * INV255;
        buf[3] = (d & 0xFF) * INV255;
        
        return buf;
    }
    
     //static method for rgb, rgba, argb, bgra and other formats
    public static float[] toFloat8(byte a, byte b, byte c)
    {
        float INV255 = 1f/255;
        float[] buf = new float[3];
        
        buf[0] = (a & 0xFF) * INV255;
        buf[1] = (b & 0xFF) * INV255;
        buf[2] = (c & 0xFF) * INV255;
                
        return buf;
    }
    
    // "& 0xFF" will convert a byte to int automatically
    public static float toFloat8(byte a)
    {
        float INV255 = 1f/255;        
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
    
    public static final Color blend(Color c1, Color c2, float b) {
        return blend(c1, c2, b, new Color());
    }

    public static final Color blend(Color c1, Color c2, float b, Color dest) {
        dest.r = (1.0f - b) * c1.r + b * c2.r;
        dest.g = (1.0f - b) * c1.g + b * c2.g;
        dest.b = (1.0f - b) * c1.b + b * c2.b;
        return dest;
    }

    public static final Color blend(Color c1, Color c2, Color b) {
        return blend(c1, c2, b, new Color());
    }

    public static final Color blend(Color c1, Color c2, Color b, Color dest) {
        dest.r = (1.0f - b.r) * c1.r + b.r * c2.r;
        dest.g = (1.0f - b.g) * c1.g + b.g * c2.g;
        dest.b = (1.0f - b.b) * c1.b + b.b * c2.b;
        return dest;
    }
    
    @Override
    public String toString()
    {
        return "r " +r+ " g " +g+ " b " +b;
    }
    
    
}
