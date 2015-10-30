/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.light;

import core.AbstractLight;
import core.AbstractShape;
import core.coordinates.Point2f;
import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import core.math.BoundingSphere;
import core.math.Color;
import core.math.FloatValue;
import core.math.Transform;

/**
 *
 * @author user
 */
public class AreaLight extends AbstractLight
{
    AbstractShape shape;
    
    public AreaLight(AbstractShape shape, Transform l2w)
    {
        super(l2w);
        this.shape = shape;
    }
    
    @Override
    public Color illuminate(BoundingSphere sceneSphere, Point3f receivingPosition, Point2f rndTuple, Point3f emittingPosition, FloatValue directPdfW, FloatValue emissionPdfW, FloatValue cosAtLight) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Color emit(BoundingSphere sceneSphere, Point2f dirRndTuple, Point2f posRndTuple, Point3f position, Vector3f direction, FloatValue emissionPdfW, FloatValue directPdfA, FloatValue cosThetaLight) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Color radiance(BoundingSphere sceneSphere, Vector3f rayDirection, Point3f hitPoint, FloatValue directPdfA, FloatValue emissionPdfW) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isFinite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isDelta() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isAreaLight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
