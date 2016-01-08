/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.thread;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class CallableOnceExecution implements Runnable
{
    boolean suspend = false;    
    boolean finish  = false;
    
    Callable callable = null;
    
    public CallableOnceExecution(Callable callable)
    {
        this.callable = callable;
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
  
    public void finish()
    {
        finish = true;
    }
    
    protected boolean terminated()
    {
        return finish;
    }
    
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
        try {
            callable.call();
        } catch (Exception ex) {
            Logger.getLogger(CallableOnceExecution.class.getName()).log(Level.SEVERE, null, ex);
        }
        finish();
    }    
}
