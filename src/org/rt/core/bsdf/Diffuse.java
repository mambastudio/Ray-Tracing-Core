/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.bsdf;

import org.rt.core.AbstractBSDF;
import org.rt.core.BSDFType;
import org.rt.core.coordinates.Point2f;
import org.rt.core.coordinates.Vector3f;
import org.rt.core.color.Color;
import org.rt.core.math.FloatValue;
import org.rt.core.math.Frame;
import org.rt.core.math.Utility;

/**
 *
 * @author user
 */
public class Diffuse extends AbstractBSDF
{
    public Diffuse(Color color)
    {
        super(color);
    }
    
    public Diffuse(Color color, Frame frame, Vector3f localWi)
    {
        super(color, frame, localWi);
    }
    
    @Override
    public BSDFType type() {
        return BSDFType.DIFFUSE;
    }
    
    @Override
    public Color sample(Point2f rndTuple, Vector3f worldWo, FloatValue pdfWo, FloatValue cosWo) 
    {
        if(localWi.z < Utility.EPS_COSINE)
            return new Color();
                
        Vector3f localDirGen = Utility.sampleCosineHemisphereW(rndTuple.x, rndTuple.y, pdfWo);       
        Color bsdfColor = color.mul(Utility.INV_PI_F);
       
        cosWo.value   = Math.abs(localDirGen.z);
        if(cosWo.value < Utility.EPS_COSINE)
            return new Color();
        
        //Vector3f v = frame.toWorld(new Vector3f());
        // System.out.println("ks");
        worldWo.set(frame.toWorld(localDirGen));
       
        return bsdfColor;
    }

    @Override
    public Color evaluate(Vector3f worldWo, FloatValue cosWo, FloatValue directPdfWo, FloatValue reversePdfWo) {
        Color bsdfColor = new Color();
        Vector3f localDirGen = frame.toLocal(worldWo);
       
        if(localDirGen.z * localWi.z < 0)
            return bsdfColor;
        
        cosWo.value = Math.abs(localDirGen.z);
         
        if(localWi.z < Utility.EPS_COSINE || localDirGen.z < Utility.EPS_COSINE)
            return bsdfColor;
        
        if(directPdfWo != null)
            directPdfWo.value += Math.max(0.f, localDirGen.z * Utility.INV_PI_F);

        if(reversePdfWo != null)
            reversePdfWo.value += Math.max(0.f, localWi.z * Utility.INV_PI_F);
                
        bsdfColor.setColor(color.mul(Utility.INV_PI_F));
        return bsdfColor;
    }
    
}
