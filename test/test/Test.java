/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.Arrays;
import org.rt.core.color.Color;

/**
 *
 * @author user
 */
public class Test {
    public static void main(String... args)
    {
        Color color = new Color(.5f, .1f, .2f);
        System.out.println(color);
        byte[] buf = Color.toByte(color.r, color.g, color.b);
        float[] fbuf = Color.toFloat8(buf[0], buf[1], buf[2]);
        System.out.println(Arrays.toString(fbuf));
    }
    
   
}
