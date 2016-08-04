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
