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
public class ReferencedExecution implements Runnable
{
    //Where thread is made, interface and thread class
    Runnable runnable = null;
    Thread thread = null;
    
    //Variables to control state of thread
    boolean suspend = false;    
    boolean finish  = false;
    
    ArrayList<ReferencedExecution> executionPool = null;
    
    public ReferencedExecution(ArrayList<ReferencedExecution> executionPool)
    {
        this.executionPool = executionPool;
        this.executionPool.add(this);
    }
            
    protected boolean terminated()
    {
        return finish;
    }
        
    //Call this inside method execution to make use of thread states
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
            runnable.run();
            if(terminated()) break;
        }
        executionPool.remove(this);
    }
    
    public void setRunnable(Runnable runnable)
    {
        this.runnable = runnable;
    }
    
    public void startExecution(Runnable runnable)
    {
        this.runnable = runnable;
        this.startExecution();
    }
    
    public void startExecution()
    {
        if(runnable == null) return;
        
        this.thread = new Thread(this);
        this.thread.start();
    }
    
    public void join()
    {
        try {
            thread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(ReferencedExecution.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stopExecution()
    {
        finish = true;
    }
    
    public void pauseExecution()
    {
        suspend = true;
    }

    public synchronized void resumeExecution()
    {        
        suspend = false;
        notify();
    }      
}
