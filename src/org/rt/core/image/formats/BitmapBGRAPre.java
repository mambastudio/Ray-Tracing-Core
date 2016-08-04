/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.image.formats;

import org.rt.core.AbstractBitmap;
import org.rt.core.color.Color;
import org.rt.core.math.Utility;
import java.nio.ByteBuffer;
import javafx.scene.image.Image;

/**
 *
 * @author user
 */
public class BitmapBGRAPre extends AbstractBitmap
{
    //Format in this class is bgrapreinstance 
    private byte [] bgraArray = null;
   
    private final int w, h;
    
    public BitmapBGRAPre(int width, int height)
    {
        this.w = width; this.h = height;
        bgraArray = new byte[w * h * 4];
        
    }
    
    @Override
    public int getWidth() {
        return w;
    }

    @Override
    public int getHeight() {
        return h;
    }
    
    private int index(int x, int y)
    {
        return (y * w + x) * 4;
    }

    @Override
    public Color readColor(int x, int y) {
        int index = index(x, y);
        float b = (bgraArray[index    ] & 0xFF) / 255f;
        float g = (bgraArray[index + 1] & 0xFF) / 255f;
        float r = (bgraArray[index + 2] & 0xFF) / 255f;
        
        return new Color(r, g, b);
    }

    @Override
    public float readAlpha(int x, int y) 
    {
        int index = index(x, y);
        return (bgraArray[index + 3] & 0xFF) / 255f;        
    }

    @Override
    public void writeColor(Color color, float alpha, int x, int y)
    {
        byte[] bgraPre = toByteBgraPre(color, alpha);
        int index = index(x, y);
        bgraArray[index    ] = bgraPre[0];
        bgraArray[index + 1] = bgraPre[1];
        bgraArray[index + 2] = bgraPre[2];
        bgraArray[index + 3] = bgraPre[3];        
    }
    
    private byte[] toByteBgraPre(Color color, float alpha)
    {
        int ia = (int) (alpha   * 255 + 0.5);
        int ir = (int) (color.r * 255 + 0.5);
        int ig = (int) (color.g * 255 + 0.5);
        int ib = (int) (color.b * 255 + 0.5);
        ir = Utility.clamp(ir, 0, 255);
        ig = Utility.clamp(ig, 0, 255);
        ib = Utility.clamp(ib, 0, 255);
        ia = Utility.clamp(ia, 0, 255);
        
        int rgba = (ib << 24) | (ig << 16) | (ir << 8) | ia;
        
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(rgba);
        return bb.array();
    }
    
    public final byte[] getArray()
    {
        return bgraArray;
    }    

    @Override
    public Image getImage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void writeColor(Color color, float alpha, int x, int y, int w, int h) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
