/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.display;

import core.AbstractDisplay;
import core.color.Color;
import core.color.RGBSpace;
import core.color.XYZ;
import core.system.ZoomTool;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;

/**
 *
 * @author user
 */
public class RenderDisplay extends StackPane implements AbstractDisplay
{
    private int w, h;
    
    private Color[] colorAccum = null;
    private  Color[] tonePixels = null;
    
    private int size;
        
    ImageView imageView;
    
    @Override
    public void imageBegin() 
    {
        imageBegin(600, 600);
    }

    @Override
    public void imageBegin(int width, int height) 
    {
        int iw, ih;
        
        if(width < 100) iw = 100; else iw = width;
        if(height < 100) ih = 100; else ih = height;
        
        this.w = iw;
        this.h = ih;
        
        this.colorAccum = new Color[width * height];
        this.tonePixels = new Color[width * height];
        
        this.size = w * h;
        
        for(int i = 0; i< size; i++)
        {
            colorAccum[i] = new Color();
            tonePixels[i] = new Color();
        }
        
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
    public void imageUpdate(float scale) 
    {
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
    }

    @Override
    public void imageFill(float x, float y, Color c) 
    {
        imageFill((int)x, (int)y, c);
    }
    
    private void imageFill(int x, int y, Color c) 
    {
        if(!c.isBad())
            colorAccum[index(x, y)].addAssign(c);
        else
            System.out.println("Color is bad " +c);
    }

    @Override
    public void imagePaint() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imageFill(Color[] c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imageClear() 
    {
        for(int i = 0; i<size; i++)
        {
            colorAccum[i].setBlack();
            tonePixels[i].setBlack();
        }
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
