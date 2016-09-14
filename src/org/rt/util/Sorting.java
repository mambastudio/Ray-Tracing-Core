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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.rt.core.AbstractPrimitive;

/**
 *
 * @author user
 */
public class Sorting {
    
    public static void sort(ArrayList<? extends AbstractPrimitive> list, int start, int end, int dim)
    {
        list.subList(start, end).sort((o1, o2) ->{
            if(o1.getWorldBounds().getCenter().get(dim) < o2.getWorldBounds().getCenter().get(dim))
                return -1;
            else if(o1.getWorldBounds().getCenter().get(dim) == o2.getWorldBounds().getCenter().get(dim))
                return 0;
            else 
                return 1;
        });        
    }  
    
    public static int partition(ArrayList<?extends AbstractPrimitive> list, int start, int end, int splitDimension, float splitCoord)
    {
        int mid = start;
        for(int i = start; i < end; i++)
        {
            if(list.get(i).getWorldBounds().getCenter(splitDimension) < splitCoord)
            {
                Collections.swap(list, i, mid);
                mid++;
            }
        }
        return mid;
    }
    
    public static void swap(ArrayList list1, ArrayList list2)
    {
        ArrayList<?> tmpList = new ArrayList<>(list1);
        list1.clear();
        list1.addAll(list2);
        list2.clear();
        list2.addAll(tmpList);
    }
}
