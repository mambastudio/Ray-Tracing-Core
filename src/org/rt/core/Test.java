/* 
 * The MIT License
 *
 * Copyright 2016 user.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.rt.core;

import org.rt.core.coordinates.Vector3f;
import org.rt.core.math.SphericalCoordinate;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class Test {

    public static void main(String[] args) 
    {
        
        Vector3f v = SphericalCoordinate.elevationDegrees(45);
       
        System.out.println(v);
        System.out.println(SphericalCoordinate.elevationDegrees(v));
        
        /*
        HosekWilkie sky = new HosekWilkie();
        
        double solarElevation = toRadians(45);
        
        HosekWilkie.ArHosekSkyModelState skyState = sky.arhosek_rgb_skymodelstate_alloc_init(2, 0, solarElevation); //turbidity, albedo, elevation
        
        double theta = toRadians(10);
        double gamma = toRadians(80);
        
        double r = sky.arhosek_tristim_skymodel_radiance(skyState, theta, gamma, 0);
        double g = sky.arhosek_tristim_skymodel_radiance(skyState, theta, gamma, 1);
        double b = sky.arhosek_tristim_skymodel_radiance(skyState, theta, gamma, 2);
        
        System.out.println(skyState.configsString());
        System.out.println(r+ " " +g+ " " +b);
                */
        
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
