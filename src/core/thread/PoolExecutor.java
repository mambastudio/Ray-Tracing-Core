/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.thread;

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
    
    private static Threading rThreading = null;
    private static Threading pThreading = null;
    
    
    
    public static void init()
    {
        progressivePool = Executors.newSingleThreadExecutor();
        realtimePool    = Executors.newSingleThreadExecutor();
        queuePool = Executors.newFixedThreadPool(1);
    }
    
    public static void submitQueue(Runnable runnable)
    {
        queuePool.submit(runnable);
    }
    
    public static void submitRealtimeRender(Threading rthreading)
    {
        if(rthreading == null) return;
        rThreading = rthreading;
        realtimePool.submit(rthreading);
    }
    
    public static void submitProgressiveRender(Threading pthreading)
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
    
    public static void pause1Realtime()
    {
        if(rThreading != null) rThreading.pause1();
    }
    
    public static void pause2Realtime()
    {
        if(rThreading != null) rThreading.pause2();
    }
    
    public static void resume1Realtime()
    {
        if(rThreading != null) rThreading.resume1();
    }
    
    public static void resume2Realtime()
    {
        if(rThreading != null) rThreading.resume2();
    }
    
    public static boolean isRealtimeNull()
    {
        return rThreading == null;
    }  
}
