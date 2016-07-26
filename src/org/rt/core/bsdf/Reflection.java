/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.bsdf;

import org.rt.core.AbstractBSDF;
import org.rt.core.BSDFType;
import static org.rt.core.BSDFType.REFLECT;
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
public class Reflection extends AbstractBSDF
{
    public Reflection(Color color)
    {
        super(color);
    }
    
    public Reflection(Color color, Frame frame, Vector3f localWi)
    {
        super(color, frame, localWi);
    }

    @Override
    public BSDFType type() 
    {
        return REFLECT;
    }

    @Override
    public Color sample(Point2f rndTuple, Vector3f worldWo, FloatValue pdfWo, FloatValue cosWo) 
    {        
        Vector3f localWo = Utility.reflectLocal(localWi);
        worldWo.set(frame.toWorld(localWo));
        
        pdfWo.value = 1f;
        
        cosWo.value   = Math.abs(localWo.z);
        if(cosWo.value < Utility.EPS_COSINE)
            return new Color();
        
        // BSDF is multiplied (outside) by cosine (oLocalDirGen.z),
        // for mirror this shouldn't be done, so we pre-divide here instead
        return color.div(localWo.z);
    }

    @Override
    public Color evaluate(Vector3f worldWo, FloatValue cosWo, FloatValue directPdfWo, FloatValue reversePdfWo) {
        return null;
    }
    
}
