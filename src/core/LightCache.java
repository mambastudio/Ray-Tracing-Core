/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.math.ExtendedList;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class LightCache {
    ExtendedList<AbstractLight> lightList = null;
    ExtendedList<AbstractPrimitive> arealightList = null;
    
    AbstractBackground backgroundLight = null;
    
    public LightCache()
    {
        lightList = new ExtendedList<>();
        arealightList = new ExtendedList<>();
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
    
    public void add(ArrayList<AbstractPrimitive> primitives)
    {
        for(AbstractPrimitive primitive: primitives)
            if(primitive.getMaterial().isEmitter())
                arealightList.add(primitive);
    }
    
    public AbstractLight getRandomLight()
    {
        return null;
    }
    
    public float getLightsPdf()
    {
        return 0f;
    }
    
    public int getSize()
    {
        return 0;
    }
}
