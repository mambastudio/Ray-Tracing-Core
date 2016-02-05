/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.coordinates.Vector3f;
import core.math.FloatValue;
import core.math.SphericalCoordinate;
import static core.math.Utility.INV_PI_F;
import static core.math.Utility.acosf;
import static core.math.Utility.asinf;
import static core.math.Utility.atan2f;
import static core.math.Utility.toDegrees;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class Test {

    public static void main(String[] args) 
    {
        Vector3f sunPosition = new Vector3f(0, 0.26f, -0.97f).normalize();
        //System.out.println(SphericalCoordinate.directionDegrees(75, 0));
        System.out.println(sunPosition);
    }
    
    static class FloatSort implements Comparator<Float>
    {
        @Override
        public int compare(Float o1, Float o2) {
            if(o1 < o2)
                return -1;
            else if(o1 == o2)
                return 0;
            else 
                return 1;
        }        
    }
}
