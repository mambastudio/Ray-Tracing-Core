/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.image;

import core.color.Color;
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author user
 */
public class Texture 
{
    private String uri;
    private Bitmap bitmap;
        
    public Texture(File file)
    {
        this(file.toURI().toString());
    }
    
    public Texture(String uri)
    {
        //load file
        this.uri = uri;
        load(uri);         
    }
    
    public Texture(Bitmap bitmap, String uri)
    {
        this.bitmap = bitmap;
        this.uri = uri;
    }
    
    @Override
    public Texture clone()
    {
        return new Texture(bitmap, uri);
    }
    
    public void setTexture(Texture texture)
    {
        if(texture != null)
        {
            bitmap = texture.getBitmap();
            uri = texture.getURI();
        }
    }
    
    public Bitmap getBitmap()
    {
        return bitmap;
    }
         
    public final void load(String uri)
    {
        // regular image, load using javafx api
        Image image = new Image(uri);
        bitmap = new Bitmap(image);
    }
    
    public Color getPixel(float x, float y) 
    {
        return bitmap.getColor(x, y);
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
        
        return bitmap.getColor(uu * getWidthF(), vv * getHeightF());
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
