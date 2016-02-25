/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.thread.KernelExecution;
import core.color.Color;

/**
 *
 * @author user
 */
public abstract class RenderKernel extends KernelExecution
{
    protected AbstractDisplay display;
    protected Scene scene;
    
    public RenderKernel(Scene scene, AbstractDisplay display)
    {
        this.display = display;
        this.scene = scene;
    }
            
    public abstract void pauseRender();
    public abstract void reinitRender();    
    public abstract void initRender(); 
    public abstract void stopRender();
    
    @Override
    public abstract void execute();
    
    public Color brdfLightSampling(Intersection isect)
    {
        return null;
    }
    
    public Color directLightSampling(Intersection isect)
    {
        return null;
    }
}


