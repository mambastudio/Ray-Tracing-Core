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
public class BlackBody {
    /*                            BB_SPECTRUM
     *
     Calculate, by Planck's radiation law, the emittance of a black body
     of temperature bbTemp at the given wavelength (in metres).  */

    double bbTemp = 5000;                 /* Hidden temperature argument
                                             to BB_SPECTRUM. */

    public BlackBody()
    {

    }

    public BlackBody(float bbTemp)
    {
        this.bbTemp = bbTemp;
    }

    double planckRadiation(double wavelength)
    {
        double wlm = wavelength * 1e-9;   /* Wavelength in meters */

        return (3.74183e-16 * Math.pow(wlm, -5.0)) /
                (Math.exp(1.4388e-2 / (wlm * bbTemp)) - 1.0);
    }
}
