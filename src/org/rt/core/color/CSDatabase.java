/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.color;

/**
 *
 * @author user
 */
public class CSDatabase {
    static final float  GAMMA_REC709 = 0;
    
    /* White point chromaticities.*/
    public static final Illuminant illuminantC = new Illuminant("IllumiantC", 0.3101f, 03162f);
    public static final Illuminant illuminantD65 = new Illuminant("IllumiantD65", 0.3127f, 0.3291f);
    public static final Illuminant illuminantE = new Illuminant("IllumiantE", 0.33333333f, 0.33333333f);

    public static ColorSystem NTSCsystem  =  new ColorSystem( "NTSC",               0.67f,   0.33f,   0.21f,   0.71f,   0.14f,   0.08f,   illuminantC,    GAMMA_REC709 );
    public static ColorSystem EBUsystem   =  new ColorSystem( "EBU (PAL/SECAM)",    0.64f,   0.33f,   0.29f,   0.60f,   0.15f,   0.06f,   illuminantD65,  GAMMA_REC709 );
    public static ColorSystem SMPTEsystem =  new ColorSystem( "SMPTE",              0.630f,  0.340f,  0.310f,  0.595f,  0.155f,  0.070f,  illuminantD65,  GAMMA_REC709 );
    public static ColorSystem HDTVsystem  =  new ColorSystem( "HDTV",               0.670f,  0.330f,  0.210f,  0.710f,  0.150f,  0.060f,  illuminantD65,  GAMMA_REC709 );
    public static ColorSystem CIEsystem   =  new ColorSystem( "CIE",                0.7355f, 0.2645f, 0.2658f, 0.7243f, 0.1669f, 0.0085f, illuminantE,    GAMMA_REC709 );
    public static ColorSystem Rec709system = new ColorSystem( "CIE REC 709",        0.64f,   0.33f,   0.30f,   0.60f,   0.15f,   0.06f,   illuminantD65,  GAMMA_REC709 );
}
