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
package org.rt.core.image.formats;

import java.nio.ByteBuffer;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import org.rt.core.AbstractBitmap;
import org.rt.core.color.Color;
import org.rt.core.color.ColorCoding;

/**
 *
 * @author user
 */
public class BitmapBGRA extends AbstractBitmap
{
    private final byte[] data;
    private final int w, h;
    
    public BitmapBGRA(int w, int h)
    {
        this.data = new byte[w * h * 4];
        this.w = w; this.h = h;
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
        float[] buf = ColorCoding.toFloat8(data[index+0], data[index+1], data[index+2]);
        return new Color(buf[2], buf[1], buf[0]); 
    }

    @Override
    public float readAlpha(int x, int y) {
        int index = index(x, y);        
        return ColorCoding.toFloat8(data[index+3]);
    }

    @Override
    public void writeColor(Color color, float alpha, int x, int y) {
        int index = index(x, y); 
        byte[] buf = ColorCoding.toByte(color.r, color.g, color.b);
        data[index+0] = buf[2];
        data[index+1] = buf[1];
        data[index+2] = buf[0];
        data[index+3] = ColorCoding.toByte(alpha);
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

    @Override
    public Image getImage() {
        WritableImage wImage = new WritableImage(w, h);        
        WritablePixelFormat<ByteBuffer> pixelFormat = WritablePixelFormat.getByteBgraInstance();        
        wImage.getPixelWriter().setPixels(0, 0, w, h, pixelFormat, data, 0, w * 4);
        return wImage;
    }
    
    private int index(int x, int y)
    {
        return (x + y * w ) * 4;
    }

    @Override
    public void writeColor(Color[] color, float[] alpha, int x, int y, int w, int h) {
        System.out.println(color.length);
        for(int j = y; j<h; j++)
            for(int i = x; i<w; i++)
            {
                Color colorChannel = color[i + j * w];
                float alphaChannel = 1;
                
                if(alpha != null) alphaChannel = alpha[i + j * w];
                                
                writeColor(colorChannel, alphaChannel, i, j);
            }
    }
}
