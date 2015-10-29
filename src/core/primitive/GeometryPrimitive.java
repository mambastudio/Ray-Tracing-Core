/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.primitive;

import core.AbstractShape;
import core.Material;

/**
 *
 * @author user
 */
public class GeometryPrimitive {
    
    private final AbstractShape shape;
    private final Material material;
    
    public GeometryPrimitive(AbstractShape shape, Material material)
    {
        this.shape = shape;
        this.material = material;
    }
}
