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
package org.rt.core.accelerator;

import org.rt.core.math.BoundingBox;
import org.rt.core.math.Utility;

/**
 *
 * @author user
 * 
 * This class is inspired by: 
 *      "Ray Tracing with the Single Slab Hierarchy" by Eisemann et al. 
 * 
 * Main aim is to have a memory efficient BVH, in order to load bigger scenes,
 * also to be GPU memory efficient if future porting is required.
 * 
 * Currently it's in progress of writing... hence don't use it.
 * 
 */
public class SSBVH 
{
    
    BoundingBox bounds = new BoundingBox();
    
    /*
     node structure goes as follows
        SSNode
        {
            int plane;  //plane in float converted to int
            int data;   //bit 0..1  : axis (x, y, z) or leaf
                        //bit 2     : interior left or right
                        //bit 3..4  : traversal axis
                        //bit 2..5  : nPrimitives (maximum 15) if leaf
                        //bit > 5   : first node ID or first triangle ID if leaf
        }
    */
        
    int[] tree;
    
    public boolean isLeaf(int value)
    {
        return (value & 3) == 3;
    }
    
    public boolean isAxisX(int value)
    {
        return (value & 3) == 0;
    }
    
    public boolean isAxisY(int value)
    {
        return (value & 3) == 1;
    }
    
    public boolean isAxisZ(int value)
    {
        return (value & 3) == 2;
    }
    
    public int nPrimitives(int value)
    {
        return (value >> 2) & 15;
    }
    
    public int getPrimitiveOffset(int value)
    {
        return 0;
    }
    
    public int getNodeOffset(int value)
    {
        return 0;
    }
    
    public static void main(String... args)
    {
        System.out.println(Integer.toBinaryString(3));
    }
}