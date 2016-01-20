/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.color.Color;
import core.math.Utility;

/**
 *
 * @author user
 */
public class Material {
    // diffuse is simply added to the others
    public Color diffuse;
    // Phong is simply added to the others
    public Color phong;
    public float phongExponent;

    // mirror can be either simply added, or mixed using Fresnel term
    // this is governed by mIOR, if it is >= 0, fresnel is used, otherwise
    // it is not
    public Color mirrorReflectance;
    
    public float ior;
    
    
    public float albedoDiffuse()
    {
        return diffuse.luminance();
    }
    
    public float albedoPhong()
    {
        return phong.luminance();
    }
    
    public float albedoReflect()
    {
        return mirrorReflectance.luminance();
    }
    
    public float albedoRefract()
    {
        return 1f;
    }
    
    public void componentProbabilities(AbstractBSDF bsdf)
    {
        float reflectCoeff = Utility.fresnelDielectric(bsdf.cosThetaWi(), ior);

        float albedoDiffuse = albedoDiffuse();
        float albedoPhong   = albedoPhong();
        float albedoReflect = reflectCoeff          * albedoReflect();
        float albedoRefract = (1.f - reflectCoeff)  * albedoRefract();

        float totalAlbedo = albedoDiffuse + albedoPhong + albedoReflect + albedoRefract;
        
        if(totalAlbedo < 1e-9f)
        {
            bsdf.probabilities.diffProb  = 0.f;
            bsdf.probabilities.phongProb = 0.f;
            bsdf.probabilities.reflProb  = 0.f;
            bsdf.probabilities.refrProb  = 0.f;
            bsdf.continuationProbability = 0.f;
        }
        else
        {
            bsdf.probabilities.diffProb  = albedoDiffuse / totalAlbedo;
            bsdf.probabilities.phongProb = albedoPhong   / totalAlbedo;
            bsdf.probabilities.reflProb  = albedoReflect / totalAlbedo;
            bsdf.probabilities.refrProb  = albedoRefract / totalAlbedo;
            
            // The continuation probability is max component from reflectance.
            // That way the weight of sample will never rise.
            // Luminance is another very valid option.
            bsdf.continuationProbability =
                     diffuse.add(
                     phong.add(
                     mirrorReflectance.mul(reflectCoeff))).max() + (1.f - reflectCoeff);
                
            bsdf.continuationProbability = Math.min(1.f, Math.max(0.f, bsdf.continuationProbability));
        }
    }
}
