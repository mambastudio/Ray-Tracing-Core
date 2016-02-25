/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.thread;

/**
 *
 * @author user
 * 
 * This class is for thread implementation, and it's main focus is to utilize 
 * java lambda feature. The class design to be standalone and hence does not require
 * a thread class or execution service to invoke
 * 
 * The method execution is called in loop unless terminated by invoking 'finish'
 * variable
 * 
 */
public class LambdaExecution implements Runnable
{
    //Where thread is made, interface and thread class
    Runnable runnable = null;
    Thread thread = null;
    
    //Variables to control state of thread
    boolean suspend = false;    
    boolean finish  = false;
            
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
            if(terminated()) return;
        }
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
