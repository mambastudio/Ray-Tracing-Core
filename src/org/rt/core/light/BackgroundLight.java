/* 
 * The MIT License
 *
 * Copyright 2016 user.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.rt.core.light;

import org.rt.core.AbstractBackground;
import org.rt.core.Scene;
import org.rt.core.coordinates.Point2f;
import org.rt.core.coordinates.Point3f;
import org.rt.core.coordinates.Vector3f;
import org.rt.core.color.Color;
import org.rt.core.math.FloatValue;
import org.rt.core.math.Frame;
import org.rt.core.math.Ray;
import org.rt.core.math.Utility;

/**
 *
 * @author user
 */
public class BackgroundLight extends AbstractBackground
{
    Color backgroundColor;    
    float scale = 1f;
    
    public BackgroundLight(Color color)
    {
        super();
        this.backgroundColor = color;        
    }

    @Override
    public Color illuminate(Scene scene, Point3f receivingPosition, Point2f rndTuple, Ray rayToLight, FloatValue cosAtLight) 
    {
        Vector3f directionToLight = Utility.sampleUniformSphereW(rndTuple, null);
        rayToLight.d.set(directionToLight);
        rayToLight.o.set(receivingPosition);
        rayToLight.setMax(1e36f);
        rayToLight.init();
        
        Color radiance = backgroundColor.mul(scale);
        
        if(cosAtLight != null)
            cosAtLight.value = 1.f;
        
        return radiance;
    }

    @Override
    public Color emit(Scene scene, Point2f dirRndTuple, Point2f posRndTuple, Ray rayFromLight, FloatValue cosAtLight) 
    {
        //direction from light
        Vector3f direction = Utility.sampleUniformSphereW(dirRndTuple, null);
        
        //radiance
        Color radiance = backgroundColor.mul(scale);
        
        //Sample uniform disk
        Point2f xy = Utility.sampleConcentricDisc(posRndTuple.x, posRndTuple.y);
        
        Frame frame = new Frame();
        frame.setFromZ(direction);
        
        Vector3f a = direction.neg().add(frame.binormal().mul(xy.x)).add(frame.tangent().mul(xy.y));
        
        //Position from light
        Point3f position = scene.getBoundingSphere().c.add(a.mul(scene.getBoundingSphere().r));
        
        rayFromLight.d.set(direction);
        rayFromLight.o.set(position);
        rayFromLight.init();
        
        // Not used for infinite or delta lights
        if(cosAtLight != null)
            cosAtLight.value = 1.f;

        return radiance;
    }

    @Override
    public Color radiance(Scene scene, Point3f hitPoint, Vector3f direction, FloatValue cosAtLight) 
    {
        //radiance        
        Color radiance = backgroundColor.mul(scale);
        return radiance;
    }

    @Override
    public boolean isFinite() {
        return false;
    }

    @Override
    public boolean isDelta() {
        return false;
    }

    @Override
    public boolean isAreaLight() {
        return false;
    }

    @Override
    public float directPdfW(Scene scene, Point3f p, Vector3f w) 
    {        
        return Utility.uniformSpherePdfW();
    }

    @Override
    public float directPdfA(Scene scene, Vector3f w) {
        return Utility.uniformSpherePdfW();
    }

    @Override
    public float emissionPdfW(Scene scene, Vector3f w, float cosAtLight) 
    {
        float directPdf = Utility.uniformSpherePdfW();
        float positionPdf = Utility.concentricDiscPdfA() /
            scene.getBoundingSphere().radiusSqr;
        
        return directPdf * positionPdf;
    }

    @Override
    public boolean isCompound() {
        return false;
    }
    
}
