/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.coordinates;

/**
 *
 * @author user
 */
public class Point2i {
    public int x, y;
    
    public Point2i(){}
    public Point2i(int x, int y){this.x = x; this.y = y;}
    
    public void set(Point2f p)
    {
        x = (int) p.x;
        y = (int) p.y;
    }
    
    public void set(Point2i p)
    {
        x = p.x;
        y = p.y;
    }
       
    @Override
    public final String toString() 
    {
        return x+ " " +y;
    }
}
