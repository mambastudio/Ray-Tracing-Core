/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.thread;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public abstract class KernelThread implements Runnable
{
    boolean suspend = false;    
    boolean finish  = false;
    
    Thread thread = null;
    
    ArrayList<KernelThread> arrayList = null;
    
    public KernelThread()
    {
        thread = new Thread(this);
    }
    
    public KernelThread(ArrayList<KernelThread> arrayList)
    {
        this.arrayList = arrayList;
        this.arrayList.add(this);
    }
    
    public void startKernel()
    {
        thread.start();
    }
    
    public void joinKernel()
    {
        try {
            thread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(KernelThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void pauseKernel()
    {
        suspend = true;
    }

    public synchronized void resumeKernel()
    {
        suspend = false;
        notify();
    }
  
    public void stopKernel()
    {
        finish = true; 
        resumeKernel();        
    }
    
    public boolean kernelTerminated()
    {
        return finish;
    }
    
    //call this in an executing method in order to invoke thread state such pause & resume
    public synchronized void chill()
    {
        try
        {
            waitForNotificationToResume();
        }
        catch (InterruptedException ex)
        {
            System.err.println(ex);
        }
    }
    
    private synchronized void waitForNotificationToResume() throws InterruptedException
    {
        while(suspend)
        {           
            wait();
        }         
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            chill(); //useful in start_&_pause state of thread;
            execute();
            if(kernelTerminated()) break;
        }
        
        if(arrayList != null)
            arrayList.remove(this);
    }
    
    public abstract void execute();
    
}
