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
package org.rt.core.color;

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
