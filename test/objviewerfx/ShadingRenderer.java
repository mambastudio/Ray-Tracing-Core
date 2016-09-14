/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objviewerfx;

import org.rt.display.DynamicDisplay;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.rt.core.AbstractBitmap;
import org.rt.core.AbstractDisplay;
import org.rt.core.Camera;
import org.rt.core.ImageSampler;
import org.rt.core.Intersection;
import org.rt.core.Scene;
import org.rt.core.color.Color;
import org.rt.core.coordinates.Vector3f;
import org.rt.core.image.formats.BitmapRGB;
import org.rt.core.math.Orientation;
import org.rt.core.math.Ray;
import org.rt.core.math.Utility;
import org.rt.thread.KernelThread;

/**
 *
 * @author user
 */
public class ShadingRenderer extends KernelThread implements ImageSampler
{
    DynamicDisplay display;
    Scene scene;
    boolean fast;
    
    
    @Override
    public boolean prepare(Scene scene, int w, int h) {
        this.scene = scene;
        return true;
    }

    @Override
    public void render(AbstractDisplay display)
    {
        if(display instanceof DynamicDisplay)
            this.display = (DynamicDisplay) display;
        else
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        this.display.viewportW.addListener((observable, old_value, new_value) -> {
            reinit();
        });
        
        this.display.viewportH.addListener((observable, old_value, new_value) -> {
            reinit();
        });
        
        this.display.translationDepth.addListener((observable, old_value, new_value) -> {
             Orientation.translateDistance(scene.getCamera(), new_value.floatValue());
             reinit();
        });
        
        this.display.translationXY.addListener((observable, old_value, new_value) -> {
            Orientation.rotateX(scene.camera, new_value.x);
            Orientation.rotateY(scene.camera, new_value.y);
            reinit();
        });
        
        startKernel();
    }
    
    public void reinit()
    {
        fast = true;
        resumeKernel();        
    }
    
    @Override
    public void updateDisplay() {
        display.imagePaint();
    }

    @Override
    public boolean isRunning() {
        return kernelTerminated();
    }

    @Override
    public void execute() {
        AbstractBitmap tile = null;
        
        if(display != null)
        {
            for(int i = 3; i >= 0; i--)
            {         
                chill();
            
                int w = display.getImageWidth();
                int h = display.getImageHeight();

                tile = new BitmapRGB(w, h);
               
                int step = 1 << i;
                if(fast && i != 3) return;
                                   
                DummyClass dthread = new DummyClass(tile, 0, 0, w, h, step, i);
                dthread.start();
                dthread.finish();

                if(!dthread.finished) return;      
                
                display.imageFill(tile);                
                fast = false;            
            }
        }        
        pauseKernel();
        
    }

    @Override
    public void stop() {
        stopKernel();
    }

    @Override
    public void pause() {
        pauseKernel();
    }

    @Override
    public void resume() {
        resumeKernel();
    }
    
    
    private Color doSomething(Camera camera, int x, int y, int w, int h)
    {
        Ray ray = camera.getFastRay(x, y, w, h);                    
        Intersection isect = new Intersection();
        boolean hit = scene.intersect(ray, isect);
                    
        Color col;
        if(hit)
        {
            if(isect.isEmitter())
            {                            
                col = evaluateColor(isect.primitive.getMaterial().getPoweredEmission());                            
            }
            else
            { 
                float coeff = Math.abs(Vector3f.dot(ray.d, isect.dg.n));
                coeff = Utility.clamp(coeff, 0.0F, 1.0F);
                col = evaluateColor(isect.bsdf.getColor(), coeff);  
            }
        }
        else
            col = Color.BLACK;
        
        return col;
    }
    
    private Color evaluateColor(Color color)
    {
        float r = Utility.clamp(color.r, 0.0F, 1.0F);
        float g = Utility.clamp(color.g, 0.0F, 1.0F);
        float b = Utility.clamp(color.b, 0.0F, 1.0F);
            
        return new Color(r, g, b);
    }
        
    private Color evaluateColor(Color color, float coeff)
    {
        float r = Utility.clamp(color.r * coeff, 0.0F, 1.0F);
        float g = Utility.clamp(color.g * coeff, 0.0F, 1.0F);
        float b = Utility.clamp(color.b * coeff, 0.0F, 1.0F);
            
        return new Color(r, g, b);
    }
    
    public class DummyClass extends Thread
    {
        AbstractBitmap tile;      
        
        int i, w, h, step;
        int startX, startY;
        int endX, endY;
        
        boolean finished = false;
        
        public DummyClass(AbstractBitmap tile, int startX, int startY, int endX, int endY, int step, int i)
        {
            this.tile = tile;                  
            this.w = tile.getWidth(); this.h = tile.getHeight();
            this.step = step; this.i = i;
            this.startX = startX; this.startY = startY;
            this.endX = endX; this.endY = endY;
        }
        
        @Override
        public void run()
        {
            Camera camera = scene.camera.copy();
            
            for(int y = startY; y < endY; y+=step)
                for(int x = startX; x < endX; x+=step)
                {
                    
                    chill();
                    if(fast && i != 3) return;
                    
                    int wi = x + step >= endX ? endX - x : step;
                    int hi = y + step >= endY ? endY - y : step;
                    
                    Color col = doSomething(camera, x, y, w, h);
                    
                    tile.writeColor(col, 1, x, y, wi, hi);                    
                }
            finished = true;
        }
        
        public void finish()
        {
            try {
                join();
            } catch (InterruptedException ex) {
                Logger.getLogger(ShadingRenderer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
