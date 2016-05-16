/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.thread.KernelExecution1;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class PoolExecution 
{
    private final ArrayList<KernelExecution1> kernelList;
    
    public PoolExecution()
    {
        kernelList = new ArrayList<>();
    }
    
    public void add(KernelExecution1 kernel)
    {
        kernelList.add(kernel);
    }
    
    public void start()
    {
        for(KernelExecution1 execution : kernelList)
            execution.start();
    }
        
    public void stop()
    {
        for(KernelExecution1 execution : kernelList)
            execution.stop();
    }
    
    public void pause()
    {
        for(KernelExecution1 execution : kernelList)
            execution.pause();
    }
}
