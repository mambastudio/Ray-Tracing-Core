/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.math;

/**
 *
 * @author user
 */
public class IntArray 
{
    private int[] array;
    private int size;

    public IntArray()
    {
        array = new int[0];
        size = 0;
    }

    public final void add(int i)
    {
        if (size == array.length)
        {
            int[] oldArray = array;
            array = new int[(size * 3) / 2 + 1];
            System.arraycopy(oldArray, 0, array, 0, size);
        }
        array[size] = i;
        size++;
    }

    public final void set(int index, int value)
    {
        array[index] = value;
    }

    public final int get(int index)
    {
        return array[index];
    }

    public final int getSize()
    {
        return size;
    }

    public final int[] trim()
    {
        if (size < array.length)
        {
            int[] oldArray = array;
            array = new int[size];
            System.arraycopy(oldArray, 0, array, 0, size);            
        }
        return array;
    }
}
