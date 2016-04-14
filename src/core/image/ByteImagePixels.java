/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.image;

import core.color.Color;
import core.math.Utility;
import java.io.Serializable;
import java.nio.ByteBuffer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;

/**
 *
 * @author user
 */
public class ByteImagePixels implements Serializable
{
    //Format in this class is bgrapreinstance 
    byte [] bgraArray = null;
    int w, h;
    
    public ByteImagePixels(int w, int h)
    {
        this.w = w;
        this.h = h;
        
        bgraArray = new byte[w * h * 4];
    }
    
    private int index(int x, int y)
    {
        return (y * w + x) * 4;
    }
    
    public Color getColor(float x, float y)
    {
        return getColor((int)x, (int)y);
    }
    
    public int getWidth()
    {
        return w;
    }
    
    public int getHeight()
    {
        return h;
    }
    
    public Color getColor(int x, int y)
    {
        int index = index(x, y);
        float b = (bgraArray[index    ] & 0xFF) / 255f;
        float g = (bgraArray[index + 1] & 0xFF) / 255f;
        float r = (bgraArray[index + 2] & 0xFF) / 255f;
        float a = (bgraArray[index + 3] & 0xFF) / 255f;
        return new Color(r, g, b, a);
    }
    
        
    public void setColor(int x, int y, Color color)
    {
        byte[] bgraPre = color.toByteBgraPre();
        int index = index(x, y);
        bgraArray[index    ] = bgraPre[0];
        bgraArray[index + 1] = bgraPre[1];
        bgraArray[index + 2] = bgraPre[2];
        bgraArray[index + 3] = bgraPre[3];
    }
    
    public void setColorNullAlpha(int x, int y)
    {
        byte[] bgra_pre = getByteBgraPreNullAlpha();
        int index = index(x, y);
        bgraArray[index    ] = bgra_pre[0];
        bgraArray[index + 1] = bgra_pre[1];
        bgraArray[index + 2] = bgra_pre[2];
        bgraArray[index + 3] = bgra_pre[3];
    }
    
    public static byte[] getByteBgraPreNullAlpha()
    {
        int ia = (int) (0);
        int ir = (int) (0);
        int ig = (int) (0);
        int ib = (int) (0);
        ir = Utility.clamp(ir, 0, 255);
        ig = Utility.clamp(ig, 0, 255);
        ib = Utility.clamp(ib, 0, 255);
        ia = Utility.clamp(ia, 0, 255);
        
        int rgba = (ib << 24) | (ig << 16) | (ir << 8) | ia;
        
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(rgba);
        return bb.array();
    }
    
    public ByteBuffer getByteBuffer()
    {
         ByteBuffer bb = ByteBuffer.allocate(bgraArray.length);
         bb.put(bgraArray);
         return bb;
    }
    
    public byte[] getByteArray()
    {
        return bgraArray;
    }
    
    public Image getImage()
    {
        WritableImage image = new WritableImage(w, h);
        image.getPixelWriter().setPixels(0, 0, w, h, WritablePixelFormat.getByteBgraPreInstance(), bgraArray, 0, w * 4);
        return image;
    }
            
    public static ByteImagePixels getByteImage(Image image)
    {
        int w = (int) image.getWidth();
        int h = (int) image.getHeight();        
        ByteImagePixels byteImage = new ByteImagePixels(w, h);         
        image.getPixelReader().getPixels(0, 0, w, h, WritablePixelFormat.getByteBgraPreInstance(), byteImage.getByteArray(), 0, w * 4);
        return byteImage;
    }   
    
    public ImageView getImageView()
    {
        return new ImageView(getImage());
    }
}
