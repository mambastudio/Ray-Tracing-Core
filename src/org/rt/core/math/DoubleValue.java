/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.math;

/**
 *
 * @author user
 */
public class DoubleValue {
     public DoubleValue(double value)
    {
        this.value = value;
    }
    
    public DoubleValue()
    {
        value = 0;
    }
    
    public double value;
    
    public double getValue()
    {
        return value;
    }
    
    public void setValue(double value)
    {
        this.value = value;
    }
    
    @Override
    public String toString()
    {
        return "" +value;
    }
}
