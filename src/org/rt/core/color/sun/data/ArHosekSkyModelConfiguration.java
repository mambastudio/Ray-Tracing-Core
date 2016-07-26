/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.color.sun.data;

import java.util.Arrays;

/**
 *
 * @author user
 */
public class ArHosekSkyModelConfiguration {
    public double [] values = new double[9];
        
    @Override
    public String toString()
    {
        return Arrays.toString(values);
    }
}
