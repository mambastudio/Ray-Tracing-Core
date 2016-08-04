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
package org.rt.core;

import org.rt.thread.KernelExecution;

/**
 *
 * @author user
 */
public abstract class AbstractRenderer extends KernelExecution
{
    //Where thread is made, interface and thread class
    Runnable runnable = null;
    Thread thread = null;
    
    protected AbstractDisplay display;
    protected Scene scene;
    
    protected int   maxPathLength = 8;
    protected int   minPathLength = 0;
    protected int   iterations = 0;
    
    protected boolean inner = false;    
    protected boolean triggerClearRender = false;
    
    public AbstractRenderer(AbstractDisplay display, Scene scene)
    {
        this.display = display; this.scene = scene;
    }
    
    public abstract void render();  
        
    @Override
    public final void execute()
    {
        render();
    }
    
    public void startRender()
    {
        if(thread == null)
        {
            this.thread = new Thread(this);
            this.thread.start();
            this.scene.prepareToRender();
        }
    }
    public void startRenderAndPause()
    {
        pause();        
        if(thread == null)
        {
            this.thread = new Thread(this);
            this.thread.start(); 
            this.scene.prepareToRender();
        }        
    }
    
    public void stopRender()
    {
        resume();        
        finish();        
    }
    
    public void pauseRender()
    {
        pause();
    }
    
    public void resumeRender()
    {
        resume();
    }
    
    public void clearRender()
    {       
        display.imageClear();
        iterations = 1;        
    }
    
    public void triggerClearRender()
    {
        
    }
}
