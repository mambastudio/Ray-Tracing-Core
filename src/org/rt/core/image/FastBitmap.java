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
package org.rt.core.image;

import org.rt.core.color.Color;
import java.nio.ByteBuffer;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;

/**
 *
 * @author user
 */
public class FastBitmap 
{
    
    private final int width, height;
    private ByteImagePixels pixels = null;
    
    //For javafx ui
    private WritableImage image = null;
    private WritablePixelFormat<ByteBuffer> format = null;
    
    public FastBitmap(int width, int height)
    {
        this.width = width;
        this.height = height;
        this.pixels = new ByteImagePixels(width, height);
        this.image = new WritableImage(width, height);
        this.format = WritablePixelFormat.getByteBgraPreInstance();
        
        setNull();
    }
    
    public FastBitmap(double width, double height)
    {
        this((int)width, (int)height);
    }
    
    public FastBitmap(float width, float height)
    {
        this((int)width, (int)height);
    }
    
    public synchronized void setColor(int x, int y, int w, int h, org.rt.core.color.Color color)
    {
        int w1 = (int) (image.getWidth() - 1);
        int h1 = (int) (image.getHeight() - 1);
        
        for (int dx = Math.min(x + w , w1); dx >= x; dx--)
            for (int dy = Math.min(y + h, h1); dy >= y; dy--)
            {          
                //System.out.println(color);
                setColor(dx, dy, color);                
            }
    }
    
    public void setColor(float x, float y, Color color)
    {
        setColor((int)x, (int)y, color);
    }
    
    public void setColor(int x, int y, Color color)
    {
        pixels.setColor(x, y, color);
    }
    
    public final void setNull()
    {
        for(int j = 0; j<height; j++)
            for(int i = 0; i<width; i++)
                pixels.setColor(i, j, Color.BLACK);
    }
    
    
    public final void setAlphaNull()
    {
        for(int j = 0; j<height; j++)
            for(int i = 0; i<width; i++)
                pixels.setColorNullAlpha(i, j);
    }
    
    public Image getImage()
    {        
        image.getPixelWriter().setPixels(0, 0, width, height, format, pixels.getByteArray(), 0, width * 4);
        return image;
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public int getHeight()
    {
        return height;
    }
    
}
