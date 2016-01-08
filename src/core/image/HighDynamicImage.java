/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.image;

import core.color.Color;
import core.coordinates.Point2i;
import core.math.Utility;
import core.tonemap.Reinhard;

/**
 *
 * @author user
 */
public class HighDynamicImage {
    float[] r, g, b;
    float width, height;
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
    
    public HighDynamicImage(int width, int height)
    {
        r = new float[width * height];
        g = new float[width * height];
        b = new float[width * height];
        
        this.width = width;
        this.height = height;
    }
    
    public HighDynamicImage(int width, int height, int[] rgbe)
    {
        this(width, height);
        
        for(int j = 0; j<height; j++)
            for(int i = 0; i<width; i++)
            {
                int index = getIndex(i, j);
                setRGBE(i, j, rgbe[index]);
            }
    }
    
    public HighDynamicImage(int width, int height, float[] r, float[] g, float[] b)
    {
        this.width = width;
        this.height = height;
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public float luminance(float x, float y)
    {
        Color color = getColor(x, y);
        return color.luminance();
    }
    
    public float getWidth()
    {
        return width;
    }
    
    public float getHeight()
    {
        return height;
    }
    
    public int getWidthInt()
    {
        return (int)width;
    }
    
    public int getHeightInt()
    {
        return (int)height;
    }
    
    public void setColor(float x, float y, Color color)
    {
        int index = getIndex(x, y);
       
        r[index] = color.r;
        g[index] = color.g;
        b[index] = color.b;
    }
    
    public Color getColor(float x, float y)
    {
        if ((x >= 0) && (x < width) && (y >= 0) && (y < height))
        {
            int index = getIndex(x, y);
            return new Color(r[index], g[index], b[index]);
        }
        else      
            return new Color();
       
    }
    
    public final int[] toIntRGBA(float a)
    {
        int[] rgba = new int[(int)(width * height)];
        for(int j = 0; j<height; j++)
            for(int i = 0; i<width; i++)
            {
                int index = getIndex(i, j);
                rgba[index] = toRGBA(i, j, a);
            }
        return rgba;
    }
    
    public final int toRGBA(float x, float y, float a)
    {
        int index = getIndex(x, y);
        
        int ia = (int) (a * 255 + 0.5);
        int ir = (int) (r[index] * 255 + 0.5);
        int ig = (int) (g[index] * 255 + 0.5);
        int ib = (int) (b[index] * 255 + 0.5);
        
        ir = Utility.clamp(ir, 0, 255);
        ig = Utility.clamp(ig, 0, 255);
        ib = Utility.clamp(ib, 0, 255);
        ia = Utility.clamp(ia, 0, 255);

        return (ia << 24) | (ir << 16) | (ig << 8) | ib;
    }
    
    public final int toRGB(float x, float y)
    {
        int index = getIndex(x, y);
        
        int ir = (int) (r[index] * 255 + 0.5);
        int ig = (int) (g[index] * 255 + 0.5);
        int ib = (int) (b[index] * 255 + 0.5);
        ir = Utility.clamp(ir, 0, 255);
        ig = Utility.clamp(ig, 0, 255);
        ib = Utility.clamp(ib, 0, 255);
        return (ir << 16) | (ig << 8) | ib;
    }
    
    public Color getColor(Point2i uv)
    {
        return getColor(uv.x, uv.y);
    }
        
    public Color getGaussianColor(float i, float j)
    {
        float radius = 1;
        float rs = (float) Math.ceil(radius * 2.57);
        
        float valr = 0, valg = 0, valb = 0, wsum = 0;
        
        for(float y1 = j-rs; y1<j+rs+1; y1++)
            for(float x1 = i-rs; x1<i+rs+1; x1++) 
            {
                float x = Math.min(width-1, Math.max(0, x1));
                float y = Math.min(height-1, Math.max(0, y1));
                float dsq = (x1-i)*(x1-i)+(y1-j)*(y1-j);
                float wght = (float) (Math.exp( -dsq / (2*radius*radius)) / (Math.PI*2*radius*radius));
                
                int index = getIndex(x, y);
                valr += r[index] * wght;  
                valg += g[index] * wght;
                valb += b[index] * wght;
                wsum += wght;
            }
        
        valr = valr / wsum;
        valg = valg / wsum;
        valb = valb / wsum;
        
        return new Color(valr, valg, valb);
    }
    
    public Color getGaussianColor(Point2i uv)
    {
        return HighDynamicImage.this.getGaussianColor(uv.x, uv.y);
    }
    
    private int getIndex(float x, float y)
    {
        int ix = (int)x, iy = (int)y;
        return (int) (iy*width + ix);
    }
    
    public final void setRGBE(float x, float y, int rgbe)
    {
        int index = getIndex(x, y);
        
        float f = EXPONENT[rgbe & 0xFF]*2;
        r[index] = f * ((rgbe >> 24) & 0xFF);
        g[index] = f * ((rgbe >> 16) & 0xFF);
        b[index] = f * ((rgbe >> 8)& 0xFF);        
    }
    
    public final int toRGBE(float x, float y) 
    {        
        // encode the color into 32bits while preserving HDR using Ward's RGBE
        // technique
        
        int index = getIndex(x, y);
        
        float v = Utility.max(r[index], g[index], b[index]);
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
        c |= ((int) (r[index] * v) << 24);
        c |= ((int) (g[index] * v) << 16);
        c |= ((int) (b[index] * v) << 8);
        return c;
    }
    
    public HighDynamicImage resize(int w2, int h2)
    {
        float [] r1 = new float[w2 * h2], g1 = new float[w2 * h2], b1 = new float[w2 * h2];
        float a, b, c, d;
        int index, x, y ;
        float x_ratio = ((float)(width-1))/w2 ;
        float y_ratio = ((float)(height-1))/h2 ;
        float x_diff, y_diff, blue, red, green ;
        int offset = 0 ;
        
        for (int i=0;i<h2;i++) 
            for (int j=0;j<w2;j++) 
            {
                x = (int)(x_ratio * j) ;
                y = (int)(y_ratio * i) ;
                x_diff = (x_ratio * j) - x ;
                y_diff = (y_ratio * i) - y ;
                index = (y*(int)width+x) ;
                
                // Yb = Ab(1-w)(1-h) + Bb(w)(1-h) + Cb(h)(1-w) + Db(wh)
                //red channel
                a = r[index] ;
                b = r[index+1] ;
                c = r[index+ (int)width] ;
                d = r[index+(int)width+1] ;
                red = (a)*(1-x_diff)*(1-y_diff) + (b)*(x_diff)*(1-y_diff) +
                        (c)*(y_diff)*(1-x_diff)   + (d)*(x_diff*y_diff);
                
                //green channel
                a = g[index] ;
                b = g[index+1] ;
                c = g[index+ (int)width] ;
                d = g[index+(int)width+1] ;
                green = (a)*(1-x_diff)*(1-y_diff) + (b)*(x_diff)*(1-y_diff) +
                        (c)*(y_diff)*(1-x_diff)   + (d)*(x_diff*y_diff);
                
                // blue element                
                a = this.b[index] ;
                b = this.b[index+1] ;
                c = this.b[index+ (int)width] ;
                d = this.b[index+(int)width+1] ;
                blue = (a)*(1-x_diff)*(1-y_diff) + (b)*(x_diff)*(1-y_diff) +
                        (c)*(y_diff)*(1-x_diff)   + (d)*(x_diff*y_diff);

                r1[offset] = red;
                g1[offset] = green;
                b1[offset] = blue;
                offset++;
            }
        return new HighDynamicImage(w2, h2, r1, g1, b1);
    }
    
    public HighDynamicImage bestResizeFit(int size)
    {
        if(width > height)
        {
            int w1 = (int) (width/height * size);
            return resize(w1, size);
        }
        else if(height > width)
        {
            int h1 = (int) (height/width * size);
            return resize(size, h1);
        }
        else
             return resize(size, size);
    }
    
    public HighDynamicImage tonemap()
    {
        Reinhard tonemap = new Reinhard();
        float maxLum = 0f;
        float aveLum = 0f;
        float N = 0f;
        
        for(int j = 0; j < height; j++)
            for(int i = 0; i < width; i++)
            {
                Color color = getColor(i, j);
            
                if(color.luminance() > maxLum)
                    maxLum = color.luminance();
            
                if(color.luminance() > 0f)
                {
                    aveLum += Math.log(0.01 + color.luminance());
                    N++;
                }
            }
        
        aveLum /= N;
        aveLum = (float)Math.exp(aveLum);
        
        float[] r1 = new float[r.length], g1 = new float[g.length], b1 = new float[b.length];
        
        for(int j = 0; j < height; j++)
            for(int i = 0; i < width; i++)
            {
                Color color = getColor(i, j);
                Color color1 = tonemap.toneMap(0.18f, aveLum, maxLum, color).simpleGamma();
                     
                int index = getIndex(i, j);
                
                r1[index] = color1.r;
                g1[index] = color1.g;
                b1[index] = color1.b;
            }
        
        return new HighDynamicImage((int)width, (int)height, r1, g1, b1);
    }
    
    public HighDynamicImage gaussianBlur()
    {        
        float[] r1 = new float[r.length], g1 = new float[g.length], b1 = new float[b.length];
        
        float rr = 3;
        float rs = 1;
        for(int i=0; i<height; i++)
            for(int j=0; j<width; j++) 
            {
                float valr = 0, valg = 0, valb = 0, wsum = 0;
                for(float iy = i-rs; iy<i+rs+1; iy++)
                    for(float ix = j-rs; ix<j+rs+1; ix++) 
                    {
                        float x = Math.min(width-1, Math.max(0, ix));
                        float y = Math.min(height-1, Math.max(0, iy));
                        float dsq = (ix-j)*(ix-j)+(iy-i)*(iy-i);
                        float wght = (float) (Math.exp( -dsq / (2*rr*rr)) / (Math.PI*2*rr*rr));
                                                
                        int index = (int) (y*width+x);
                        valr += r[index] * wght;  
                        valg += g[index] * wght;
                        valb += b[index] * wght;
                        wsum += wght;
                    }
                int index = (int) (i*width+j);
                r1[index] = valr / wsum;
                g1[index] = valg / wsum;
                b1[index] = valb / wsum;
                
            }
        return new HighDynamicImage((int)width, (int)height, r1, g1, b1);
    }
}
