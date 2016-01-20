/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.thread.LoopExecution;
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
    
    private static LoopExecution pThreading = null;
    
    
    
    public static void init()
    {
        progressivePool = Executors.newSingleThreadExecutor();
        
        queuePool = Executors.newFixedThreadPool(1);
    }
    
    public static void submitQueue(Runnable runnable)
    {
        queuePool.submit(runnable);
    }
    
    public static void submitProgressiveRender(LoopExecution pthreading)
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
}
