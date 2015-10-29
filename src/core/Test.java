/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.coordinates.Normal3f;
import core.coordinates.Vector3f;
import core.math.FloatValue;

/**
 *
 * @author user
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FloatValue value = new FloatValue(5);
        
        InnerClass innerClass = new InnerClass(value);
        
        System.out.println(innerClass);        
        innerClass.getValue().value = 15;
        System.out.println(innerClass);
    }
    
    
    public static class InnerClass
    {
        private final FloatValue value;
        
        public InnerClass(FloatValue value)
        {
            this.value = value;
        }
        
        @Override
        public String toString()
        {
            return value.toString();
        }
        
        public FloatValue getValue()
        {
            return value;
        }
    }    
}
