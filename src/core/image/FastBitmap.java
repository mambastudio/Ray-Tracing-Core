/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.image;

import core.color.Color;
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
    
    public synchronized void setColor(int x, int y, int w, int h, core.color.Color color)
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
