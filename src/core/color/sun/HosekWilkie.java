/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.color.sun;

import core.color.Color;
import core.color.sun.data.ArHosekSkyModelConfiguration;
import static core.color.sun.data.ArHosekSkyModelData_CIEXYZ.datasetsXYZ;
import static core.color.sun.data.ArHosekSkyModelData_CIEXYZ.datasetsXYZRad;
import static core.color.sun.data.ArHosekSkyModelData_RGB.datasetsRGB;
import static core.color.sun.data.ArHosekSkyModelData_RGB.datasetsRGBRad;
import core.color.sun.data.ArHosekSkyModelState;
import core.coordinates.Vector3f;
import core.math.SphericalCoordinate;
import static java.lang.Math.cos;
import static java.lang.Math.exp;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.Arrays;



/**
 *
 * @author user
 */

public final class HosekWilkie 
{
    
    int NIL                         = 0;
    double MATH_PI                   = 3.141592653589793;
    double MATH_DEG_TO_RAD           = ( MATH_PI / 180.0 );
    double MATH_RAD_TO_DEG           = ( 180.0 / MATH_PI );
    double DEGREES                   = MATH_DEG_TO_RAD;
    double TERRESTRIAL_SOLAR_RADIUS  = ( ( 0.51 * DEGREES ) / 2.0 );

    // internal functions
    
    Vector3f sunPosition = SphericalCoordinate.elevationDegrees(0);
    
    float turbidity = 1;
    float albedo = 0;
    float exposure = 0.01f;
    float tonemapGamma = 3.2f;
        
    ArHosekSkyModelState currentState = new ArHosekSkyModelState();
    
    public HosekWilkie()
    {
        init();
    }

    public void ArHosekSkyModel_CookConfiguration(
        double[]                         dataset,
        ArHosekSkyModelConfiguration  config, 
        double                         turbidity, 
        double                         albedo, 
        double                    solar_elevation
        )
    {
        double[]  elev_matrix;

        int     int_turbidity = (int)turbidity;
        double  turbidity_rem = turbidity - (double)int_turbidity;

        solar_elevation = pow(solar_elevation / (MATH_PI / 2.0), (1.0 / 3.0));
        
        // alb 0 low turb

        elev_matrix = Arrays.copyOfRange(dataset, 9 * 6 * (int_turbidity-1), dataset.length);
        
        for(int i = 0; i < 9; ++i )
        {
            //(1-t).^3* A1 + 3*(1-t).^2.*t * A2 + 3*(1-t) .* t .^ 2 * A3 + t.^3 * A4;
            config.values[i] = 
                    (1.0-albedo) * (1.0 - turbidity_rem) 
                    * ( pow(1.0-solar_elevation, 5.0) * elev_matrix[i]  + 
                    5.0  * pow(1.0-solar_elevation, 4.0) * solar_elevation * elev_matrix[i+9] +
                    10.0*pow(1.0-solar_elevation, 3.0)*pow(solar_elevation, 2.0) * elev_matrix[i+18] +
                    10.0*pow(1.0-solar_elevation, 2.0)*pow(solar_elevation, 3.0) * elev_matrix[i+27] +
                    5.0*(1.0-solar_elevation)*pow(solar_elevation, 4.0) * elev_matrix[i+36] +
                    pow(solar_elevation, 5.0)  * elev_matrix[i+45]);
        }
        
        // alb 1 low turb
        elev_matrix = Arrays.copyOfRange(dataset, 9*6*10 + 9*6*(int_turbidity-1), dataset.length);
        for(int i = 0; i < 9; ++i)
        {
            //(1-t).^3* A1 + 3*(1-t).^2.*t * A2 + 3*(1-t) .* t .^ 2 * A3 + t.^3 * A4;
            config.values[i] += 
                (albedo) * (1.0 - turbidity_rem)
                * ( pow(1.0-solar_elevation, 5.0) * elev_matrix[i]  + 
                5.0  * pow(1.0-solar_elevation, 4.0) * solar_elevation * elev_matrix[i+9] +
                10.0*pow(1.0-solar_elevation, 3.0)*pow(solar_elevation, 2.0) * elev_matrix[i+18] +
                10.0*pow(1.0-solar_elevation, 2.0)*pow(solar_elevation, 3.0) * elev_matrix[i+27] +
                5.0*(1.0-solar_elevation)*pow(solar_elevation, 4.0) * elev_matrix[i+36] +
                pow(solar_elevation, 5.0)  * elev_matrix[i+45]);
        }
        
        if(int_turbidity == 10)
            return;

        // alb 0 high turb
        elev_matrix = Arrays.copyOfRange(dataset, (9*6*(int_turbidity)), dataset.length);
        
        for(int i = 0; i < 9; ++i)
        {
            //(1-t).^3* A1 + 3*(1-t).^2.*t * A2 + 3*(1-t) .* t .^ 2 * A3 + t.^3 * A4;
            config.values[i] += 
                (1.0-albedo) * (turbidity_rem)
                * ( pow(1.0-solar_elevation, 5.0) * elev_matrix[i]  + 
                5.0  * pow(1.0-solar_elevation, 4.0) * solar_elevation * elev_matrix[i+9] +
                10.0*pow(1.0-solar_elevation, 3.0)*pow(solar_elevation, 2.0) * elev_matrix[i+18] +
                10.0*pow(1.0-solar_elevation, 2.0)*pow(solar_elevation, 3.0) * elev_matrix[i+27] +
                5.0*(1.0-solar_elevation)*pow(solar_elevation, 4.0) * elev_matrix[i+36] +
                pow(solar_elevation, 5.0)  * elev_matrix[i+45]);
        }

        // alb 1 high turb
        elev_matrix = Arrays.copyOfRange(dataset, (9*6*10 + 9*6*(int_turbidity)), dataset.length);
        for(int i = 0; i < 9; ++i)
        {
            //(1-t).^3* A1 + 3*(1-t).^2.*t * A2 + 3*(1-t) .* t .^ 2 * A3 + t.^3 * A4;
            config.values[i] += 
                (albedo) * (turbidity_rem)
                * ( pow(1.0-solar_elevation, 5.0) * elev_matrix[i]  + 
                5.0  * pow(1.0-solar_elevation, 4.0) * solar_elevation * elev_matrix[i+9] +
                10.0*pow(1.0-solar_elevation, 3.0)*pow(solar_elevation, 2.0) * elev_matrix[i+18] +
                10.0*pow(1.0-solar_elevation, 2.0)*pow(solar_elevation, 3.0) * elev_matrix[i+27] +
                5.0*(1.0-solar_elevation)*pow(solar_elevation, 4.0) * elev_matrix[i+36] +
                pow(solar_elevation, 5.0)  * elev_matrix[i+45]);
        }
    }
    
    double ArHosekSkyModel_CookRadianceConfiguration(
        double[]                          dataset, 
        double                            turbidity, 
        double                            albedo, 
        double                            solar_elevation
        )
    {
        double[] elev_matrix;

        int int_turbidity = (int)turbidity;
        double turbidity_rem = turbidity - (double)int_turbidity;
        double res;
        solar_elevation = pow(solar_elevation / (MATH_PI / 2.0), (1.0 / 3.0));
        
        // alb 0 low turb
        elev_matrix = Arrays.copyOfRange(dataset, 6*(int_turbidity-1), dataset.length);
        
        //(1-t).^3* A1 + 3*(1-t).^2.*t * A2 + 3*(1-t) .* t .^ 2 * A3 + t.^3 * A4;
        res = (1.0-albedo) * (1.0 - turbidity_rem) *
            ( pow(1.0-solar_elevation, 5.0) * elev_matrix[0] +
            5.0*pow(1.0-solar_elevation, 4.0)*solar_elevation * elev_matrix[1] +
            10.0*pow(1.0-solar_elevation, 3.0)*pow(solar_elevation, 2.0) * elev_matrix[2] +
            10.0*pow(1.0-solar_elevation, 2.0)*pow(solar_elevation, 3.0) * elev_matrix[3] +
            5.0*(1.0-solar_elevation)*pow(solar_elevation, 4.0) * elev_matrix[4] +
            pow(solar_elevation, 5.0) * elev_matrix[5]);

        // alb 1 low turb
        elev_matrix = Arrays.copyOfRange(dataset, 6*10 + 6*(int_turbidity-1), dataset.length);
        //(1-t).^3* A1 + 3*(1-t).^2.*t * A2 + 3*(1-t) .* t .^ 2 * A3 + t.^3 * A4;
        res += (albedo) * (1.0 - turbidity_rem) *
            ( pow(1.0-solar_elevation, 5.0) * elev_matrix[0] +
            5.0*pow(1.0-solar_elevation, 4.0)*solar_elevation * elev_matrix[1] +
            10.0*pow(1.0-solar_elevation, 3.0)*pow(solar_elevation, 2.0) * elev_matrix[2] +
            10.0*pow(1.0-solar_elevation, 2.0)*pow(solar_elevation, 3.0) * elev_matrix[3] +
            5.0*(1.0-solar_elevation)*pow(solar_elevation, 4.0) * elev_matrix[4] +
            pow(solar_elevation, 5.0) * elev_matrix[5]);
        if(int_turbidity == 10)
            return res;

        // alb 0 high turb
        elev_matrix = Arrays.copyOfRange(dataset, 6*(int_turbidity), dataset.length);
        //(1-t).^3* A1 + 3*(1-t).^2.*t * A2 + 3*(1-t) .* t .^ 2 * A3 + t.^3 * A4;
        res += (1.0-albedo) * (turbidity_rem) *
            ( pow(1.0-solar_elevation, 5.0) * elev_matrix[0] +
            5.0*pow(1.0-solar_elevation, 4.0)*solar_elevation * elev_matrix[1] +
            10.0*pow(1.0-solar_elevation, 3.0)*pow(solar_elevation, 2.0) * elev_matrix[2] +
            10.0*pow(1.0-solar_elevation, 2.0)*pow(solar_elevation, 3.0) * elev_matrix[3] +
            5.0*(1.0-solar_elevation)*pow(solar_elevation, 4.0) * elev_matrix[4] +
            pow(solar_elevation, 5.0) * elev_matrix[5]);

        // alb 1 high turb
        elev_matrix = Arrays.copyOfRange(dataset, 6*10 + 6*(int_turbidity), dataset.length);
        //(1-t).^3* A1 + 3*(1-t).^2.*t * A2 + 3*(1-t) .* t .^ 2 * A3 + t.^3 * A4;
        res += (albedo) * (turbidity_rem) *
            ( pow(1.0-solar_elevation, 5.0) * elev_matrix[0] +
            5.0*pow(1.0-solar_elevation, 4.0)*solar_elevation * elev_matrix[1] +
            10.0*pow(1.0-solar_elevation, 3.0)*pow(solar_elevation, 2.0) * elev_matrix[2] +
            10.0*pow(1.0-solar_elevation, 2.0)*pow(solar_elevation, 3.0) * elev_matrix[3] +
            5.0*(1.0-solar_elevation)*pow(solar_elevation, 4.0) * elev_matrix[4] +
            pow(solar_elevation, 5.0) * elev_matrix[5]);
        return res;
    }
    
    double ArHosekSkyModel_GetRadianceInternal(
        ArHosekSkyModelConfiguration  configuration, 
        double                        theta, 
        double                        gamma
        )
    {
        double expM = exp(configuration.values[4] * gamma);
        double rayM = cos(gamma)*cos(gamma);
        double mieM = (1.0 + cos(gamma)*cos(gamma)) / pow((1.0 + configuration.values[8]*configuration.values[8] - 2.0*configuration.values[8]*cos(gamma)), 1.5);
        double zenith = sqrt(cos(theta));

        return (1.0 + configuration.values[0] * exp(configuration.values[1] / (cos(theta) + 0.01))) *
            (configuration.values[2] + configuration.values[3] * expM + configuration.values[5] * rayM + configuration.values[6] * mieM + configuration.values[7] * zenith);
    }
    
    
    /* ----------------------------------------------------------------------------

        arhosekskymodelstate_alloc_init() function
        ------------------------------------------

        Initialises an ArHosekSkyModelState struct for a terrestrial setting.

    ---------------------------------------------------------------------------- */
    public ArHosekSkyModelState   arhosekskymodelstate_alloc_init(
        float  solar_elevation,
        float  atmospheric_turbidity,
        float  ground_albedo
        )
    {
        return null;
    }
    
    /* ----------------------------------------------------------------------------

    arhosekskymodelstate_alienworld_alloc_init() function
    -----------------------------------------------------

    Initialises an ArHosekSkyModelState struct for an "alien world" setting
    with a sun of a surface temperature given in 'kelvin'. The parameter
    'solar_intensity' controls the overall brightness of the sky, relative
    to the solar irradiance on Earth. A value of 1.0 yields a sky dome that
    is, on average over the wavelenghts covered in the model (!), as bright
    as the terrestrial sky in radiometric terms. 
    
    Which means that the solar radius has to be adjusted, since the 
    emissivity of a solar surface with a given temperature is more or less 
    fixed. So hotter suns have to be smaller to be equally bright as the 
    terrestrial sun, while cooler suns have to be larger. Note that there are
    limits to the validity of the luminance patterns of the underlying model:
    see the discussion above for more on this. In particular, an alien sun with
    a surface temperature of only 2000 Kelvin has to be very large if it is
    to be as bright as the terrestrial sun - so large that the luminance 
    patterns are no longer a really good fit in that case.
    
    If you need information about the solar radius that the model computes
    for a given temperature (say, for light source sampling purposes), you 
    have to query the 'solar_radius' variable of the sky model state returned 
    *after* running this function.

---------------------------------------------------------------------------- */

    public ArHosekSkyModelState  arhosekskymodelstate_alienworld_alloc_init(
        float  solar_elevation,
        float  solar_intensity,
        float  solar_surface_temperature_kelvin,
        float  atmospheric_turbidity,
        float  ground_albedo
        )
    {
        return null;
    }
    
    public void arhosekskymodelstate_free(ArHosekSkyModelState  state)
    {
        
    }

    public float arhosekskymodel_radiance(
        ArHosekSkyModelState    state,
        double                  theta, 
        double                  gamma, 
        double                  wavelength
        )
    {
        return 0;
    }
    
    // CIE XYZ and RGB versions


    public ArHosekSkyModelState   arhosek_xyz_skymodelstate_alloc_init(
        double  turbidity, 
        double  albedo, 
        double  elevation
        )
    {
        ArHosekSkyModelState state = new ArHosekSkyModelState();
        
        state.solar_radius = TERRESTRIAL_SOLAR_RADIUS;
        state.turbidity    = turbidity;
        state.albedo       = albedo;
        state.elevation    = elevation;
        
        for(int channel = 0; channel < 3; ++channel )
        {
            ArHosekSkyModel_CookConfiguration(
                datasetsXYZ[channel], 
                state.configs[channel], 
                turbidity, 
                albedo, 
                elevation);
        
            state.radiances[channel] = 
            ArHosekSkyModel_CookRadianceConfiguration(
                datasetsXYZRad[channel],
                turbidity, 
                albedo,
                elevation);
        }
        return state;        
    }


    public ArHosekSkyModelState   arhosek_rgb_skymodelstate_alloc_init(
        double  turbidity, 
        double  albedo, 
        double  elevation
        )
    {
        ArHosekSkyModelState state = new ArHosekSkyModelState();
    
        state.solar_radius = TERRESTRIAL_SOLAR_RADIUS;
        state.turbidity    = turbidity;
        state.albedo       = albedo;
        state.elevation    = elevation;

        for(int channel = 0; channel < 3; ++channel )
        {
            ArHosekSkyModel_CookConfiguration(
                datasetsRGB[channel], 
                state.configs[channel], 
                turbidity, 
                albedo, 
                elevation);
        
            state.radiances[channel] = 
                ArHosekSkyModel_CookRadianceConfiguration(
                datasetsRGBRad[channel],
                turbidity, 
                albedo,
                elevation);
        }
        return state;
    }
    
    public double arhosek_tristim_skymodel_radiance(
        ArHosekSkyModelState    state,
        double                  theta,
        double                  gamma, 
        int                     channel
        )
    {        
        return ArHosekSkyModel_GetRadianceInternal(
                state.configs[channel], 
                theta, 
                gamma) 
            * state.radiances[channel];
    }
    
    public Color arhosek_rgb_skymodel_radiance()
    {
        return null;
    }
    
    //   Delivers the complete function: sky + sun, including limb darkening.
    //   Please read the above description before using this - there are several
    //   caveats!

    public float arhosekskymodel_solar_radiance(
        ArHosekSkyModelState        state,
        float                       theta,
        float                       gamma,
        float                       wavelength
        )
    {
        return 0;
    }
    
    /*
    This struct holds the pre-computation data for one particular albedo value.
    Most fields are self-explanatory, but users should never directly 
    manipulate any of them anyway. The only consistent way to manipulate such 
    structs is via the functions 'arhosekskymodelstate_alloc_init' and 
    'arhosekskymodelstate_free'.
    
    'emission_correction_factor_sky'
    'emission_correction_factor_sun'

        The original model coefficients were fitted against the emission of 
        our local sun. If a different solar emission is desired (i.e. if the
        model is being used to predict skydome appearance for an earth-like 
        planet that orbits a different star), these correction factors, which 
        are determined during the alloc_init step, are applied to each waveband 
        separately (they default to 1.0 in normal usage). This is the simplest 
        way to retrofit this sort of capability to the existing model. The 
        different factors for sky and sun are needed since the solar disc may 
        be of a different size compared to the terrestrial sun.
    */
    
    
    public void setElevationDegrees(float degrees)
    {
        sunPosition = SphericalCoordinate.elevationDegrees(degrees);
    }
    
    public void setTurbidity(float turbidity)
    {
        this.turbidity = turbidity;
    }
    
    public void setAlbedo(float albedo)
    {
        this.albedo = albedo;
    }
    
    public void setExposure(float exposure)
    {
        this.exposure = exposure;
    }
    
    public void setTonemapGamma(float tonemapGamma)
    {
        this.tonemapGamma = tonemapGamma;
    }
  
    float solarZenith()
    {
        return SphericalCoordinate.thetaRadians(sunPosition);
    }
    
    float zenith(Vector3f v)
    {
        return SphericalCoordinate.thetaRadians(v);
    }
    
    float gamma(Vector3f v)
    {
        return SphericalCoordinate.getRadiansBetween(v, sunPosition);
    }
    
    public void init()
    {
        currentState = arhosek_rgb_skymodelstate_alloc_init(turbidity, albedo, SphericalCoordinate.elevationRadians(sunPosition));        
    }
    
    public Color getColor(Vector3f v)
    {
        float gamma         = gamma(v);
        float theta         = zenith(v);
        
        if(Math.toDegrees(theta) > 90)
            return new Color();
        
        double r = this.arhosek_tristim_skymodel_radiance(currentState, theta, gamma, 0);
        double g = this.arhosek_tristim_skymodel_radiance(currentState, theta, gamma, 1);
        double b = this.arhosek_tristim_skymodel_radiance(currentState, theta, gamma, 2)
                ;
        return new Color(r, g, b).mul(exposure).simpleGamma(tonemapGamma);
    }
}
