/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.scene;

import core.AbstractMaterial;
import core.LightCache;
import core.Scene;
import core.accelerator.UniformGrid;
import core.color.Color;
import core.coordinates.Normal3f;
import core.coordinates.Point3f;
import core.light.Envmap;
import core.material.UnitMaterial;
import core.primitive.Geometries;
import core.shape.Quad;
import core.shape.Sphere;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class SphereScene extends Scene
{    
    Point3f p1 = new Point3f(-4, -0.3f, 4);    
    Point3f p2 = new Point3f(-4, -0.3f, -4);
    Point3f p4 = new Point3f(4, -0.3f, 4);    
    Point3f p3 = new Point3f(4, -0.3f, -4);
    
    public SphereScene()
    {
        init();
    }
    
    @Override
    public final void init()
    {
        accelerator = new UniformGrid();
        primitives = new ArrayList<>();
        lights = new LightCache();
        
        AbstractMaterial bottomM = UnitMaterial.createLambert(new Color(0.9f, 0.9f, 0.9f));
        bottomM.setName("Bottom");
        
        AbstractMaterial glassM = UnitMaterial.createLambert(new Color(0.9f, 0.9f, 0.9f));
        glassM.setName("Glass");        
        
        //bottom        
        Geometries bottom = new Geometries(bottomM);
        bottom.addGeometryPrimitive(new Quad(p1, p2, p3, p4, new Normal3f(0, 1, 0)));   
        bottom.init();
        primitives.add(bottom);
        
        //sphere1 right
        Geometries sphere1 = new Geometries(glassM);          
        sphere1.addGeometryPrimitive(new Sphere(new Point3f(0f, 0f, 0f), 0.3f));
        sphere1.init();
        primitives.add(sphere1);
        
        accelerator.setPrimitives(primitives);
       
    }
}
