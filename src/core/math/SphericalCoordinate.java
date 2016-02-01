/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.math;

import core.coordinates.Vector3f;
import static core.math.Utility.HALF_PI_F;
import static core.math.Utility.PI_F;
import static core.math.Utility.acosf;
import static core.math.Utility.atan2f;
import static core.math.Utility.cosf;
import static core.math.Utility.sinf;
import static core.math.Utility.toDegrees;

/**
 *
 * @author user
 */
public class SphericalCoordinate 
{
    //theta values are from z-axis and covers 180 degrees to negative z-axis
    //phi values are 360 degrees and cover a full circle horizontal surface (xy)
    //assumption here is a right hand coordinate system
    
    //http://blog.demofox.org/2013/10/12/converting-to-and-from-polar-spherical-coordinates-made-easy/
    //but here we use different axis orientation contrary to the page (see above paragraph in this class)
    
    //this class is good for sunlight coordinates
    
    private SphericalCoordinate()
    {
        
    }
    
    public static float thetaRadians(Vector3f v)
    {
        return acosf(v.y);  // Y / radius -> radius = 1 since v is normalized
    }
    
    public static float thetaDegrees(Vector3f v)
    {
        return toDegrees(thetaRadians(v));
    }
        
    public static float phiRadians(Vector3f v)
    {        
        return atan2f(v.x, -v.z);
    }
        
    public static float phiDegrees(Vector3f v)
    {
        return toDegrees(phiRadians(v));
    }
    
    public static Vector3f direction(float theta, float phi)
    {
        float x = sinf(phi) * sinf(theta);                
        float y = cosf(theta);
        float z = -cosf(phi) * sinf(theta);
        
        return new Vector3f(x, y, z);
    }
    
    public static Vector3f hemisphereDirection(float i, float j, float width, float height)
    {
        //Since this is the right hand rule coordinate system, the phi covers 360 degrees
        //intuitively, but the z-axis is negative hence we subtract with PI_F to rotate
        //by 180 degrees
        
        //For theta angle (zenith angle), we factor by half-pi since it's in the hemisphere range
        
        float scalePhi = i / width;
        float scaleTheta = j / height;
        
        float phi = PI_F * (2 * scalePhi - 1f);
        float theta = scaleTheta * HALF_PI_F;
        
        return direction(theta, phi);
    }
    
    public static Vector3f sphericalDirection(float i, float j, float width, float height)
    {
        //Since this is the right hand rule coordinate system, the phi covers 360 degrees
        //intuitively, but the z-axis is negative hence we subtract with PI_F to rotate
        //by 180 degrees
        
        float scalePhi = i / width;
        float scaleTheta = j / height;
        
        float phi = PI_F * (2 * scalePhi - 1f);
        float theta = scaleTheta * PI_F;
        
        return direction(theta, phi);
    }
    
    public static float getRadiansBetween(Vector3f v1, Vector3f v2)
    {
        return acosf(Vector3f.dot(v1, v2));
    }
    
    public static float getDegreesBetween(Vector3f v1, Vector3f v2)
    {
        return toDegrees(getRadiansBetween(v1, v2));
    }
    
}
