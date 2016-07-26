/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.math;

import org.rt.core.coordinates.Vector3f;
import static org.rt.core.math.Utility.toRadians;

/**
 *
 * @author user
 */
public class SunCoordinates 
{
    public float solarZenith;
    
    public SunCoordinates(float position_degrees)
    {
        if(position_degrees > 90)
            solarZenith = toRadians(90);
        else if(position_degrees < 0)
            solarZenith = toRadians(0);
        else
            solarZenith = toRadians(position_degrees);
    }
    
    public SunCoordinates(Vector3f direction)
    {
        
    }
}
