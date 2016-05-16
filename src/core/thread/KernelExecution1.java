/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.thread;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public abstract class KernelExecution1 implements Runnable
{
    boolean suspend = false;    
    boolean finish  = false;
    
    Thread thread = null;
    
    ArrayList<KernelExecution1> arrayList = null;
    
    public KernelExecution1()
    {
        thread = new Thread(this);
    }
    
    public KernelExecution1(ArrayList<KernelExecution1> arrayList)
    {
        this.arrayList = arrayList;
        this.arrayList.add(this);
    }
    
    public void start()
    {
        thread.start();
    }
    
    public void join()
    {
        try {
            thread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(KernelExecution1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void pause()
    {
        suspend = true;
    }

    public synchronized void resume()
    {
        suspend = false;
        notify();
    }
  
    public void stop()
    {
        finish = true;  
        join();
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
            if(terminated()) break;
        }
        
        if(arrayList != null)
            arrayList.remove(this);
    }
    
    public abstract void execute();
    
}
