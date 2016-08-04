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
package org.rt.util;

import org.rt.core.color.Color;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * @author user
 */
public class FrameBuffer
{
    private final AtomicReference<Color> [] buffer;
    private final AtomicInteger accum;    
    private final int w, h, size;
    
    private final Color [] scaledColorArray;
    
    public FrameBuffer(int w, int h)
    {
        this.w = w; this.h = h;
        this.size = w * h;
        this.buffer = new AtomicReference[size];  
        this.accum = new AtomicInteger(1);
        this.scaledColorArray = new Color[size];
        
        for(int i = 0; i < size; i++)
        {
            buffer[i] = new AtomicReference<>(new Color());
            scaledColorArray[i] = new Color();
        }
    }    
    
    public void incrementAccum()
    {
        accum.incrementAndGet();
    }
    
    public int getAccum()
    {
        return accum.get();
    }
    
    public synchronized void add(int x, int y, Color color)
    {
        if(color.isBad())
        {
            System.out.println("Bad color entry in frame buffer");
            return;
        }
        
        int index = index(x, y);
        
        while(true)
        {
            Color oldColor = buffer[index].get();
            Color newColor = oldColor.add(color);
            
            if(buffer[index].compareAndSet(oldColor, newColor))
                return;
        }
    }    
    
    public Color get(int x, int y)
    {
        return buffer[index(x, y)].get();
    }
    
    public Color get(int index)
    {
        return buffer[index].get();
    }
    
    public Color getScaled(int x, int y)
    {
        return get(x, y).mul(1f/getAccum());
    }
    
    public Color getScaled(int index)
    {
        return get(index).mul(1f/getAccum());
    }
        
    public void scaleBuffer()
    {
        for(int i = 0; i<size; i++)
        {
            scaledColorArray[i].setColor(getScaled(i));
        }
    }
    
    public Color[] getScaledColorArray()
    {
        return scaledColorArray;
    }
    
    private int index(int x, int y)
    {
        return (y * w + x);
    }
}
