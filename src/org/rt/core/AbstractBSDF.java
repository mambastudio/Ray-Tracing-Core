/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
