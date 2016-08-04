/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
