/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
