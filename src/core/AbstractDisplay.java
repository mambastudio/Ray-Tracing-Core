/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.color.Color;

/**
 *
 * @author user
 */
public interface AbstractDisplay {
    
    public void imageBegin();   
    public void imageUpdate(float scale);
    public void imageFill(float x, float y, Color c);
    public void imagePaint();
    public void imageFill(Color[] c);
    
    public int getImageWidth();
    public int getImageHeight();
}
