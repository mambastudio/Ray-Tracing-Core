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
package org.rt.core;

import org.rt.core.coordinates.Normal3f;
import org.rt.core.coordinates.Point2f;
import org.rt.core.coordinates.Vector3f;
import org.rt.core.color.Color;
import org.rt.core.math.FloatValue;
import org.rt.core.math.Frame;

/**
 *
 * @author user
 */
public abstract class AbstractBSDF {
    protected Color color = null;
    public Frame frame = null;
    public Vector3f localWi = null; //Vector is always facing away from surface (negated incident ray)
    
    public float continuationProbability;
        
    public AbstractBSDF(Color color)
    {                
        this.color = color;        
        this.frame = new Frame();             
    }
    
    public AbstractBSDF(Color color, Frame frame, Vector3f localWi)
    {
        this.color = color;
        this.frame = frame;
        this.localWi = localWi;
    }
    
    public void setUp(Normal3f worldNorm, Vector3f worldWi)
    {
        frame.setFromZ(worldNorm);
        if(worldWi != null)
            this.localWi = frame.toLocal(worldWi.neg());
    }
    
    public abstract BSDFType type();       
    
    //Calculates f(reflectance) * color. 
    public abstract Color sample(Point2f rndTuple, Vector3f worldWo, FloatValue pdfWo, FloatValue cosWo);
    //Calculates f(reflectance) * color. 
    public abstract Color evaluate(Vector3f worldWo, FloatValue cosWo, FloatValue directPdfWo, FloatValue reversePdfWo);
        
    public Vector3f getWi()
    {        
        return frame.toWorld(localWi);
    }
    
    public Normal3f getNormal()
    {
        return (Normal3f) frame.normal();
    }
    
    public float cosThetaWi()
    {
       return localWi.z;
    }
    
    public float cosThetaWo(Vector3f worldWo)
    {
        return frame.toLocal(worldWo).z;
    }
        
    public Color getColor()
    {
        return color;
    }
    
    public void setColor(Color color)
    {
        this.color = color;
    }        
    
    public boolean isDiffuse()
    {
        return type() == BSDFType.DIFFUSE;
    }
    
    public boolean isReflect()
    {
        return type() == BSDFType.REFLECT;
    }
    
    public boolean isRefract()
    {
        return type() == BSDFType.REFRACT;
    }
    
    public boolean isPhong()
    {
        return type() == BSDFType.PHONG;
    }
    
    public boolean isDelta()
    {
        return isReflect() || isRefract();
    }    
}
