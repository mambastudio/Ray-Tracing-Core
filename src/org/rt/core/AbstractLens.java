/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core;

import org.rt.core.coordinates.Point2f;
import org.rt.core.coordinates.Point3f;
import org.rt.core.math.Ray;

/**
 *
 * @author user
 */
public abstract class AbstractLens
{
    public abstract Ray generateRay(float x, float y, float xResolution, float yResolution, float lensX, float lensY);
    public abstract Point2f generatePixel(Point3f aHitpoint, float xResolution, float yResolution, float lensX, float lensY);

}
