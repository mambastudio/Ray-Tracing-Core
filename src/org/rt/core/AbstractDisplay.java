/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core;

import org.rt.core.color.Color;

/**
 *
 * @author user
 */
public interface AbstractDisplay {
    
    public void imageBegin(); 
    public void imageBegin(int width, int height);
    public void imageUpdate(float scale);    
    public void imagePaint();
    public void imageFill(Color[] c);
    public void imageFill(float x, float y, Color c);
    public void imageFill(AbstractBitmap bitmap);
    public void imageClear();
    
    public int getImageWidth();
    public int getImageHeight();
}
