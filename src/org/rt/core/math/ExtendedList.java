/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.math;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author user
 * @param <V>
 */
public class ExtendedList <V>
{
    Random rand = new Random();
    ArrayList<V> list = null;
    
    public ExtendedList()
    {
        list = new ArrayList<>();
    }
    
    public boolean contains(V v)
    {
        return list.contains(v);
    }
    
    public V getFirst()
    {
        return list.get(0);
    }
        
    public V get(int index)
    {        
        return list.get(index);
    }
    
    public void add(V v)
    {
        list.add(v);
    }
    
    public void addAll(ArrayList<V> arraylist)
    {
        list.addAll(arraylist);
    }
    
    public int size()
    {
        return list.size();
    }
    
    public boolean removeAll()
    {
        return list.removeAll(list);
    }
    
    public boolean isEmpty()
    {
        return list.isEmpty();
    }
    
    public V getRandom()
    {
        if(!list.isEmpty())
            return list.get(rand.nextInt(list.size()));
        else
            return null;
    }
    
    public ArrayList<V> getArrayList()
    {
        return list;
    }
}
