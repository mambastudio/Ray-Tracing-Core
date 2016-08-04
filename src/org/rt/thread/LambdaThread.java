/* 
 * The MIT License
 *
 * Copyright 2016 user.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.rt.thread;

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
public class LambdaThread implements Runnable
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
    
    public static void executeThread(Runnable runnable)
    {
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
