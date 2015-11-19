/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.bsdf;

import core.AbstractBSDF;
import core.BSDFType;
import static core.BSDFType.REFRACT;
import core.coordinates.Point2f;
import core.coordinates.Vector3f;
import core.math.Color;
import core.math.FloatValue;
import core.math.Utility;

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
        
        return color.div(Math.abs(localWo.z));
    }

    @Override
    public Color evaluate(Vector3f worldWo, FloatValue cosWo, FloatValue directPdfWo, FloatValue reversePdfWo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
