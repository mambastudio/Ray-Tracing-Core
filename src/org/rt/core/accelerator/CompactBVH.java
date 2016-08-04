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
 */
public class CompactBVH {
    BoundingBox bounds = new BoundingBox();
       
    public static class Node
    {
        //Leaf
        //--bits 0..1       : splitting dimension
        //--bits 2..30      : offset bits (if leaf) or bits offset to first son (if node)
        //--bit 31 (sign)   : flag whether node is a leaf
        int flagDimAndOffset;   
        int splitCoordinate;
                
        public int[] getArray()
        {
            return new int[]{flagDimAndOffset, splitCoordinate};
        }
        
        public void setSplitAxis(int value)
        {
            if(!(value < 0 || value > 3))      //Is within range of 0 and 2            
                flagDimAndOffset = value & 0x3;
        }
        
        public int getSplitAxis()
        {
            return flagDimAndOffset ;
        }
        
        public boolean isLeaf()
        {
            return false;
        }
    }
           
    
    public static void main(String... args)
    {
        int value = 0;        
        value = Utility.setBitOneAt(value, 31);
        System.out.println(Integer.toBinaryString(value));
    }
}
