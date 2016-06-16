/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.color.sun.data;

import core.coordinates.Vector3f;
import core.math.SphericalCoordinate;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class ArHosekSkyModelState {
    public ArHosekSkyModelConfiguration[]  configs     = new ArHosekSkyModelConfiguration[11];
    public double []    radiances   = new double[11];
    public double       turbidity;
    public double       solar_radius;
    public double []    emission_correction_factor_sky = new double[11];
    public double []    emission_correction_factor_sun = new double[11];
    public double       albedo;
    public double       elevation;
        
    public ArHosekSkyModelState()
    {
        for(int i = 0; i<configs.length; i++)
            configs[i] = new ArHosekSkyModelConfiguration();
    }
        
    public String radiancesString()
    {
        return Arrays.toString(radiances);
    }
        
    public String configsString()
    {
    StringBuilder builder = new StringBuilder();
        for(ArHosekSkyModelConfiguration config: configs)
        {
            builder.append(config.toString()).append("\n");
        }
        return builder.toString();
    }
    
    public double solarRadiusToDegrees()
    {
        double solarRadiusDegrees = Math.toDegrees(solar_radius);
        return solarRadiusDegrees;
    }
    
    public double elevationToDegrees()
    {
        double elevationDegrees = Math.toDegrees(elevation);
        return elevationDegrees;
    }
    
    public Vector3f getSolarDirection()
    {       
        return SphericalCoordinate.elevationRadians((float)elevation);        
    }
}
