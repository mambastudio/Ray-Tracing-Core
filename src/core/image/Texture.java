/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.image;

import core.image.Bitmap;
import core.color.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author user
 */
public class Texture 
{
    private final String uri;
    private Bitmap bitmap;
        
    public Texture(String uri)
    {
        //load file
        this.uri = uri;
        load(uri);         
    }
    
    public final void load(String uri)
    {
        // regular image, load using javafx api
        Image image = new Image(uri);
        bitmap = new Bitmap(image);
    }
    
    public Color getPixel(float x, float y) 
    {
        return bitmap.getPixel(x, y);
    }
    
    public Color getTexelUV(float u, float v)
    {
        float uu, vv;
        
        if(u >= 0)        
            uu = u - (int)u;
        else
            uu = Math.abs(u) - (int)Math.abs(u);
        
        if(v >= 0)        
            vv = v - (int)v;
        else
            vv = Math.abs(v) - (int)Math.abs(v);
        
        return bitmap.getPixel(uu * getWidthF(), vv * getHeightF());
    }
    
    public String getURI()
    {
        return uri;
    }   
    
    public Image getImage()
    {
        return bitmap.getImage();
    }
    
    public ImageView getImageView()
    {
        return bitmap.getImageView();
    }
    
    public float getWidthF()
    {
        return bitmap.getWidth();
    }
    
    public float getHeightF()
    {
        return bitmap.getHeight();
    }
}
