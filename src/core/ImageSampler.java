/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 *
 * @author user
 */
public interface ImageSampler
{    
    
     public boolean prepare(Scene scene, int w, int h);
     public void render(AbstractDisplay display);
     public void stop();
     public void pause();
     public void resume();
}
