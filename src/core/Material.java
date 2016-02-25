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
   
    public Color diffuseReflectance;    
    public Color phongReflectance;
    public Color specularReflectance;
    public Color emission;
    
    public float power;
    public float phongExponent;
    public float ior;
    
    public String name;
    
    public Material()
    {
        diffuseReflectance = new Color(0.9, 0.9453, 0.9);
        phongReflectance = new Color();
        specularReflectance = new Color();
        emission = new Color();
        
        power = 1;
        phongExponent = 20;
        ior = -1;
        
        name = "diffuse surface";
    }
   
    public float albedoDiffuse()
    {
        return diffuseReflectance.luminance();
    }
    
    public float albedoPhong()
    {
        return phongReflectance.luminance();
    }
    
    public float albedoReflect()
    {
        return specularReflectance.luminance();
    }
    
    public float albedoRefract()
    {
        return specularReflectance.luminance();
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
                     diffuseReflectance.add(phongReflectance.add(specularReflectance.mul(reflectCoeff))).max() + (1.f - reflectCoeff);
                
            bsdf.continuationProbability = Math.min(1.f, Math.max(0.f, bsdf.continuationProbability));
        }
    }
}
