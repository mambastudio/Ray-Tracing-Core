/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core;

import org.rt.core.math.DifferentialGeometry;

/**
 *
 * @author user
 */
public class Intersection {
    public AbstractBSDF bsdf = null;
    public DifferentialGeometry dg = new DifferentialGeometry();
    public AbstractPrimitive primitive = null;
        
    public AbstractPrimitive topPrimitive = null;
    
    public boolean isEmitter()
    {
        if(primitive == null) return false;        
        return primitive.getMaterial().isEmitter();
    }
}
