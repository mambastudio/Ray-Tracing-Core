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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author user
 */
public class TimerExecution 
{
    ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    
    private final long initialDelay, period;
    private final TimeUnit timeUnit;
    
    private final long cycles; // number of iterations on execution
    private final boolean afterCyclesShutdown;
    
    private ScheduledFuture future;
    private FutureRunnable fRunnable;
        
    public TimerExecution(long initialDelay, long period, TimeUnit timeUnit)
    {
        this.initialDelay = initialDelay;
        this.period = period;
        this.timeUnit = timeUnit;
        this.cycles = -1;
        this.afterCyclesShutdown = false;
    }
    
    public TimerExecution(long initialDelay, long period, long cycles, TimeUnit timeUnit)
    {
        this.initialDelay = initialDelay;
        this.period = period;
        this.timeUnit = timeUnit;
        this.cycles = cycles;
        this.afterCyclesShutdown = false;
    }
    
    public TimerExecution(long initialDelay, long period, long cycles, boolean afterCyclesShutdown, TimeUnit timeUnit)
    {
        this.initialDelay = initialDelay;
        this.period = period;
        this.timeUnit = timeUnit;
        this.cycles = cycles;
        this.afterCyclesShutdown = afterCyclesShutdown;
    }
    
    public void execute(Runnable runnable)
    {
        fRunnable = new FutureRunnable(runnable);
        future = scheduledExecutorService.scheduleWithFixedDelay(fRunnable, initialDelay, period, timeUnit);        
    }
    
    public void cancel()
    {
        future.cancel(false);
    }
    
    public boolean isCancelled()
    {
        return future.isCancelled();
    }
    
    public boolean isDone()
    {
        return future.isDone();
    }
    
    public void pause()
    {
        fRunnable.pause();
    }
    
    public void resume()
    {
        fRunnable.resume();
    }
    
    public void shutDown()
    {
        resume();
        scheduledExecutorService.shutdownNow();        
    }
    
    public int getNumberOfExecutions()
    {
        return fRunnable.getNumberOfExecutions();
    }
    
    private class FutureRunnable implements Runnable
    {
        Runnable runnable;
        int numberOfExecutions;        
        boolean suspend = false;    
        
        FutureRunnable(Runnable runnable)
        {
            this.runnable = runnable;
        }

        @Override
        public void run() {
            chill();
            runnable.run();
            numberOfExecutions++;
            chill();
            
            if(cycles > 0)
                if(numberOfExecutions >= cycles && afterCyclesShutdown)
                {
                    cancel();
                    shutDown();
                }
                else if(numberOfExecutions >= cycles)
                {
                    cancel();                    
                }
        }
        
        public int getNumberOfExecutions()
        {
            return numberOfExecutions;
        }
        
        private void pause()
        {
            suspend = true;
        }

        private synchronized void resume()
        {
            suspend = false;
            notify();
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
    }
}
