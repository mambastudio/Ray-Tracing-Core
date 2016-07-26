/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.math;

/**
 *
 * @author user
 */
public class FloatArray {
    private float[] array;
    private int size;

    public FloatArray()
    {
        array = new float[0];
        size = 0;
    }
    
    public void clear()
    {
        array = new float[10];
        size = 0;
    }

    public final void add(float i)
    {
        if (size == array.length)
        {
            float[] oldArray = array;
            array = new float[(size * 3) / 2 + 1];
            System.arraycopy(oldArray, 0, array, 0, size);
        }
        array[size] = i;
        size++;
    }
    
    public final void add(float... value)
    {
        for(float i : value)
            add(i);
    }

    public final void set(int index, float value)
    {
        array[index] = value;
    }

    public final float get(int index)
    {
        return array[index];
    }

    public final int getSize()
    {
        return size;
    }

    public final float[] trim()
    {
        if (size < array.length)
        {
            float[] oldArray = array;
            array = new float[size];
            System.arraycopy(oldArray, 0, array, 0, size);            
        }
        return array;
    }    
}
