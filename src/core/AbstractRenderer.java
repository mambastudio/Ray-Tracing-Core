/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.thread.KernelExecution;

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
        }
    }
    public void startRenderAndPause()
    {
        pause();        
        if(thread == null)
        {
            this.thread = new Thread(this);
            this.thread.start();            
        }        
    }
    
    public void stopRender()
    {
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
}
