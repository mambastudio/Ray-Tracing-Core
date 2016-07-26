/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.thread;

/**
 *
 * @author user
 */
public class KernelExecution implements Runnable
{
    boolean suspend = false;    
    boolean finish  = false;
    
    public void pause()
    {
        suspend = true;
    }

    public synchronized void resume()
    {
        suspend = false;
        notify();
    }
  
    public void finish()
    {
        finish = true;        
    }
    
    private boolean terminated()
    {
        return finish;
    }
    
    //use this in a method in order to execute the thread state such pause & resume
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
            if(terminated()) return;
        }
    }
    
    public void execute()
    {
        
    }
}
