/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.coordinates;

/**
 *
 * @author user
 */
public class Point2f {
    public float x, y;
    
    public Point2f(){}
    public Point2f(float x, float y){this.x = x; this.y = y;}
    
    public Point2f add(Point2f a) {return new Point2f(x + a.x, y + a.y);}
    
    @Override
    public final String toString() 
    {
        return String.format("(%.2f, %.2f)", x, y);
    }
}
