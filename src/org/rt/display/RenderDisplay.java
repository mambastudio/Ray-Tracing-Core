/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.display;

import org.rt.core.AbstractDisplay;
import org.rt.core.color.Color;
import org.rt.core.system.ZoomTool;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import org.rt.core.AbstractBitmap;
import org.rt.core.color.RGBSpace;
import org.rt.core.color.XYZ;
import org.rt.core.image.formats.BitmapRGB;

/**
 *
 * @author user
 */
public class RenderDisplay extends StackPane implements AbstractDisplay
{
    private int w, h, size;    
    ImageView imageView;
    Color[] colorArray = null;
    
    @Override
    public void imageBegin() 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void imageBegin(int width, int height) 
    {
        this.w = width; this.h = height;
        this.size = width * height;
        
        ZoomTool.reset();
        
        setOnMousePressed((MouseEvent e) -> {
            ZoomTool.setMouseValue(e.getX(), e.getY());                
        });
        
        setOnMouseDragged((MouseEvent e) -> {
            ZoomTool.pan(imageView, e);            
        });
        
        setOnScroll((ScrollEvent event) -> {
            ZoomTool.zoom(imageView, event);          
        });   
        
        imageView = new ImageView();
        imageView.setImage(getImageNull());
        getChildren().add(imageView);
        
        imageView.setScaleX(1);
        imageView.setScaleY(1);
       
        imageView.setTranslateX(0);
        imageView.setTranslateY(0);       
    }

    @Override
    public  synchronized void imageUpdate(float scale) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void imageFill(float x, float y, Color c) 
    {
        imageFill((int)x, (int)y, c);
    }
    
    private void imageFill(int x, int y, Color c) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void imagePaint() 
    {        
        //Average luminance
        float aveLum = 0f;
        float N = 0f;
        for(int index = 0; index<size; index++)
            if(colorArray[index].luminance()> 0)
            {
                //System.out.println("Kubafu");
                aveLum += Math.log(0.01 + colorArray[index].luminance());
                N++;
            }
        aveLum /= N;
        aveLum = (float)Math.exp(aveLum);
                 
        for(Color c : colorArray)   
        {
            float L = c.luminance();
            XYZ xyz = RGBSpace.convertRGBtoXYZ(c);
            float Lscaled = 0.18f * (L/aveLum);
            
            xyz.xyz();
            xyz.setY(Lscaled);
            xyz.xyYtoXYZ();
            
            c.setColor(RGBSpace.convertXYZtoRGB(xyz));
            c.setColor(c.simpleGamma());
        }
       
        
        BitmapRGB bitmap = new BitmapRGB(w, h);
        
        for(int j = 0; j<h; j++)
            for(int i = 0; i<w; i++)
                bitmap.writeColor(colorArray[index(i, j)], 1, i, j);
        
        imageView.setImage(bitmap.getImage());        
    }

    @Override
    public void imageFill(Color[] c) {
        this.colorArray = c;        
    }

    @Override
    public void imageClear() 
    {
        imageView.setImage(getImageNull());
        getChildren().add(imageView);
    }

    @Override
    public int getImageWidth() {
        return w;
    }

    @Override
    public int getImageHeight() {
        return h;
    }
    
    private int index(int x, int y)
    {
        return w * y + x;
    }    
    
    private Image getImageNull()
    {
        WritableImage wImage = new WritableImage(w, h);        
        for(int j = 0; j<h; j++)
            for(int i = 0; i<w; i++)
                wImage.getPixelWriter().setColor(i, j, javafx.scene.paint.Color.BLACK);
        
        return wImage;
    }

    @Override
    public void imageFill(AbstractBitmap bitmap) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
