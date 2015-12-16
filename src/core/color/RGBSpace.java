/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.color;

import static core.color.CSDatabase.GAMMA_REC709;
import core.math.Utility;

/**
 *
 * @author user
 */
public class RGBSpace {
    /*                             XYZ_TO_RGB
     *
     Given an additive tricolour system CS, defined by the CIE x
     and y chromaticities of its three primaries (z is derived
     trivially as 1-(x+y)), and a desired chromaticity (XC, YC,
     ZC) in CIE space, determine the contribution of each
     primary in a linear combination which sums to the desired
     chromaticity.  If the  requested chromaticity falls outside
     the Maxwell  triangle (colour gamut) formed by the three
     primaries, one of the r, g, or b weights will be negative.
     *
     Caller can use constrain_rgb() to desaturate an
     outside-gamut colour to the closest representation within
     the available gamut and/or norm_rgb to normalise the RGB
     components so the largest nonzero component has value 1.
     *
     */

    public static XYZ convertRGBtoXYZ(Color color)
    {
        float X, Y, Z;

        float r = color.r;
        float g = color.g;
        float b = color.b;

        X = 0.5893f * r + 0.1789f * g + 0.1831f * b;
        Y = 0.2904f * r + 0.6051f * g + 0.1045f * b;
        Z = 0.0000f * r + 0.0684f * g + 1.0202f * b;

        return new XYZ(X, Y, Z);
    }

    public static Color convertXYZtoRGB(XYZ xyzColor)
    {
        float r, g, b;

        float X = xyzColor.X;
        float Y = xyzColor.Y;
        float Z = xyzColor.Z;

        r =  1.967f * X - 0.548f * Y - 0.297f * Z;
        g = -0.955f * X + 1.938f * Y - 0.027f * Z;
        b =  0.064f * X - 0.130f * Y + 0.982f * Z;

        return new Color(r, g, b);
    }

    public static void xyz_to_rgb(ColorSystem cs,
            Chromaticity cc, Color color)
    {
        double xr, yr, zr, xg, yg, zg, xb, yb, zb;
        double xw, yw, zw;
        double rx, ry, rz, gx, gy, gz, bx, by, bz;
        double rw, gw, bw;

        xr = cs.xRed;    yr = cs.yRed;    zr = 1 - (xr + yr);
        xg = cs.xGreen;  yg = cs.yGreen;  zg = 1 - (xg + yg);
        xb = cs.xBlue;   yb = cs.yBlue;   zb = 1 - (xb + yb);

        xw = cs.illuminant.xWhite;  yw = cs.illuminant.yWhite;  zw = 1 - (xw + yw);

        /* xyz -> rgb matrix, before scaling to white. */

        rx = (yg * zb) - (yb * zg);  ry = (xb * zg) - (xg * zb);  rz = (xg * yb) - (xb * yg);
        gx = (yb * zr) - (yr * zb);  gy = (xr * zb) - (xb * zr);  gz = (xb * yr) - (xr * yb);
        bx = (yr * zg) - (yg * zr);  by = (xg * zr) - (xr * zg);  bz = (xr * yg) - (xg * yr);

        /* White scaling factors.
         Dividing by yw scales the white luminance to unity, as conventional. */

        rw = ((rx * xw) + (ry * yw) + (rz * zw)) / yw;
        gw = ((gx * xw) + (gy * yw) + (gz * zw)) / yw;
        bw = ((bx * xw) + (by * yw) + (bz * zw)) / yw;

        /* xyz -> rgb matrix, correctly scaled to white. */

        rx = rx / rw;  ry = ry / rw;  rz = rz / rw;
        gx = gx / gw;  gy = gy / gw;  gz = gz / gw;
        bx = bx / bw;  by = by / bw;  bz = bz / bw;

        /* rgb of the desired point */

        color.r = (float) ((rx * cc.x) + (ry * cc.y) + (rz * cc.z));
        color.g = (float) ((gx * cc.x) + (gy * cc.y) + (gz * cc.z));
        color.b = (float) ((bx * cc.x) + (by * cc.y) + (bz * cc.z));
        
    }

    /*                            INSIDE_GAMUT
     *
     Test whether a requested colour is within the gamut
     achievable with the primaries of the current colour
     system.  This amounts simply to testing whether all the
     primary weights are non-negative.
     *
     */

    public static boolean inside_gamut(double r, double g, double b)
    {
        return (r >= 0) && (g >= 0) && (b >= 0);
    }

    /*                          CONSTRAIN_RGB
     *
     If the requested RGB shade contains a negative weight for
     one of the primaries, it lies outside the colour gamut
     accessible from the given triple of primaries.  Desaturate
     it by adding white, equal quantities of R, G, and B, enough
     to make RGB all positive.  The function returns 1 if the
     components were modified, zero otherwise.
     *
     */

    public static boolean constrain_rgb(Color c)
    {
        float w;

        /* Amount of white needed is w = - min(0, *r, *g, *b) */

        w = (0 < c.r) ? 0 : c.r;
        w = (w < c.g) ? w : c.g;
        w = (w < c.b) ? w : c.b;
        w = -w;

        /* Add just enough white to make r, g, b all positive. */

        if (w > 0)
        {
            c.r += w;  c.g += w; c.b += w;
            return true;                     /* Colour modified to fit RGB gamut */
        }

        return false;                        /* Colour within RGB gamut */
    }

    /*                          GAMMA_CORRECT_RGB
     *
     Transform linear RGB values to nonlinear RGB values. Rec.
     709 is ITU-R Recommendation BT. 709 (1990) ``Basic
     Parameter Values for the HDTV Standard for the Studio and
     for International Programme Exchange'', formerly CCIR Rec.
     709. For details see
     *
     http://www.poynton.com/ColorFAQ.html
     http://www.poynton.com/GammaFAQ.html
     */

    public static float gamma_correct(ColorSystem cs, float c)
    {
        float gamma;

        gamma = (float) cs.gamma;

        if (gamma == GAMMA_REC709)
        {
            /* Rec. 709 gamma correction. */
            double cc = 0.018;

            if (c < cc)
            {
                c *= ((1.099 * Math.pow(cc, 0.45)) - 0.099) / cc;
            }
            else
            {
                c = (float) ((1.099 * Math.pow(c, 0.45)) - 0.099);
            }
        }
        else
        {
            /* Nonlinear colour = (Linear colour)^(1/gamma) */
            c = (float) Math.pow(c, 1.0 / gamma);
        }
        return c;
    }

    public static void gamma_correct_rgb(ColorSystem cs, Color c)
    {
        c.r = gamma_correct(cs, c.r);
        c.g = gamma_correct(cs, c.g);
        c.b = gamma_correct(cs, c.b);
    }

    /*  	    	    	    NORM_RGB
     *
     Normalise RGB components so the most intense (unless all
     are zero) has a value of 1.
     *
     */

    public static void norm_rgb(Color c)
    {
        float greatest = Utility.max(c.r, c.g, c.b);

        if (greatest > 0)
        {
            c.r /= greatest;
            c.g /= greatest;
            c.b /= greatest;
        }
    }

    public static void main(String [] args)
    {
        float t;
        ColorSystem cs = CSDatabase.SMPTEsystem;

        System.out.println("Temperature       x      y      z       R     G     B\n");
        System.out.println("-----------    ------ ------ ------   ----- ----- -----\n");

        for (t = 1000; t <= 10000; t+= 500)
        {
            Chromaticity chromaticity = new Chromaticity();
            Color c = new Color();

            SpectralCurve.spectrum_to_xyz(new BlackBody(t), chromaticity);
            xyz_to_rgb(cs, chromaticity, c);
            System.out.format("  %5.0f, K      %.4f, %.4f, %.4f,   ", t, chromaticity.x, chromaticity.y, chromaticity.z);
            if (constrain_rgb(c))
            {
                norm_rgb(c);
                System.out.format("%.3f %.3f %.3f (Approximation)\n", c.r, c.g, c.b);
            }
            else
            {
                norm_rgb(c);
                System.out.format("%.3f %.3f %.3f\n", c.r, c.g, c.b);
            }
        }        
    }
}
