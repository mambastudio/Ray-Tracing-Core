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
