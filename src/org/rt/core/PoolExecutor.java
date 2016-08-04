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
package org.rt.core;

import org.rt.thread.KernelExecution;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author user
 */
public class PoolExecutor 
{   
    private static ExecutorService progressivePool = null;
    private static ExecutorService queuePool = null;
    
    private static KernelExecution pThreading = null;
    
    
    
    public static void init()
    {
        progressivePool = Executors.newSingleThreadExecutor();
        
        queuePool = Executors.newFixedThreadPool(1);
    }
    
    public static void submitQueue(Runnable runnable)
    {
        queuePool.submit(runnable);
    }
    
    public static void submitProgressiveRender(KernelExecution pthreading)
    {
        if(pthreading == null) return;
        pThreading = pthreading;
        progressivePool.submit(pthreading);
    }
    
    public static void shutdown()
    {        
        progressivePool.shutdown();
        queuePool.shutdown();
    }
    
    public static void stopProgressiveRender()
    {
        pThreading.finish();
    }    
    
    //Useful for executing small processes in separate thread, once submitted
    //user has no control over it, hence care must be taken care of in terms
    //of executing resouce consuming and long tasks
    public static void executeThread(Runnable runnable)
    {
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
