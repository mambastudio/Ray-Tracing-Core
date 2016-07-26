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
public abstract class AbstractBitmap {
    protected static final float INV255 = 1.0f/255;
    
    public abstract int getWidth();
    public abstract int getHeight();
    public abstract Color readColor(int x, int y);
    public abstract float readAlpha(int x, int y);
    public abstract void writeColor(Color color, float alpha, int x, int y); 
}
