/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.image.formats;

import java.nio.IntBuffer;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import org.rt.core.AbstractBitmap;
import org.rt.core.color.Color;

/**
 *
 * @author user
 */
public class BitmapRGB extends AbstractBitmap
{
    private final int w, h;
    private final int[] data;
    
    public BitmapRGB(int w, int h)
    {
        this.w = w;
        this.h = h;
        this.data = new int[w * h];
    }
    
    public BitmapRGB(int w, int h, int[] data)
    {
        this.w = w;
        this.h = h;
        this.data = data;
    }
    
    @Override
    public int getWidth() {
        return w;
    }

    @Override
    public int getHeight() {
        return h;
    }

    @Override
    public Color readColor(int x, int y) {
        int index = index(x, y);        
        float[] buf = Color.toFloat8(data[index]);        
        return new Color(buf[1], buf[2], buf[3]); //buf[0] is alpha value
    }

    @Override
    public float readAlpha(int x, int y) {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void writeColor(Color color, float alpha, int x, int y) {
        int index = index(x, y);        
        data[index] = Color.toInt8(1, color.r, color.g, color.b);        
    }
    
    private int index(int x, int y)
    {
        return x + y * w;
    }

    @Override
    public Image getImage() 
    {       
        WritableImage wImage = new WritableImage(w, h);
        WritablePixelFormat<IntBuffer> format = WritablePixelFormat.getIntArgbInstance();       
        wImage.getPixelWriter().setPixels(0, 0, w, h, format, data, 0, w);
        return wImage;
    }

    @Override
    public void writeColor(Color color, float alpha, int x, int y, int w, int h) {
        int w1 = (int) (getWidth() - 1);
        int h1 = (int) (getHeight() - 1);
        
        for (int dx = Math.min(x + w , w1); dx >= x; dx--)
            for (int dy = Math.min(y + h, h1); dy >= y; dy--)
            {          
                //System.out.println(color);
                writeColor(color, alpha, dx, dy);                
            }
    }
    
}
