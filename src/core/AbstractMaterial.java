/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.coordinates.Normal3f;
import core.coordinates.Vector3f;
import core.image.Color;

/**
 *
 * @author user
 */
public abstract class AbstractMaterial {
   
    protected String name = null;    
    protected Color emission = new Color();
    protected float power = 5f;
        
    public boolean isEmitter()
    {
        return !emission.isBlack();
    }
    
    public void setEmission(Color emission)
    {
        this.emission = emission;
    }
    
    public Color getEmission()
    {
        return emission.mul(power);
    }
    
    public void setPower(float power)
    {
        this.power = power;
    }
    
    public float getPower()
    {
        return power;
    }
        
    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }
    
    public abstract AbstractBSDF getBSDF(Normal3f worldNormal, Vector3f worldWi);
    
    @Override
    public String toString()
    {
        return getName();
    }    
}
