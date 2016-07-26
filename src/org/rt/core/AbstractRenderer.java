/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
