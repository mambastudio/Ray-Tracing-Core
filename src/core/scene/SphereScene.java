/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.scene;

import core.LightCache;
import core.Material;
import core.Scene;
import core.accelerator.UniformGrid;
import core.color.Color;
import core.coordinates.Normal3f;
import core.coordinates.Point3f;
import core.primitive.Geometries;
import core.shape.Quad;
import core.shape.Sphere;
import core.shape.Triangle;
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
        super(cameraWidth, cameraHeight);
        camera.position.z = 1.2f;
        
        camera.fov = 60;
        camera.position.y = -0.5f;
        camera.lookat.y   = -0.5f;
        
        init();
    }
    
    @Override
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
        Geometries back = new Geometries(backM); 
        back.addGeometryPrimitive(new Quad(p4, p3, p7, p8, new Normal3f(0, 0, 1)));
        back.init();
        primitives.add(back);
        
        //left
        Geometries left = new Geometries(leftM);
        left.addGeometryPrimitive(new Quad(p1, p2, p3, p4, new Normal3f(1, 0, 0)));        
        left.init();
        primitives.add(left);
        
        //right
        Geometries right = new Geometries(rightM);    
        right.addGeometryPrimitive(new Quad(p5, p6, p7, p8, new Normal3f(-1, 0, 0)));     
        right.init();
        primitives.add(right);
                
        //bottom        
        Geometries bottom = new Geometries(bottomM);
        bottom.addGeometryPrimitive(new Quad(p1, p4, p8, p5, new Normal3f(0, 1, 0)));   
        bottom.init();
        primitives.add(bottom);
        
        //sphere 
        Geometries sphere1 = new Geometries(sphereM);          
        sphere1.addGeometryPrimitive(new Sphere(new Point3f(0f, -0.5f, 0f), 0.5f));
        sphere1.init();
        primitives.add(sphere1);
                
        //light
        float disp = 0.05f;
        float scale = 0.9f;
        Normal3f n = new Normal3f(0, -1, 0);
        
        Geometries light = new Geometries(emissionM);        
        light.addGeometryPrimitive(new Triangle(new Point3f(-1 * scale, 1 - disp, 1 * scale),
                                    new Point3f(-1 * scale, 1 - disp, -1 * scale),
                                    new Point3f(1 * scale, 1 - disp, -1 * scale), n));
        light.addGeometryPrimitive(new Triangle(new Point3f(-1 * scale, 1 - disp, 1 * scale),
                                    new Point3f(1 * scale, 1 - disp, 1 * scale),
                                    new Point3f(1 * scale, 1 - disp, -1 * scale), n)); 
        
        light.init();
        primitives.add(light);
        
        accelerator.setPrimitives(primitives);
       
    }
    
    public Material getEditingMaterial()
    {
        return sphereM;
    }
}
