/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.render;

import core.AbstractDisplay;
import core.ImageSampler;
import core.Scene;
import core.thread.BatchThread;
import core.thread.KernelThread;

/**
 *
 * @author user
 */
public class SimpleRenderer implements ImageSampler
{
    int width, height;
    Scene scene;
    
    BatchThread threads;
    
    public SimpleRenderer()
    {
        this.threads = new BatchThread();
    }
    
    @Override
    public boolean prepare(Scene scene, int w, int h) 
    {
        this.width = w; this.height = h;
        this.scene = scene;
        
        return true;
    }

    @Override
    public void render(AbstractDisplay display) 
    {
        display.imageBegin(width, height);
        
        for(int i = 0; i<scene.getThreads(); i++)
            threads.add(new RendererThread());
        
        threads.start();
    }

    @Override
    public void stop() {
        threads.stop();
    }

    @Override
    public void pause() {
        threads.pause();
    }

    @Override
    public void resume() {
        threads.resume();
    }
    
    private class RendererThread extends KernelThread
    {

        @Override
        public void execute() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
}
