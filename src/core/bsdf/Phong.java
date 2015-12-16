/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.bsdf;

import core.AbstractBSDF;
import core.BSDFType;
import static core.BSDFType.PHONG;
import core.coordinates.Point2f;
import core.coordinates.Vector3f;
import core.color.Color;
import core.math.FloatValue;
import core.math.Frame;
import core.math.Utility;
import static core.math.Utility.EPS_PHONG;
import static core.math.Utility.PI_F;
import static core.math.Utility.acosf;
import static core.math.Utility.powf;

/**
 *
 * @author user
 */
public class Phong extends AbstractBSDF
{    
    float ks = 1;
    float n = 50;
    
    public Phong(Color color)
    {
        super(color);
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
        Vector3f reflLocalDirFixed = Utility.reflectLocal(localWi);
        {
            Frame frame = new Frame();
            frame.setFromZ(reflLocalDirFixed);
            localWo.set(frame.toWorld(localWo));
        }

        float dot_R_Wi = Vector3f.dot(reflLocalDirFixed, localWo);
        
        worldWo.set(frame.toWorld(localWo));
        
        if(dot_R_Wi <= EPS_PHONG)
            return new Color();
        
        cosWo.value = localWo.z;
                
        float brdf = ks * (n + 2) / (2 * PI_F) * powf(dot_R_Wi, n);        
        return color.mul(brdf);
    }

    @Override
    public Color evaluate(Vector3f worldWo, FloatValue cosWo, FloatValue directPdfWo, FloatValue reversePdfWo) 
    {        
        Vector3f aLocalDirGen = frame.toLocal(worldWo);
        if(localWi.z < Utility.EPS_COSINE || aLocalDirGen.z < Utility.EPS_COSINE)
            return new Color();
        
        // assumes this is never called when rejectShadingCos(oLocalDirGen.z) is true
        Vector3f reflLocalDirIn = Utility.reflectLocal(localWi);
        float dot_R_Wi = Vector3f.dot(reflLocalDirIn, aLocalDirGen);

        if(dot_R_Wi <= EPS_PHONG)
            return new Color();
        
        if(directPdfWo != null || reversePdfWo != null)
        {
            // the sampling is symmetric
            float pdfW = Utility.pdfPowerCosHemisphereW(aLocalDirGen.z, n);

            if(directPdfWo != null)
                directPdfWo.value += pdfW;

            if(reversePdfWo != null)
                reversePdfWo.value += pdfW;
        }
        
        cosWo.value = aLocalDirGen.z;
        
        float brdf = ks * (n + 2) / (2 * PI_F) * powf(dot_R_Wi, n);
        return color.mul(brdf);
    }
    
}
