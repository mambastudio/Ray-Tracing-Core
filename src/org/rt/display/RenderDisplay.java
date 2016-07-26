/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.display;

import org.rt.core.AbstractDisplay;
import org.rt.core.color.Color;
import org.rt.core.color.RGBSpace;
import org.rt.core.color.XYZ;
import org.rt.core.system.ZoomTool;
import java.nio.IntBuffer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;

/**
 *
 * @author user
 */
public class RenderDisplay extends StackPane implements AbstractDisplay
{
    private int w, h, size;    
    ImageView imageView;
    
    @Override
    public void imageBegin() 
    {
        
    }

    @Override
    public void imageBegin(int width, int height) 
    {
        this.w = width; this.h = height;
        
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
        /*
        //copy first
        System.arraycopy(colorAccum, 0, tonePixels, 0, w * h);
        
        //Scale accumulation
        for(int index = 0; index<size; index++)                  
            tonePixels[index].setColor(colorAccum[index].mul(scale));
        
        //Average luminance
        float aveLum = 0f;
        float N = 0f;
        for(int index = 0; index<size; index++)
            if(tonePixels[index].luminance()> 0)
            {
                aveLum += Math.log(0.01 + tonePixels[index].luminance());
                N++;
            }
        aveLum /= N;
        aveLum = (float)Math.exp(aveLum);
                 
        System.out.println(aveLum);
               
        for(Color c : tonePixels)   
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
                */
    }

    @Override
    public synchronized void imageFill(float x, float y, Color c) 
    {
        imageFill((int)x, (int)y, c);
    }
    
    private void imageFill(int x, int y, Color c) 
    {
        
    }

    @Override
    public void imagePaint() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imageFill(Color[] c) {
        int[] arrayBuffer = new int[w * h];
        
        WritableImage wImage = new WritableImage(w, h); 
        WritablePixelFormat<IntBuffer> format = WritablePixelFormat.getIntArgbInstance(); 
                
        for(int i = 0; i < arrayBuffer.length; i++)
            arrayBuffer[i] = c[i].toRGBA(1);
                   
        wImage.getPixelWriter().setPixels(0, 0, w, h, format, arrayBuffer, 0, w);
        imageView.setImage(wImage);
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
}
