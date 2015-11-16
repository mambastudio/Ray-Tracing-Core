/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.material;

import core.AbstractBSDF;
import core.AbstractMaterial;
import core.bsdf.Diffuse;
import core.bsdf.Phong;
import core.bsdf.Reflection;
import core.coordinates.Normal3f;
import core.coordinates.Vector3f;
import core.math.Color;

/**
 *
 * @author user
 */
public class UnitMaterial extends AbstractMaterial
{
    AbstractBSDF bsdf;
    
    public UnitMaterial() {}
    public UnitMaterial(AbstractBSDF bsdf)
    {
        this.bsdf = bsdf;
    }
    
    @Override
    public AbstractBSDF getBSDF(Normal3f worldNormal, Vector3f worldWi) {
        if(isEmitter())
            return null;
        
        bsdf.setUp(worldNormal, worldWi);
        return bsdf;
    }
    
    public static AbstractMaterial createLambert(Color color)
    {
        return new UnitMaterial(new Diffuse(color));
    }
    
    public static AbstractMaterial createEmission()
    {
        AbstractMaterial material = new UnitMaterial();
        material.setEmission(Color.WHITE);
        return material;
    }
    
    public static AbstractMaterial createMirror(Color color)
    {
        return new UnitMaterial(new Reflection(color));
    }
    
    public static AbstractMaterial createGlossy(Color color)
    {
        return new UnitMaterial(new Phong(color));
    }
}
