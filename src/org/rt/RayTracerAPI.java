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
package org.rt;

import java.util.ArrayList;
import org.rt.core.AbstractDisplay;
import org.rt.core.AbstractPrimitive;
import org.rt.core.Camera;
import org.rt.core.ImageSampler;
import org.rt.core.Scene;
import org.rt.core.math.BoundingBox;

/**
 *
 * @author user
 */
public class RayTracerAPI 
{
    private Scene scene = new Scene();
    private ImageSampler renderer;
    private Camera camera;
    
    private int imageWidth = 100, imageHeight = 100;
    
    public Camera getCamera()
    {
        return camera;
    }
    
    public void setCamera(Camera camera)
    {
        this.camera = camera;
    }
    
    public void setImageSize(int imageWidth, int imageHeight)
    {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }
    
    public int getImageWidth()
    {
        return imageWidth;
    }
    
    public int getImageHeight()
    {
        return imageHeight;
    }
    
    public void setRenderer(ImageSampler renderer)
    {
        this.renderer = renderer;
    }
    
    public boolean isRendererRunning()
    {
        if(renderer == null) return false;
        else return renderer.isRunning();
    }
    
    public boolean render(AbstractDisplay display)
    {
        if(renderer == null) 
        {
            System.out.println("renderer null");
            return false;
        }
        else if(renderer.isRunning())
        {
            System.out.println("still probably running or paused or about to finish a task intensive process");
            return false;
        }
        
        this.camera.setUp();
        this.scene.setCamera(camera);
        this.scene.prepareToRender();
        this.renderer.prepare(scene, imageWidth, imageHeight);
        this.renderer.render(display);
        
        return true;
    }
    
    
    public void createScene(ArrayList<AbstractPrimitive> primitives)
    {
        scene.setPrimitives(primitives);
        scene.build();
    }
        
    public void pause()
    {
        this.renderer.pause();
    }
    
    public void stop()
    {
        this.renderer.stop();
    }
    
    public void resume()
    {
        this.renderer.resume();
    }
    
    public BoundingBox getWorldBounds()
    {
        return scene.getWorldBounds();
    }
}
