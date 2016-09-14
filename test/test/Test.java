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
package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 *
 * @author user
 */
public class Test {
    public static void main(String... args)
    {
        Integer[] array = new Integer[]{4, 9, 3, 6, 2, 5, 0, 7, 1, 8};
        ArrayList<Integer> arrayList = new ArrayList<>(Arrays.asList(array));
        System.out.println(arrayList.stream().map(Object::toString).collect(Collectors.joining(", ")));
        sort(arrayList);
        System.out.println(arrayList.stream().map(Object::toString).collect(Collectors.joining(", ")));
    }
    
    public static void sort(ArrayList<Integer> arrayList)
    {
        int start = 0;
        int end = arrayList.size();
        int split = 6;
        
        int mid = start;
        for(int i = start; i < end; i++)
        {
            if(arrayList.get(i) < split)
            {
                Collections.swap(arrayList, i, mid);
                mid++;
            }
        }
        
        System.out.println(mid);
    }
}
