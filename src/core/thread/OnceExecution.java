/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.thread;

/**
 *
 * @author user
 */
public class OnceExecution implements Runnable
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
        execute();
        finish();
    }
    
    public void execute()
    {
        
    }
}
