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
import static org.rt.core.BSDFType.REFRACT;
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
public class Refraction extends AbstractBSDF
{
    public float n1 = 1;
    public float n2 = 1.3f;
    public Refraction(Color color)
    {
        super(color);
    }
    
    public Refraction(Color color, Frame frame, Vector3f localWi, float n2)
    {
        super(color, frame, localWi);
        this.n2 = n2;
    }
    
    @Override
    public BSDFType type() {
        return REFRACT;
    }

    @Override
    public Color sample(Point2f rndTuple, Vector3f worldWo, FloatValue pdfWo, FloatValue cosWo) {
        float ei = n1, et = n2;
        
        Vector3f localWi_out = localWi.clone();
                
        boolean entering = localWi_out.z > 0.;
        if (!entering)
        {
           //swap(ei, et);
            float temp;
            temp = ei;
            ei = et;
            et = temp;
        }                
        
        float sini2 = Utility.sinTheta2(localWi_out);
        float eta   = ei/et;
        float sint2 = eta * eta * sini2;
        if(sint2 >= 1.f) 
        {
            return new Color();
        }
        float cost  = Math.max(0f, Utility.sqrt(1f - sint2));
        if(entering) cost = -cost;
        float sintOverSini = eta;
        Vector3f localWo = new Vector3f(sintOverSini * -localWi_out.x, sintOverSini * -localWi_out.y, cost);
        
        cosWo.value   = Math.abs(localWo.z);
        pdfWo.value = 1f;
        
        worldWo.set(frame.toWorld(localWo).normalize());
        
        //System.out.println(worldWo);
        
        return color.div(Math.abs(localWo.z));
    }

    @Override
    public Color evaluate(Vector3f worldWo, FloatValue cosWo, FloatValue directPdfWo, FloatValue reversePdfWo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
