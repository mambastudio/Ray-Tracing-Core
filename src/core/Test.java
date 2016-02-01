/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.coordinates.Vector3f;
import core.math.FloatValue;
import static core.math.Utility.acosf;
import static core.math.Utility.asinf;
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
        Vector3f zenith = new Vector3f(0, 1, 0);
        Vector3f v = new Vector3f(0.3f, 0.5f, 0.9f).normalize();
        
        System.out.println(toDegrees(acosf(Vector3f.dot(zenith, v))));
        System.out.println(toDegrees(acosf(v.y)));
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
