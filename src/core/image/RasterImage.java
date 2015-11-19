/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.image;

import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 *
 * @author user
 */
public class RasterImage 
{
    int width, height;
    public WritableImage wImage;
    PixelWriter pw;
    private final int w1;
    private final int h1;
    
    public RasterImage(int width, int height)
    {        
        this.width = width;
        this.height = height;
        this.w1 = (width - 1);
        this.h1 = (height - 1);
        this.wImage = new WritableImage(width, height);
        this.pw = wImage.getPixelWriter();
        this.setNull();
    }
    
    public final void setNull()
    {
        for(int j = 0; j<height; j++)
            for(int i = 0; i<width; i++)
                pw.setColor(i, j, Color.BLACK);               
    }
        
    public synchronized void setRGB(int x, int y, int rgb)
    {
        double r = ((rgb >> 16) & 0xFF) / 255.0f;
        double g = ((rgb >> 8) & 0xFF) / 255.0f;
        double b = (rgb & 0xFF) / 255.0f;
        
        pw.setColor(x, y, new Color(r, g, b, 1));        
    }
           
    public synchronized void setRGB(int x, int y, int w, int h, int rgb)
    {
        for (int dx = Math.min(x + w , this.w1); dx >= x; dx--)
            for (int dy = Math.min(y + h, this.h1); dy >= y; dy--)
            {
                double r = ((rgb >> 16) & 0xFF) / 255.0f;
                double g = ((rgb >> 8) & 0xFF) / 255.0f;
                double b = (rgb & 0xFF) / 255.0f;
                
                pw.setColor(dx, dy, new Color(r, g, b, 1));                
            }

    }
     
    public int getWidth()
    {
        return width;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public ImageView getImageView()
    {
        return new ImageView(wImage);
    }
  
}
