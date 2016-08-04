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
package org.rt.thread;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author user
 * @param <K>
 */
public class BatchThread <K extends KernelThread>
{
    ArrayList<K> threadList;
    
    public BatchThread()
    {
        threadList = new ArrayList<>();
    }
    
    public BatchThread(K... threads)            
    {
        this();
        threadList.addAll(Arrays.asList(threads));
    }
    
    public K get(int index)
    {
        if(!threadList.isEmpty())
            return threadList.get(index);
        else
            return null;
    }
    
    public void add(K... threads)
    {
        threadList.addAll(Arrays.asList(threads));
    }
    
    public void start()
    {
        for(KernelThread thread : threadList)
            thread.startKernel();
    }
    
    public void pause()
    {
        for(KernelThread thread : threadList)
            thread.pauseKernel();
    }
    
    public void resume()
    {
        for(KernelThread thread : threadList)
            thread.resumeKernel();
    }
    
    public void stop()
    {
        for(KernelThread thread : threadList)            
            thread.stopKernel();
        
        threadList.removeAll(threadList);
    }
    
    public void join()
    {
        for(KernelThread thread : threadList)
            thread.joinKernel();
    }
    
    public boolean anyThreadActive()
    {
        return !isThreadListEmpty();
    }
    
    public boolean isThreadListEmpty()
    {
        return threadList.isEmpty();
    }
    
    public static int getCoreThreadNumber()
    {
        return Runtime.getRuntime().availableProcessors();
    }
}
