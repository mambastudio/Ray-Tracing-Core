/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.image.film;

import core.color.Color;
import java.nio.IntBuffer;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;

/**
 *
 * @author user
 */
public class DoubleFilm {
    Film layer1, layer2;    
    int w, h, size;    
    Color[] tonePixels = null;
    
    public DoubleFilm(int w, int h)
    {
        layer1 = new Film(w, h);
        layer2 = new Film(w, h);
        
        this.w = w;
        this.h = h;
        
        size = w*h;
        tonePixels = new Color[size];
        
        for(int i = 0; i< size; i++)
        {           
            tonePixels[i] = new Color();
        }
    }
    
    public void clear()
    {
        layer1.clear();
        layer2.clear();
        
        for(int i = 0; i<size; i++)
            tonePixels[i].setBlack();
    }
    
    public void add(Color color, int x, int y, int layer)
    {
        if(layer == 0)
            layer1.add(color, x, y);
        else if(layer == 1)
            layer2.add(color, x, y);
    }
    
    public int getWidth()
    {
        return w;
    }
    
    public int getHeight()
    {
        return h;
    }
    
    public void updatePixels(float scale)
    {
        layer1.updatePixels(scale);
        layer2.updatePixels(scale);
        
        for(int i = 0; i<size; i++)
        {
            Color color1 = layer1.tonePixels[i];
            Color color2 = layer2.tonePixels[i];
            
            
            if(!color2.isBlack())
                tonePixels[i] = color2;    
            
            if(!color1.isBlack())                
                tonePixels[i] = color1;  
            
              
            
        }
    }
    
    public Image getImage()
    {
        int[] arrayBuffer = new int[w * h];
        
        WritableImage wImage = new WritableImage(w, h); 
        WritablePixelFormat<IntBuffer> format = WritablePixelFormat.getIntArgbInstance(); 
                
        for(int i = 0; i < arrayBuffer.length; i++)
            arrayBuffer[i] = tonePixels[i].toRGBA(1);
                   
        wImage.getPixelWriter().setPixels(0, 0, w, h, format, arrayBuffer, 0, w);
        
        return wImage;
    }
    
    public Image getImageNull()
    {
        WritableImage wImage = new WritableImage(w, h); 
       
        for(int j = 0; j<h; j++)
            for(int i = 0; i<w; i++)
                wImage.getPixelWriter().setColor(i, j, javafx.scene.paint.Color.BLACK);
        
        return wImage;
    }    
}
