/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
