/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.thread.CallableOnceExecution;
import core.thread.LoopExecution;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author user
 */
public class PoolExecutor 
{
    private static ExecutorService realtimePool = null;
    private static ExecutorService progressivePool = null;
    private static ExecutorService queuePool = null;
    
    private static LoopExecution rThreading = null;
    private static LoopExecution pThreading = null;
    
    
    
    public static void init()
    {
        progressivePool = Executors.newSingleThreadExecutor();
        realtimePool    = Executors.newSingleThreadExecutor();
        queuePool = Executors.newFixedThreadPool(1);
    }
    
    public static void submitCallable(Callable callable)
    {
        queuePool.submit(new CallableOnceExecution(callable));
    }
    
    public static void submitQueue(Runnable runnable)
    {
        queuePool.submit(runnable);
    }
    
    public static void submitRealtimeRender(LoopExecution rthreading)
    {
        if(rthreading == null) return;
        rThreading = rthreading;
        realtimePool.submit(rthreading);
    }
    
    public static void submitProgressiveRender(LoopExecution pthreading)
    {
        if(pthreading == null) return;
        pThreading = pthreading;
        progressivePool.submit(pthreading);
    }
    
    public static void shutdown()
    {
        realtimePool.shutdown();
        progressivePool.shutdown();
        queuePool.shutdown();
    }
    
    public static void stopProgressiveRender()
    {
        pThreading.finish();
    }
    
    public static void pauseRealtime()
    {
        if(rThreading != null) rThreading.pause();
    }
        
    public static void resumeRealtime()
    {
        if(rThreading != null) rThreading.resume();
    }
        
    public static boolean isRealtimeNull()
    {
        return rThreading == null;
    }  
}
