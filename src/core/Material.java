/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.bsdf.Diffuse;
import core.bsdf.Phong;
import core.bsdf.Reflection;
import core.bsdf.Refraction;
import core.color.Color;
import core.coordinates.Normal3f;
import core.coordinates.Vector3f;
import core.image.Texture;
import core.math.Frame;
import core.math.Rng;
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
    
    public boolean skyportal;
    public boolean emitter;
    public boolean refractive;
    
    public Texture texture = null;
    
    public String name;
        
    public Material()
    {
        diffuseReflectance = new Color(0.0, 0, 0);
        phongReflectance = new Color(0, 0.0, 0);
        specularReflectance = new Color();
        emission = new Color();
        
        power = 1;
        phongExponent = 20;
        ior = 1;
        
        skyportal = false;
        emitter = false;
        refractive = false;
        
        name = "diffuse surface";
    }
    
    public static Material createLambert(Color color)
    {
        Material material = new Material();
        material.diffuseReflectance.setColor(color);
        return material;
    }
    
    public static Material createEmission()
    {
        Material material = new Material();
        material.emission.setColor(Color.WHITE);
        material.power = 3;
        material.emitter = true;
        return material;
    }
    
    public static Material createMirror(Color color)
    {
        Material material = new Material();
        material.specularReflectance.setColor(color);
        material.refractive = false;
        return material;
    }
    
    public static Material createGlass(Color color)
    {
        Material material = new Material();
        material.specularReflectance.setColor(color);
        material.ior = 1.3f;
        material.refractive = true;
        return material;
    }
    
    public static Material createGlossy(Color color)
    {
        Material material = new Material();
        material.phongReflectance.setColor(color);
        return material;
    }
        
    public Material copy()
    {
        Material material = new Material();
        
        //reflectance and emission color
        material.diffuseReflectance.setColor(diffuseReflectance);
        material.phongReflectance.setColor(phongReflectance);
        material.specularReflectance.setColor(specularReflectance);
        material.emission.setColor(emission);
        
        //power, phong exponent & ior
        material.power = power;
        material.phongExponent = phongExponent;
        material.ior = ior;
        
        //sky portal
        material.skyportal = skyportal;        
        material.emitter = emitter;
        material.refractive = refractive;
        
        //texture
        material.setTexture(texture);
        
        //name of material
        material.name = name;
        
        return material;
    }
    
    public void setMaterial(Material material)
    {
        diffuseReflectance.setColor(material.diffuseReflectance);
        phongReflectance.setColor(material.phongReflectance);
        specularReflectance.setColor(material.specularReflectance);
        emission.setColor(material.emission);
        
        power = material.power;
        phongExponent = material.phongExponent;
        ior = material.ior;
        
        skyportal = material.skyportal;        
        emitter = material.emitter;
        refractive = material.refractive;
        
        setTexture(material.getTexture());       
        
        name = material.name;
    }
    
    public AbstractBSDF getBSDF(Normal3f worldNormal, Vector3f worldWi)
    {
        Frame frame = new Frame();
        Vector3f localWi = new Vector3f();
        
        //Init variables
        frame.setFromZ(worldNormal);
        if(worldWi != null)
            localWi = frame.toLocal(worldWi.neg()); //Vector is always facing away from surface (negated incident ray)
        
        //Calculate probabilities of choosing bsdf
        ComponentProbabilities probabilities = getComponentProbabilities(localWi.z);
        
        //Select bsdf randomly
        float random = Rng.getFloat();
        
        //bsdf
        AbstractBSDF bsdf;
        
        if(random < probabilities.diffProb)
        {
            //return diffuse bsdf
            bsdf =  new Diffuse(diffuseReflectance, frame, localWi);
        }
        else if(random < probabilities.diffProb + probabilities.phongProb)
        {
            //return phong bsdf
            bsdf =  new Phong(phongReflectance, phongExponent, frame, localWi);
        }
        else if(random < probabilities.diffProb + probabilities.phongProb + probabilities.reflProb)
        {
            //return reflect bsdf
            bsdf =  new Reflection(specularReflectance, frame, localWi);
        }
        else
        {
            //return refract bsdf
            bsdf =  new Refraction(specularReflectance, frame, localWi, ior);
        }   
        
        //set continuationProbability
        bsdf.continuationProbability = probabilities.continuationProbability;
        
        return bsdf;
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
        return refractive ? 1.f : 0.f;
    }
    
    public Texture getTexture()
    {
        return texture;
    }
    
    public void setTexture(Texture texture)
    {
        this.texture = texture;
    }
    
    public boolean hasTexture()
    {
        return texture != null;
    }
    
    public boolean isEmitter()
    {
        return emitter && !emission.isBlack();
    }
    
    public Color getPoweredEmission()
    {
        return emission.mul(power);
    }
    
    @Override
    public String toString()
    {
        return name;
    }
        
    public ComponentProbabilities getComponentProbabilities(float cosThetaWi)
    {        
        ComponentProbabilities probabilities = new ComponentProbabilities();
        
        
        float reflectCoeff;
        
        if(refractive)
            //reflectCoeff = Utility.fresnelDielectric(cosThetaWi, ior);
            reflectCoeff = Utility.fresnelSchlick(1, ior, cosThetaWi);
        else
            reflectCoeff = 1;

        float albedoDiffuse = albedoDiffuse();
        float albedoPhong   = albedoPhong();
        float albedoReflect = reflectCoeff          * albedoReflect();
        float albedoRefract = (1.f - reflectCoeff)  * albedoRefract();
        
        //if(refractive) System.out.println(albedoRefract);

        float totalAlbedo = albedoDiffuse + albedoPhong + albedoReflect + albedoRefract;
        
        probabilities.diffProb  = albedoDiffuse / totalAlbedo;
        probabilities.phongProb = albedoPhong   / totalAlbedo;
        probabilities.reflProb  = albedoReflect / totalAlbedo;
        probabilities.refrProb  = albedoRefract / totalAlbedo;
        
        // The continuation probability is max component from reflectance.
        // That way the weight of sample will never rise.
        // Luminance is another very valid option.
        probabilities.continuationProbability =
                     diffuseReflectance.add(phongReflectance.add(specularReflectance.mul(reflectCoeff))).max() + (1.f - reflectCoeff);                
        probabilities.continuationProbability = Math.min(1.f, Math.max(0.f, probabilities.continuationProbability));  
        
        return probabilities;
    }
    
    public static class ComponentProbabilities
    {
        float diffProb;
        float phongProb;
        float reflProb;
        float refrProb;
        
        float continuationProbability;
        
        @Override
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
        
            builder.append("diffProb  : ").append(diffProb).append("\n");
            builder.append("phongProb : ").append(phongProb).append("\n");
            builder.append("reflProb  : ").append(reflProb).append("\n");
            builder.append("refrProb  : ").append(refrProb).append("\n");
            
            return builder.toString();
        }
    }
}
