/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.accelerator;

import core.math.BoundingBox;
import core.math.Utility;

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
