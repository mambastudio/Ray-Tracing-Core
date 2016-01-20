/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.image;

import java.nio.ByteBuffer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;

/**
 *
 * @author user
 */
public class Bitmap {
    int width, height;
    public WritableImage wImage = null;
    public PixelWriter pw = null;
    
    public Bitmap(int width, int height)
    {        
        this.width = width;
        this.height = height;        
        this.wImage = new WritableImage(width, height);
        this.pw = wImage.getPixelWriter();
        
        this.setNull();
    }
    
    public Bitmap(Image image)
    {
        this.width = (int) image.getWidth();
        this.height = (int) image.getHeight();
        this.wImage = new WritableImage(image.getPixelReader(), width, height);
    }
    
    public core.color.Color getColor(float x, float y)
    {
        return getColor((int)x, (int)y);
    }
    
    public core.color.Color getColor(int x, int y)
    {
        return new core.color.Color(wImage.getPixelReader().getArgb(x, y));
    }
    
    public void setColor(int x, int y, core.color.Color color)
    {
        pw.setColor(x, y, new Color(color.r, color.g, color.b, 1));
    }
   
    public final void setNull()
    {
        for(int j = 0; j<height; j++)
            for(int i = 0; i<width; i++)
                pw.setColor(i, j, Color.BLACK);               
    }
    
    public synchronized void setRGB(int x, int y, int w, int h, core.color.Color color)
    {
        int w1 = (int) (wImage.getWidth() - 1);
        int h1 = (int) (wImage.getHeight() - 1);
        
        for (int dx = Math.min(x + w , w1); dx >= x; dx--)
            for (int dy = Math.min(y + h, h1); dy >= y; dy--)
            {          
                //System.out.println(color);
                setColor(dx, dy, color);                
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
