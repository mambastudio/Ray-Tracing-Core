/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.light.DirectionalLight;
import core.math.ExtendedList;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class LightCache {
    ExtendedList<AbstractLight> lightList = null;    
    AbstractBackground backgroundLight = null;
    DirectionalLight directionalLight = null;
    
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
    
    public void addDirectionalLight()
    {
        if(directionalLight != null)
            if(!lightList.contains(directionalLight))
                lightList.add(directionalLight);
    }
    
    public void setBackgroundLight(AbstractBackground background)
    {
        this.backgroundLight = background;
    }
    
    public AbstractBackground getBackgroundLight()
    {
        return backgroundLight;
    }
    
    public void setDirectionalLight(DirectionalLight directionalLight)
    {
        this.directionalLight = directionalLight;
    }
    
    public DirectionalLight getDirectionalLight()
    {
        return directionalLight;
    }
    
    public boolean hasBackgroundLight()
    {
        return backgroundLight != null;
    }
    
    public boolean hasDirectionalLight()
    {
        return directionalLight != null;
    }
    
    public void add(AbstractPrimitive primitive)
    {       
        if(!primitive.getMaterial().isEmitter())
            return;        
        
        if(primitive.canIntersect())
        {
            lightList.add(primitive.getAreaLight());
            
        }
        else
        {
            ArrayList<AbstractPrimitive> list = new ArrayList<>();
            primitive.refine(list);
        
            for(AbstractPrimitive prim : list)
                lightList.add(prim.getAreaLight());
            
        }       
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
