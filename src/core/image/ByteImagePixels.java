/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.image;

import core.color.Color;
import java.nio.ByteBuffer;

/**
 *
 * @author user
 */
public class ByteImagePixels
{
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
    
    public void setColor(int x, int y, Color color)
    {
        byte[] bgra_pre = color.toByteBGRA_Pre();
        int index = index(x, y);
        bgraArray[index    ] = bgra_pre[0];
        bgraArray[index + 1] = bgra_pre[1];
        bgraArray[index + 2] = bgra_pre[2];
        bgraArray[index + 3] = bgra_pre[3];
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
}
