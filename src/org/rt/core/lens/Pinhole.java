/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.lens;

import org.rt.core.AbstractLens;
import org.rt.core.coordinates.Point2f;
import org.rt.core.coordinates.Point3f;
import org.rt.core.coordinates.Vector3f;
import org.rt.core.math.Ray;

/**
 *
 * @author user
 */
public class Pinhole extends AbstractLens
{
    private final float fov;
    
    public Pinhole()
    {
        fov = 90;
    }
    
    public Pinhole(float fov)
    {
        this.fov = fov;
    }
    

    @Override
    public Ray generateRay(float x, float y, float xResolution, float yResolution, float lensX, float lensY) {
        float d = (float) (1./Math.tan(Math.toRadians(fov)/2));
        
        float a = xResolution/yResolution;
        float px = a * (2 * x/xResolution - 1);
        float py = -2 * y/yResolution + 1;
        float pz = -d;
        
        Vector3f rd = new Vector3f(px, py, pz).normalize();
        Point3f ro = new Point3f();
        
        return new Ray(ro, rd);
    }

    @Override
    public Point2f generatePixel(Point3f aHitpoint, float xResolution, float yResolution, float lensX, float lensY) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
