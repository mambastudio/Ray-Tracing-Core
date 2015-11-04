/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.math.ExtendedList;
import core.primitive.Geometries;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class LightCache {
    ExtendedList<AbstractLight> lightList = null;
    
    AbstractBackground backgroundLight = null;
    
    public LightCache()
    {
        lightList = new ExtendedList<>();       
    }
    
    public void clear()
    {
        lightList.removeAll();
    }
    
    public void addBackgroundLight()
    {
        if(backgroundLight != null)
            if(!lightList.contains(backgroundLight))
                lightList.add(backgroundLight);
                
    }
    public void setBackgroundLight(AbstractBackground background)
    {
        this.backgroundLight = background;
    }
    
    public AbstractBackground getBackgroundLight()
    {
        return backgroundLight;
    }
    
    public boolean hasBackgroundLight()
    {
        return backgroundLight != null;
    }
    
    public void add(Geometries geometry)
    {
        if(!geometry.getMaterial().isEmitter())
            return;
        
        ArrayList<AbstractPrimitive> list = new ArrayList<>();
        geometry.refine(list);
        
        for(AbstractPrimitive prim : list)
            lightList.add(prim.getAreaLight());
    }
    
    public AbstractLight getRandomLight()
    {
        return lightList.getRandom();
    }
    
    public float getLightsPdf()
    {
        return 1f/lightList.size();
    }
    
    public int getSize()
    {
        return lightList.size();
    }
}
