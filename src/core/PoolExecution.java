/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.thread.KernelThread;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class PoolExecution 
{
    private final ArrayList<KernelThread> kernelList;
    
    public PoolExecution()
    {
        kernelList = new ArrayList<>();
    }
    
    public void add(KernelThread kernel)
    {
        kernelList.add(kernel);
    }
    
    public void start()
    {
        for(KernelThread execution : kernelList)
            execution.start();
    }
        
    public void stop()
    {
        for(KernelThread execution : kernelList)
            execution.stop();
    }
    
    public void pause()
    {
        for(KernelThread execution : kernelList)
            execution.pause();
    }
    
    public void join()
    {
        for(KernelThread execution : kernelList)
            execution.join();
    }
}
