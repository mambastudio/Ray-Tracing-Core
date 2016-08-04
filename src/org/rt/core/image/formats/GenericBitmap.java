/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.image.formats;

import javafx.scene.image.Image;
import org.rt.core.AbstractBitmap;
import org.rt.core.color.Color;

/**
 *
 * @author user
 */
public class GenericBitmap extends AbstractBitmap
{
    private final int w, h;
    private final Color[] color;
    private final float[] alpha;
    
    public GenericBitmap(int w, int h)
    {
        this.w = w; this.h = h; 
        color = new Color[w * h];
        alpha = new float[w * h];
    }

    @Override
    public int getWidth() {
        return w;
    }

    @Override
    public int getHeight() {
        return h;
    }

    @Override
    public Color readColor(int x, int y) {
        return color[x + y * w];
    }

    @Override
    public float readAlpha(int x, int y) {
        return alpha[x + y * w];
    }

    @Override
    public void writeColor(Color color, float alpha, int x, int y) 
    {
        this.color[x + y * w] = color;
        this.alpha[x + y * w] = alpha;
    }    

    @Override
    public Image getImage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void writeColor(Color color, float alpha, int x, int y, int w, int h) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
