/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.render;

import org.rt.core.AbstractDisplay;
import org.rt.core.ImageSampler;
import org.rt.core.Intersection;
import org.rt.core.Scene;
import org.rt.core.color.Color;
import org.rt.core.coordinates.Point2f;
import org.rt.util.FrameBuffer;
import org.rt.core.math.Ray;
import org.rt.core.math.Rng;
import org.rt.thread.BatchThread;
import org.rt.thread.KernelThread;

/**
 *
 * @author user
 */
public class SimpleRenderer implements ImageSampler
{
    int width, height;
    Scene scene;
    AbstractDisplay display;
    
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
        
        for(int i = 0; i<scene.getThreads(); i++)
            threads.add(new RendererThread(width, height));
        
        this.display = display;
        this.display.imageBegin(width, height);
        
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

    @Override
    public boolean isRunning() {
        return !threads.isThreadListEmpty();
    }
    
    private class RendererThread extends KernelThread
    {
        FrameBuffer buffer;
        
        public RendererThread(int w, int h)
        {
            buffer = new FrameBuffer(w, h);
        }
        
        @Override
        public void execute() {
            // We sample lights uniformly
            int   lightCount    = scene.lights.getSize();
            float lightPickProb = 1.f / lightCount;
                    
            buffer.incrementAccum();
            
            for(int pixID = 0; pixID < width * height; pixID++)
            {
                chill(); if(terminated()) return;
                
                int x = pixID % width;
                int y = pixID / height;
                
                Point2f sample = new Point2f((float)x, (float)y).add(Rng.getPoint2f());    
                Ray ray = scene.camera.generateRay(sample.x, sample.y, width, height);
                
                Intersection isect = new Intersection();
                if(scene.intersect(ray, isect))
                {
                    Color color = isect.bsdf.getColor();
                    buffer.add(x, y, color);
                    //System.out.println(color);
                }
                
            }
            
            Color[] colors = new Color[width * height];
            
            for(int i = 0; i<width * height; i++)
            {
                colors[i] = buffer.getScaled(i);
            }
            
            display.imageFill(colors);
            
            System.out.println(buffer.getAccum());
        }
        
    }
    
}
