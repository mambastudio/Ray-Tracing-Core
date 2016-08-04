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
    
    BatchThread<RendererThread> renderThreads;
    
    public SimpleRenderer()
    {
        this.renderThreads = new BatchThread();        
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
            renderThreads.add(new RendererThread(width, height));
        
        this.display = display;
        this.display.imageBegin(width, height);
        
        renderThreads.start();        
    }
    
    @Override
    public void updateDisplay()
    {
        renderThreads.get(0).getBuffer().scaleBuffer();            
        Color[] colors = renderThreads.get(0).getBuffer().getScaledColorArray();
                        
        display.imageFill(colors);
        display.imagePaint();         
    }

    @Override
    public void stop() {
        renderThreads.stop();       
    }

    @Override
    public void pause() {
        renderThreads.pause();       
    }

    @Override
    public void resume() {
        renderThreads.resume();        
    }

    @Override
    public boolean isRunning() {
        return !renderThreads.isThreadListEmpty();
    }
    
    private class RendererThread extends KernelThread
    {
        FrameBuffer buffer;
        
        public RendererThread(int w, int h)
        {
            buffer = new FrameBuffer(w, h);            
        }
        
        public FrameBuffer getBuffer()
        {
            return buffer;
        }
        
        @Override
        public void execute() {
            // We sample lights uniformly
            int   lightCount    = scene.lights.getSize();
            float lightPickProb = 1.f / lightCount;
                    
            for(int pixID = 0; pixID < width * height; pixID++)
            {
                chill(); if(kernelTerminated()) return;
                
                int x = pixID % width;
                int y = pixID / height;
                
                Point2f sample = new Point2f((float)x, (float)y).add(Rng.getPoint2f());    
                Ray ray = scene.camera.generateRay(sample.x, sample.y, width, height);
                
                Intersection isect = new Intersection();
                if(scene.intersect(ray, isect))
                {
                    
                    Color color = scene.directLightSampling(isect, null);
                    buffer.add(x, y, color);
                    //System.out.println(color);
                }
                
            }
            System.out.println("iteration " +buffer.getAccum());
            
            buffer.incrementAccum();
            
        }
        
    }
    
}
