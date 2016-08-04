/* 
 * The MIT License
 *
 * Copyright 2016 user.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.rt.core.image;

import org.rt.core.color.Color;
import java.io.File;
import java.io.Serializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author user
 */
public class Texture implements Serializable
{
    private String uri;
    private ByteImagePixels bitmap;
        
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
    
    public Texture(ByteImagePixels bitmap, String uri)
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
            bitmap = texture.getByteImagePixels();
            uri = texture.getURI();
        }
    }
    
    public ByteImagePixels getByteImagePixels()
    {
        return bitmap;
    }
         
    public final void load(String uri)
    {
        // regular image, load using javafx api
        Image image = new Image(uri);
        bitmap = ByteImagePixels.getByteImage(image);
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
