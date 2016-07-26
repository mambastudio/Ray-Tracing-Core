/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.math;

import org.rt.core.coordinates.Point2f;
import org.rt.core.coordinates.Point3f;

/**
 *
 * @author user
 */
public class Rng 
{    
    public static float getFloat()
    {
        return (float)Math.random();
    }
    
    public static boolean isRndBelow(double value)
    {
        if(getFloat()<value)
            return true;
        else
            return false;
    }
    
    public static Point2f getPoint2f()
    {
        float a = getFloat();
        float b = getFloat();

        return new Point2f(a, b);
    }

    public static Point3f getPoint3f()
    {
        float a = getFloat();
        float b = getFloat();
        float c = getFloat();

        return new Point3f(a, b, c);
    }
}
