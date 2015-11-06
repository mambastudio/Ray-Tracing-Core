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
public class Threading implements Runnable
{
    boolean suspend1 = false;
    boolean suspend2 = false;
    boolean finish  = false;
    
    public void pause1()
    {
        suspend1 = true;
    }

    public synchronized void resume1()
    {
        suspend1 = false;
        notify();
    }
    
    public void pause2()
    {
        suspend2 = true;
    }

    public synchronized void resume2()
    {
        suspend2 = false;
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
        while(suspend1 || suspend2)
        {           
            wait();
        }         
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            execute();
            if(terminated()) return;
        }
    }
    
    public void execute()
    {

    }
}
