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
