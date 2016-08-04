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

import org.rt.core.AbstractLight;
import org.rt.core.Scene;
import org.rt.core.color.Color;
import org.rt.core.coordinates.Point2f;
import org.rt.core.coordinates.Point3f;
import org.rt.core.coordinates.Vector3f;
import org.rt.core.math.FloatValue;
import org.rt.core.math.Frame;
import org.rt.core.math.Ray;
import org.rt.core.math.Transform;
import org.rt.core.math.Utility;

/**
 *
 * @author user
 */
public class DirectionalLight extends AbstractLight
{
    Frame frame = null;
    Color intensity = null;
    
    public DirectionalLight(Color color, Vector3f direction)
    {
        super(new Transform());
        this.frame = new Frame(direction.normalize());
        this.intensity = color;
        
    }
    @Override
    public Color illuminate(Scene scene, Point3f receivingPosition, Point2f rndTuple, Ray rayToLight, FloatValue cosAtLight) {
        
        rayToLight.d.set(frame.normal().neg());
        rayToLight.o.set(receivingPosition);
        rayToLight.setMax(1e36f);
        rayToLight.init();
        
        if(cosAtLight != null)
            cosAtLight.value = 1.f;
       
        return intensity.clone();
    }

    @Override
    public Color emit(Scene scene, Point2f dirRndTuple, Point2f posRndTuple, Ray rayFromLight, FloatValue cosAtLight) 
    {
        //Sample uniform disk
        Point2f xy = Utility.sampleConcentricDisc(posRndTuple.x, posRndTuple.y);
            
        //Direction from light
        Vector3f direction = frame.normal().clone();
               
        //Position from light        
        Vector3f a = direction.add(frame.binormal().mul(xy.x)).add(frame.tangent().mul(xy.y));
        Point3f position = scene.getBoundingSphere().c.add(a.mul(scene.getBoundingSphere().r));
        
        //Ray from light setup
        rayFromLight.d.set(direction);
        rayFromLight.o.set(position);
        rayFromLight.init();
        
        // Not used for infinite or delta lights
        if(cosAtLight != null)
            cosAtLight.value = 1.f;

        return intensity.clone();
    }

    @Override
    public Color radiance(Scene scene, Point3f hitPoint, Vector3f direction, FloatValue cosAtLight) {
        return new Color();
    }

    @Override
    public boolean isFinite() {
        return false;
    }

    @Override
    public boolean isDelta() {
        return true;
    }

    @Override
    public boolean isAreaLight() {
        return false;
    }

    @Override
    public float directPdfW(Scene scene, Point3f p, Vector3f w) {
        return 1f;
    }

    @Override
    public float directPdfA(Scene scene, Vector3f w) {
        return 1f;
    }

    @Override
    public float emissionPdfW(Scene scene, Vector3f w, float cosAtLight) {
        return Utility.concentricDiscPdfA() * scene.getBoundingSphere().invRadiusSqr;
    }

    @Override
    public boolean isCompound() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
