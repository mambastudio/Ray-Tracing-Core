/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.color.sun;

import core.color.CIE1931;
import core.color.Color;
import core.color.RGBSpace;
import core.color.XYZ;
import core.color.sun.data.ArHosekSkyModelConfiguration;
import static core.color.sun.data.ArHosekSkyModelData_CIEXYZ.datasetsXYZ;
import static core.color.sun.data.ArHosekSkyModelData_CIEXYZ.datasetsXYZRad;
import static core.color.sun.data.ArHosekSkyModelData_RGB.datasetsRGB;
import static core.color.sun.data.ArHosekSkyModelData_RGB.datasetsRGBRad;
import static core.color.sun.data.ArHosekSkyModelData_Spectral.datasets;
import static core.color.sun.data.ArHosekSkyModelData_Spectral.datasetsRad;
import static core.color.sun.data.ArHosekSkyModelData_Spectral.limbDarkeningDatasets;
import static core.color.sun.data.ArHosekSkyModelData_Spectral.solarDatasets;
import core.color.sun.data.ArHosekSkyModelState;
import core.coordinates.Vector3f;
import core.image.HDR;
import core.light.Envmap;
import core.math.SphericalCoordinate;
import static java.lang.Math.cos;
import static java.lang.Math.exp;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
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
        initStateRadiance();
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

        Initialises an ArHosekSkyModelState struct for a terrestrial setting 
        
        Spectral version
    ---------------------------------------------------------------------------- */
    public ArHosekSkyModelState   arhosekskymodelstate_alloc_init(
        float  solar_elevation,
        float  atmospheric_turbidity,
        float  ground_albedo
        )
    {
        
        ArHosekSkyModelState state = new ArHosekSkyModelState();
        
        state.solar_radius = ( 30.81 * DEGREES ) / 2.0; //repetition in code with TERRESTIAL_SOLAR_RADIUS
        state.turbidity    = atmospheric_turbidity;
        state.albedo       = ground_albedo;
        state.elevation    = solar_elevation;
        
        for(int wl = 0; wl < 11; ++wl )
        {
            ArHosekSkyModel_CookConfiguration(
                datasets[wl], 
                state.configs[wl], 
                atmospheric_turbidity, 
                ground_albedo, 
                solar_elevation
            );

            state.radiances[wl] = 
            ArHosekSkyModel_CookRadianceConfiguration(
                datasetsRad[wl],
                atmospheric_turbidity,
                ground_albedo,
                solar_elevation
                );

            state.emission_correction_factor_sun[wl] = 1.0;
            state.emission_correction_factor_sky[wl] = 1.0;
        }

        return state;        
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

    //Spectral version
    
    public double arhosekskymodel_radiance(
        ArHosekSkyModelState    state,
        double                  theta, 
        double                  gamma, 
        double                  wavelength
        )
    {
        int low_wl = (int) ((wavelength - 320.0 ) / 40.0);
        

        if ( low_wl < 0 || low_wl >= 11 )
            return 0.0f;

        double interp = ((wavelength - 320.0 ) / 40.0) % 1.0;
        
        double val_low = 
            ArHosekSkyModel_GetRadianceInternal(
                state.configs[low_wl],
                theta,
                gamma
             )
            *   state.radiances[low_wl]
            *   state.emission_correction_factor_sky[low_wl];

        if ( interp < 1e-6 )
            return val_low;

        double result = ( 1.0 - interp ) * val_low;

        if ( low_wl+1 < 11 )
        {
            result +=
                interp
                * ArHosekSkyModel_GetRadianceInternal(
                    state.configs[low_wl+1],
                    theta,
                    gamma
                  )
            * state.radiances[low_wl+1]
            * state.emission_correction_factor_sky[low_wl+1];
        }

        return result;        
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
    
    final int pieces = 45;
    final int order = 4;

    public double arhosekskymodel_sr_internal(
        ArHosekSkyModelState    state,
        int                     turbidity,
        int                     wl,
        double                  elevation
        )
    {
        int pos =
            (int) (pow(2.0*elevation / MATH_PI, 1.0/3.0) * pieces); // floor
    
        if ( pos > 44 ) pos = 44;
    
        final double break_x =
            pow(((double) pos / (double) pieces), 3.0) * (MATH_PI * 0.5);
        
        final double [] coefs = Arrays.copyOfRange(solarDatasets[wl], 0, (order * pieces * turbidity + order * (pos+1) - 1) + 1); //add 1, to point to the last index which is exclusive (not copied)
        int arrayIndex = coefs.length-1;
        
        double res = 0.0;
        final double x = elevation - break_x;
        double x_exp = 1.0;

        for (int i = 0; i < order; ++i)
        {
         
            res += x_exp * coefs[arrayIndex]; arrayIndex--;            
            x_exp *= x;
        }

        return  res * state.emission_correction_factor_sun[wl];
    }
    
    double arhosekskymodel_solar_radiance_internal2(
        ArHosekSkyModelState    state,
        double                  wavelength,
        double                  elevation,
        double                  gamma
        )
    {
        assert(wavelength >= 320.0
            && wavelength <= 720.0
            && state.turbidity >= 1.0
            && state.turbidity <= 10.0);
        
        int     turb_low  = (int) state.turbidity - 1;
        double  turb_frac = state.turbidity - (double) (turb_low + 1);
    
        if ( turb_low == 9 )
        {
            turb_low  = 8;
            turb_frac = 1.0;
        }

        int    wl_low  = (int) ((wavelength - 320.0) / 40.0);
        double wl_frac = (wavelength % 40.0) / 40.0;
    
        if ( wl_low == 10 )
        {
            wl_low = 9;
            wl_frac = 1.0;
        }
        
        double direct_radiance =
            ( 1.0 - turb_frac )
          * (    (1.0 - wl_frac)
             * arhosekskymodel_sr_internal(
                     state,
                     turb_low,
                     wl_low,
                     elevation
                   )
           +   wl_frac
             * arhosekskymodel_sr_internal(
                     state,
                     turb_low,
                     wl_low+1,
                     elevation
                   )
          )
        +   turb_frac
            * (    ( 1.0 - wl_frac )
             * arhosekskymodel_sr_internal(
                     state,
                     turb_low+1,
                     wl_low,
                     elevation
                   )
           +   wl_frac
             * arhosekskymodel_sr_internal(
                     state,
                     turb_low+1,
                     wl_low+1,
                     elevation
                   )
          );
        
        double [] ldCoefficient = new double[6];
    
        for ( int i = 0; i < 6; i++ )
            ldCoefficient[i] =
              (1.0 - wl_frac) * limbDarkeningDatasets[wl_low  ][i]
            +        wl_frac  * limbDarkeningDatasets[wl_low+1][i];
    
        // sun distance to diameter ratio, squared

        final double sol_rad_sin = sin(state.solar_radius);
        final double ar2 = 1 / ( sol_rad_sin * sol_rad_sin );
        final double singamma = sin(gamma);
        double sc2 = 1.0 - ar2 * singamma * singamma;
        if (sc2 < 0.0 ) sc2 = 0.0;
        double sampleCosine = sqrt (sc2);
    
        //   The following will be improved in future versions of the model:
        //   here, we directly use fitted 5th order polynomials provided by the
        //   astronomical community for the limb darkening effect. Astronomers need
        //   such accurate fittings for their predictions. However, this sort of
        //   accuracy is not really needed for CG purposes, so an approximated
        //   dataset based on quadratic polynomials will be provided in a future
        //   release.
        
        double  darkeningFactor =
            ldCoefficient[0]
          + ldCoefficient[1] * sampleCosine
          + ldCoefficient[2] * pow( sampleCosine, 2.0 )
          + ldCoefficient[3] * pow( sampleCosine, 3.0 )
          + ldCoefficient[4] * pow( sampleCosine, 4.0 )
          + ldCoefficient[5] * pow( sampleCosine, 5.0 );
        
        direct_radiance *= darkeningFactor;

        return direct_radiance;
    }
    
    //   Delivers the complete function: sky + sun, including limb darkening.
    //   Please read the above description before using this - there are several
    //   caveats!

    public double arhosekskymodel_solar_radiance(
        ArHosekSkyModelState         state,
        double                       theta,
        double                       gamma,
        double                       wavelength
        )
    {
        double  direct_radiance =
        arhosekskymodel_solar_radiance_internal2(
            state,
            wavelength,
            ((MATH_PI/2.0)-theta),
            gamma
            );

        double  inscattered_radiance =
        arhosekskymodel_radiance(
            state,
            theta,
            gamma,
            wavelength
            );
    
        return  direct_radiance + inscattered_radiance;
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
    
    public void initStateRGB()
    {
        currentState = arhosek_rgb_skymodelstate_alloc_init(turbidity, albedo, SphericalCoordinate.elevationRadians(sunPosition));        
    }
    
    public void initStateXYZ()
    {
        currentState = arhosek_xyz_skymodelstate_alloc_init(turbidity, albedo, SphericalCoordinate.elevationRadians(sunPosition));
    }
    
    public void initStateRadiance()
    {        
        currentState = arhosekskymodelstate_alloc_init(SphericalCoordinate.elevationRadians(sunPosition), turbidity, albedo);
    }
    
    public Color getRGB(Vector3f d)
    {
        Vector3f dir = d.clone();
                
        if(dir.y < 0)
            return new Color();        
                    
        float gamma         = gamma(dir);
        float theta         = zenith(dir);
               
        double r = this.arhosek_tristim_skymodel_radiance(currentState, theta, gamma, 0);
        double g = this.arhosek_tristim_skymodel_radiance(currentState, theta, gamma, 1);
        double b = this.arhosek_tristim_skymodel_radiance(currentState, theta, gamma, 2)
                ;
        return new Color(r, g, b).mul(exposure).simpleGamma(tonemapGamma);
    }
    
    public Color getRGB_using_XYZ(Vector3f d)
    {
        Vector3f dir = d.clone();
                
        if(dir.y < 0)
            return new Color();        
                    
        float gamma         = gamma(dir);
        float theta         = zenith(dir);
               
        double X = this.arhosek_tristim_skymodel_radiance(currentState, theta, gamma, 0);
        double Y = this.arhosek_tristim_skymodel_radiance(currentState, theta, gamma, 1);
        double Z = this.arhosek_tristim_skymodel_radiance(currentState, theta, gamma, 2);
        
        Color color =  RGBSpace.convertXYZtoRGB(new XYZ(X, Y, Z));   
        
        return color.mul(exposure).simpleGamma(tonemapGamma);
    }
    
    public Color getRGB_using_radiance(Vector3f d)
    {
        Vector3f dir = d.clone();
                
        if(dir.y < 0)
            return new Color();        
                    
        float gamma         = gamma(dir);
        float theta         = zenith(dir);
        
        float X, Y, Z;
        X = Y = Z = 0;
        
        for(int i = 320; i<=720; i+=40)
        {
            double radiance = this.arhosekskymodel_radiance(currentState, theta, gamma, i);
            X += CIE1931.getX(radiance, i);
            Y += CIE1931.getY(radiance, i);
            Z += CIE1931.getZ(radiance, i);
        }
        
        Color color =  RGBSpace.convertXYZtoRGB(new XYZ(X, Y, Z).mul(40));   
        return color.mul(exposure).simpleGamma(tonemapGamma);
    }
    
    public Color getRGB_using_solar_radiance(Vector3f d)
    {
        Vector3f dir = d.clone();
                
        if(dir.y < 0)
            return new Color();        
                    
        float gamma         = gamma(dir);
        float theta         = zenith(dir);
        
        float X, Y, Z;
        X = Y = Z = 0;
        
        for(int i = 320; i<=720; i+=40)
        {
            double radiance = this.arhosekskymodel_solar_radiance(currentState, theta, gamma, i);
            X += CIE1931.getX(radiance, i);
            Y += CIE1931.getY(radiance, i);
            Z += CIE1931.getZ(radiance, i);
        }
        
        Color color =  RGBSpace.convertXYZtoRGB(new XYZ(X, Y, Z).mul(40));   
        return color.mul(0.000001f).simpleGamma(1.2f);
    }
    
    public HDR getHDR(int size)
    {
         HDR hdr = new HDR(size, size);
        
        for(int j = 0; j<size; j++)
            for(int i = 0; i<size; i++)
            {
                Color color = getRGB(SphericalCoordinate.sphericalDirection(i, j, size, size));
                hdr.setColor(i, j, color);
            }
                
        return hdr;
    }
    
    public Envmap getEnvmap(int size)
    {
        return new Envmap(getHDR(size));
    }
}
