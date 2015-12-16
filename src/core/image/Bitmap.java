/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.image;

import java.nio.IntBuffer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;

/**
 *
 * @author user
 */
public class Bitmap 
{
    int width, height;
    public WritableImage wImage = null; 
    WritablePixelFormat<IntBuffer> format;
    
    core.color.Color[] buffer = null;
        
    public Bitmap(int width, int height)
    {        
        this.width = width;
        this.height = height;        
        this.wImage = new WritableImage(width, height);        
        this.setNull();
    }
    
    public Bitmap(int width, int height, int [] pixels)
    {
        this.width = width;
        this.height = height;        
        this.wImage = new WritableImage(width, height); 
        wImage.getPixelWriter().setPixels(0, 0, width, height, format, pixels, 0, width);
    }
    
    public Bitmap(int width, int height, boolean hdr)
    {
        this.width = width;
        this.height = height;        
        this.wImage = new WritableImage(width, height);        
        this.setNull();
        
        if(hdr)
        {
            buffer = new core.color.Color[width * height];
            setBufferNull();
            format = WritablePixelFormat.getIntArgbInstance();
        }
    }
    
    public void setAlphaNull(float alpha)
    {
        for(int j = 0; j<height; j++)
            for(int i = 0; i<width; i++)
                wImage.getPixelWriter().setColor(i, j, Color.gray(1f, alpha)); 
    }
    
    public boolean isHDR()
    {
        return buffer != null;
    }
    
    public Bitmap(Image image)
    {
        this.width = (int) image.getWidth();
        this.height = (int) image.getHeight();
        this.wImage = new WritableImage(image.getPixelReader(), width, height);
    }
    
    public synchronized void addPixelBuffer(int x, int y, core.color.Color color)
    {
        buffer[y * width + x].addAssign(color);
    }
   
    public core.color.Color getPixel(float x, float y)
    {
        return new core.color.Color(wImage.getPixelReader().getArgb((int)x, (int)y));
    }
    
    public synchronized core.color.Color getPixelBuffer(int x, int y)
    {
        return buffer[y * width + x];
    }
     
    public synchronized void flushBuffer()
    {
        int[] arrayBuffer = new int[width * height];
        
        for(int i = 0; i < arrayBuffer.length; i++)
            arrayBuffer[i] = buffer[i].intRGBGamma();
        
        
        wImage.getPixelWriter().setPixels(0, 0, width, height, format, arrayBuffer, 0, width);
    }
    
    public synchronized void flushBuffer(float scale)
    {
        int[] arrayBuffer = new int[width * height];
        
        for(int i = 0; i < arrayBuffer.length; i++)
            arrayBuffer[i] = buffer[i].mul(scale).intRGBGamma();
                   
        wImage.getPixelWriter().setPixels(0, 0, width, height, format, arrayBuffer, 0, width);
    }
    
    public final void setNull()
    {
        for(int j = 0; j<height; j++)
            for(int i = 0; i<width; i++)
                wImage.getPixelWriter().setColor(i, j, Color.BLACK);               
    }
    
    public final void setBufferNull()
    {
        for(int i = 0; i< buffer.length; i++)
            buffer[i] = new core.color.Color();
    }
        
    public synchronized void setRGB(int x, int y, int rgb)
    {
        double r = ((rgb >> 16) & 0xFF) / 255.0f;
        double g = ((rgb >> 8) & 0xFF) / 255.0f;
        double b = (rgb & 0xFF) / 255.0f;
        
        wImage.getPixelWriter().setColor(x, y, new Color(r, g, b, 1));        
    }
           
    public synchronized void setRGB(int x, int y, int w, int h, int rgb)
    {
        int w1 = (int) (wImage.getWidth() - 1);
        int h1 = (int) (wImage.getHeight() - 1);
        
        for (int dx = Math.min(x + w , w1); dx >= x; dx--)
            for (int dy = Math.min(y + h, h1); dy >= y; dy--)
            {
                double r = ((rgb >> 16) & 0xFF) / 255.0f;
                double g = ((rgb >> 8) & 0xFF) / 255.0f;
                double b = (rgb & 0xFF) / 255.0f;
                
                wImage.getPixelWriter().setColor(dx, dy, new Color(r, g, b, 1));                
            }

    }
     
    public int getWidth()
    {
        return (int) wImage.getWidth();
    }
    
    public int getHeight()
    {
        return (int) wImage.getHeight();
    }
    
    public ImageView getImageView()
    {
        return new ImageView(wImage);
    }
    
    public Image getImage()
    {
        return wImage;
    }
}
