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
