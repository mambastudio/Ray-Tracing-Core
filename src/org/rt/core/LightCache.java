/* 
 * The MIT License
 *
 * Copyright 2016 user.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.rt.core;

import org.rt.core.math.ExtendedList;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class LightCache 
{
    //Full list of light used in scene
    ExtendedList<AbstractLight> lightList = null;    
        
    //Also includes the sunsky
    AbstractBackground backgroundLight = null;
  
    public LightCache()
    {
        lightList = new ExtendedList<>();         
    }
    
    public void clear()
    {
        lightList.removeAll();
    }
    
    public void init()
    {
        if(backgroundLight != null)
        {
            lightList.add(backgroundLight);
            
            if(backgroundLight.isCompound())
                lightList.addAll(backgroundLight.getLights());
        }
        
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
