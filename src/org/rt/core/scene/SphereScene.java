/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.scene;

import org.rt.core.LightCache;
import org.rt.core.Material;
import org.rt.core.Scene;
import org.rt.core.accelerator.UniformGrid;
import org.rt.core.color.Color;
import org.rt.core.coordinates.Normal3f;
import org.rt.core.coordinates.Point3f;
import org.rt.core.primitive.Geometry;
import org.rt.core.shape.Quad;
import org.rt.core.shape.Sphere;
import org.rt.core.shape.Triangle;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class SphereScene extends Scene
{    
    Point3f p1 = new Point3f(-1, -1, 1);
    Point3f p2 = new Point3f(-1, 1, 1);
    Point3f p3 = new Point3f(-1, 1, -1);
    Point3f p4 = new Point3f(-1, -1, -1);
    Point3f p5 = new Point3f(1, -1, 1);
    Point3f p6 = new Point3f(1, 1, 1);
    Point3f p7 = new Point3f(1, 1, -1);
    Point3f p8 = new Point3f(1, -1, -1);
    
    public Material sphereM = null;
    
    public SphereScene()
    {
        super();
        camera.position.z = 2;
        init();
    }
    
    public SphereScene(int cameraWidth, int cameraHeight)
    {        
        camera.position.z = 1.2f;
        
        camera.fov = 60;
        camera.position.y = -0.5f;
        camera.lookat.y   = -0.5f;
        
        init();
    }
    
    public final void init()
    {
        accelerator = new UniformGrid();
        primitives = new ArrayList<>();
        lights = new LightCache();
                
        sphereM      = Material.createLambert(new Color(0.9f, 0.9f, 0.9f));
        sphereM.name = "Glass";    
        
        Material emissionM = Material.createEmission();
        emissionM.name = "Emission";
        
        Material leftM = Material.createLambert(new Color(0.3f, 0.3f, 0.9f));
        leftM.name = "Left";
        Material rightM = Material.createLambert(new Color(0.9f, 0.3f, 0.3f));
        rightM.name = "Right";       
        Material bottomM = Material.createLambert(new Color(0.9f, 0.9f, 0.9f));
        bottomM.name = "Bottom";
        Material backM = Material.createLambert(new Color(0.9f, 0.9f, 0.9f));
        backM.name = "Back";       
        
        //back
        Geometry back = new Geometry(backM); 
        back.addGeometryPrimitive(new Quad(p4, p3, p7, p8, new Normal3f(0, 0, 1)));
        back.build();
        primitives.add(back);
        
        //left
        Geometry left = new Geometry(leftM);
        left.addGeometryPrimitive(new Quad(p1, p2, p3, p4, new Normal3f(1, 0, 0)));        
        left.build();
        primitives.add(left);
        
        //right
        Geometry right = new Geometry(rightM);    
        right.addGeometryPrimitive(new Quad(p5, p6, p7, p8, new Normal3f(-1, 0, 0)));     
        right.build();
        primitives.add(right);
                
        //bottom        
        Geometry bottom = new Geometry(bottomM);
        bottom.addGeometryPrimitive(new Quad(p1, p4, p8, p5, new Normal3f(0, 1, 0)));   
        bottom.build();
        primitives.add(bottom);
        
        //sphere 
        Geometry sphere1 = new Geometry(sphereM);          
        sphere1.addGeometryPrimitive(new Sphere(new Point3f(0f, -0.5f, 0f), 0.5f));
        sphere1.build();
        primitives.add(sphere1);
                
        //light
        float disp = 0.05f;
        float scale = 0.9f;
        Normal3f n = new Normal3f(0, -1, 0);
        
        Geometry light = new Geometry(emissionM);        
        light.addGeometryPrimitive(new Triangle(new Point3f(-1 * scale, 1 - disp, 1 * scale),
                                    new Point3f(-1 * scale, 1 - disp, -1 * scale),
                                    new Point3f(1 * scale, 1 - disp, -1 * scale), n));
        light.addGeometryPrimitive(new Triangle(new Point3f(-1 * scale, 1 - disp, 1 * scale),
                                    new Point3f(1 * scale, 1 - disp, 1 * scale),
                                    new Point3f(1 * scale, 1 - disp, -1 * scale), n)); 
        
        light.build();
        primitives.add(light);
        
        accelerator.build(primitives);
       
    }
    
    public Material getEditingMaterial()
    {
        return sphereM;
    }
}
