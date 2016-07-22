/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.thread;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class BatchThread 
{
    ArrayList<KernelThread> threadList;
    
    public BatchThread()
    {
        threadList = new ArrayList<>();
    }
    
    public BatchThread(KernelThread... threads)            
    {
        this();
        threadList.addAll(Arrays.asList(threads));
    }
    
    public void add(KernelThread... threads)
    {
        threadList.addAll(Arrays.asList(threads));
    }
    
    public void start()
    {
        for(KernelThread thread : threadList)
            thread.start();
    }
    
    public void pause()
    {
        for(KernelThread thread : threadList)
            thread.pause();
    }
    
    public void resume()
    {
        for(KernelThread thread : threadList)
            thread.resume();
    }
    
    public void stop()
    {
        for(KernelThread thread : threadList)
            thread.stop();
    }
    
    public void join()
    {
        for(KernelThread thread : threadList)
            thread.join();
    }
    
    public static int getCoreThreadNumber()
    {
        return Runtime.getRuntime().availableProcessors();
    }
}
