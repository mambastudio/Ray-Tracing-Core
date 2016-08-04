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
