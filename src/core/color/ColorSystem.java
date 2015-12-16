/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.color;

/**
 *
 * @author user
 */
public class ColorSystem {
   public String name;     	    	    /* Colour system name */
   public float xRed, yRed;	    	    /* Red x, y */
   public float xGreen, yGreen;  	    /* Green x, y */
   public float xBlue, yBlue;    	    /* Blue x, y */
   public Illuminant illuminant;  	    /* White point x, y */
   public float gamma;   	    	    /* Gamma correction for system */

   public ColorSystem(String name,
           float xRed, float yRed,
           float xGreen, float yGreen,
           float xBlue, float yBlue,
           Illuminant illuminant,
           float gamma)
   {
       this.name = name;
       this.xRed = xRed;
       this.yRed = yRed;
       this.xGreen = xGreen;
       this.yGreen = yGreen;
       this.xBlue = xBlue;
       this.yBlue = yBlue;
       this.illuminant = illuminant;
       this.gamma = gamma;
   }
}
