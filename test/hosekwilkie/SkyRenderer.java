/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hosekwilkie;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.rt.core.AbstractBitmap;
import org.rt.core.AbstractDisplay;
import org.rt.core.ImageSampler;
import org.rt.core.Scene;
import org.rt.core.color.Color;
import org.rt.core.color.sun.HosekWilkie;
import org.rt.core.color.sun.data.ArHosekSkyModelState;
import org.rt.core.image.formats.BitmapBGRA;
import org.rt.core.math.SphericalCoordinate;
import org.rt.thread.KernelThread;

/**
 *
 * @author user
 */
public class SkyRenderer extends KernelThread implements ImageSampler
{
    private AbstractDisplay display;
    private boolean fast;
    
    private double elevation = 0;
    private double turbidity = 1;
    private double albedo = 0;
    private double tonemap = 3.2;
    private double exposure = 0.01;
    private double sunsize = 12.9;
        
    private ArHosekSkyModelState skyState;
    private ArHosekSkyModelState sunState;
        
    @Override
    public void execute() {
        AbstractBitmap tile = null;
        
        if(display != null)
        {
            for(int i = 3; i >= 0; i--)
            {                         
                chill();
            
                //int w = display.getImageWidth();
                //int h = display.getImageHeight();

                tile = new BitmapBGRA(600, 600);
               
                int step = 1 << i;
                if(fast && i != 3) return;
                                   
                DummyClass dthread = new DummyClass(tile, 0, 0, 600, 600, step, i);
                dthread.start();
                dthread.finish();

                if(!dthread.finished) return;     
                                
                display.imageFill(tile);                
                fast = false;            
            }
        }        
        pauseKernel();
        
    }
    
    public Color[] getSky(int width, int height)
    {
        return HosekWilkie.getColor(skyState.copy(), sunState.copy(), width, height, (float)exposure, (float)tonemap);
    }
    
    
    private void reinit()
    {
        fast = true;
        resumeKernel();        
    }
    
    private void initState()
    {
        sunState = HosekWilkie.initStateRadiance(turbidity, albedo, SphericalCoordinate.elevationDegrees((float)elevation));
        skyState = HosekWilkie.initStateRGB(turbidity, albedo, SphericalCoordinate.elevationDegrees((float)elevation));
    }

    @Override
    public boolean prepare(Scene scene, int w, int h) {
        return true;
    }

    @Override
    public void render(AbstractDisplay display) {
        initState();
        
        this.display = display;
        this.display.imageBegin();
        startKernel();
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

    @Override
    public void updateDisplay() {
        display.imagePaint();
    }

    @Override
    public boolean isRunning() {
        return kernelTerminated();
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
            for(int y = startY; y < endY; y+=step)
                for(int x = startX; x < endX; x+=step)
                {
                    
                    chill();
                    if(fast && i != 3) return;
                    
                    int wi = x + step >= endX ? endX - x : step;
                    int hi = y + step >= endY ? endY - y : step;
                    
                    Color sun = HosekWilkie.getRGB_using_solar_radiance(SphericalCoordinate.sphericalDirection(x, y, w, h), sunState, exposure, tonemap);
                    Color sky = HosekWilkie.getRGB(SphericalCoordinate.sphericalDirection(x, y, w, h), skyState, exposure, tonemap);
                    float sunAlpha = Math.min(sun.getMin(), 1);
                    
                    Color col = Color.blend(sky, sun, sunAlpha);
                   
                    tile.writeColor(col, 1, x, y, wi, hi);                    
                }
            finished = true;
        }
        
        public void finish()
        {
            try {
                join();
            } catch (InterruptedException ex) {
                Logger.getLogger(SkyRenderer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
    
    
    
    public void setElevation(double elevation)
    {
        this.elevation = elevation;
        initState();
        reinit();
    }
    
    public void setAlbedo(double albedo)
    {
        this.albedo = albedo;
        initState();
        reinit();
    }
    
    public void setTurbidity(double turbidity)
    {
        this.turbidity = turbidity;
        initState();
        reinit();
    }
    
    public void setTonemap(double tonemap)
    {
        this.tonemap = tonemap;
        reinit();
    }
    
    public void setExposure(double exposure)
    {
        this.exposure = exposure;
        reinit();
    }
    
    public void setSunsize(double sunsize)
    {
        this.sunsize = sunsize;
        HosekWilkie.setSunSize(sunsize);
        reinit();
    }
}
