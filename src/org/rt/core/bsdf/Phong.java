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
import static org.rt.core.BSDFType.PHONG;
import org.rt.core.coordinates.Point2f;
import org.rt.core.coordinates.Vector3f;
import org.rt.core.color.Color;
import org.rt.core.math.FloatValue;
import org.rt.core.math.Frame;
import org.rt.core.math.Utility;
import static org.rt.core.math.Utility.EPS_PHONG;
import static org.rt.core.math.Utility.PI_F;
import static org.rt.core.math.Utility.acosf;
import static org.rt.core.math.Utility.powf;

/**
 *
 * @author user
 */
public class Phong extends AbstractBSDF
{        
    float n = 100;
    
    public Phong(Color color)
    {
        super(color);
    }
    
    public Phong(Color color, float n, Frame frame, Vector3f localWi)
    {
        super(color, frame, localWi);
        this.n = n;
    }
    
    @Override
    public BSDFType type() {
        return PHONG;
    }

    @Override
    public Color sample(Point2f rndTuple, Vector3f worldWo, FloatValue pdfWo, FloatValue cosWo) 
    {
        Vector3f localWo = Utility.samplePowerCosHemisphereW(rndTuple.x, rndTuple.y, n, pdfWo);
        
        // Due to numeric issues in MIS, we actually need to compute all pdfs
        // exactly the same way all the time!!!
        Vector3f reflLocalWo = Utility.reflectLocal(localWi);
        {
            Frame tempFrame = new Frame();
            tempFrame.setFromZ(reflLocalWo);
            localWo.set(tempFrame.toWorld(localWo));
        }

        float dot_R_Wi = Vector3f.dot(reflLocalWo, localWo);
        
        worldWo.set(frame.toWorld(localWo));
        
        if(dot_R_Wi <= EPS_PHONG)
            return new Color();
        
        cosWo.value = localWo.z;
                
        float brdf = (n + 2) / (2 * PI_F) * powf(dot_R_Wi, n);      
        
        return color.mul(brdf);
    }

    @Override
    public Color evaluate(Vector3f worldWo, FloatValue cosWo, FloatValue directPdfWo, FloatValue reversePdfWo) 
    {        
        Vector3f localWo = frame.toLocal(worldWo);
        if(localWi.z < Utility.EPS_COSINE || localWo.z < Utility.EPS_COSINE)
            return new Color();
        
        // assumes this is never called when rejectShadingCos(oLocalDirGen.z) is true
        Vector3f reflLocalWo = Utility.reflectLocal(localWi);
        float dot_R_Wo = Vector3f.dot(reflLocalWo, localWo);

        if(dot_R_Wo <= EPS_PHONG)
            return new Color();
        
        if(directPdfWo != null || reversePdfWo != null)
        {
            // the sampling is symmetric
            float pdfW = Utility.pdfPowerCosHemisphereW(dot_R_Wo, n);

            if(directPdfWo != null)
                directPdfWo.value += pdfW;

            if(reversePdfWo != null)
                reversePdfWo.value += pdfW;
        }
        
        cosWo.value = localWo.z;
        
        float brdf = (n + 2) / (2 * PI_F) * powf(dot_R_Wo, n);
        return color.mul(brdf);
    }
    
}
