/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.scene;

import core.AbstractScene;
import core.Camera;
import core.LightCache;
import core.AbstractMaterial;
import core.accelerator.NullAccelerator;
import core.coordinates.Normal3f;
import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import core.material.UnitMaterial;
import core.math.Color;
import core.primitive.Geometries;
import core.shape.Sphere;
import core.shape.Triangle;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public final class CornellScene extends AbstractScene
{
    Point3f p1 = new Point3f(-1, -1, 1);
    Point3f p2 = new Point3f(-1, 1, 1);
    Point3f p3 = new Point3f(-1, 1, -1);
    Point3f p4 = new Point3f(-1, -1, -1);
    Point3f p5 = new Point3f(1, -1, 1);
    Point3f p6 = new Point3f(1, 1, 1);
    Point3f p7 = new Point3f(1, 1, -1);
    Point3f p8 = new Point3f(1, -1, -1);
    
    
    public CornellScene()
    {
        init();
    }
    
    @Override
    public void init() 
    {        
        camera = new Camera(new Point3f(0, 0, 4), new Point3f(), new Vector3f(0, 1, 0), 500, 500, 45);
        accelerator = new NullAccelerator();
        primitives = new ArrayList<>();
        lights = new LightCache();
        
        AbstractMaterial leftM = UnitMaterial.createLambert(new Color(0.3f, 0.3f, 0.9f));
        leftM.setName("Left");
        AbstractMaterial rightM = UnitMaterial.createLambert(new Color(0.9f, 0.3f, 0.3f));
        rightM.setName("Right");
        AbstractMaterial topM = UnitMaterial.createLambert(new Color(0.9f, 0.9f, 0.9f));
        topM.setName("Top");
        AbstractMaterial bottomM = UnitMaterial.createLambert(new Color(0.9f, 0.9f, 0.9f));
        bottomM.setName("Bottom");
        AbstractMaterial backM = UnitMaterial.createLambert(new Color(0.9f, 0.9f, 0.9f));
        backM.setName("Back");
        AbstractMaterial emissionM = UnitMaterial.createEmission();
        emissionM.setName("Emission");
        AbstractMaterial glassM = UnitMaterial.createGlass(new Color(0.9f, 0.9f, 0.9f));
        glassM.setName("Glass");
        AbstractMaterial mirrorM = UnitMaterial.createMirror(new Color(0.9f, 0.9f, 0.9f));
        mirrorM.setName("Mirror");
        
        //left
        Geometries left = new Geometries(leftM);
        left.addGeometryPrimitive(new Triangle(p1, p2, p3, new Normal3f(1, 0, 0)));
        left.addGeometryPrimitive(new Triangle(p1, p3, p4, new Normal3f(1, 0, 0)));
        left.init();
        primitives.add(left);
        
        //right
        Geometries right = new Geometries(rightM);        
        right.addGeometryPrimitive(new Triangle(p5, p6, p7, new Normal3f(-1, 0, 0)));
        right.addGeometryPrimitive(new Triangle(p5, p7, p8, new Normal3f(-1, 0, 0)));
        right.init();
        primitives.add(right);
        
        //top
        Geometries top = new Geometries(topM);        
        top.addGeometryPrimitive(new Triangle(p2, p3, p7, new Normal3f(0, -1, 0)));
        top.addGeometryPrimitive(new Triangle(p2, p6, p7, new Normal3f(0, -1, 0)));
        top.init();
        primitives.add(top);
        
        //bottom        
        Geometries bottom = new Geometries(bottomM);
        bottom.addGeometryPrimitive(new Triangle(p1, p4, p8, new Normal3f(0, 1, 0)));
        bottom.addGeometryPrimitive(new Triangle(p1, p5, p8, new Normal3f(0, 1, 0)));
        bottom.init();
        primitives.add(bottom);
        
        //back
        Geometries back = new Geometries(backM);        
        back.addGeometryPrimitive(new Triangle(p4, p3, p7, new Normal3f(0, 0, 1)));
        back.addGeometryPrimitive(new Triangle(p4, p8, p7, new Normal3f(0, 0, 1)));
        back.init();
        primitives.add(back);
        
        //light
        float disp = 0.05f;
        float scale = 0.2f;
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
        
        //sphere1 right
        Geometries sphere1 = new Geometries(glassM);          
        sphere1.addGeometryPrimitive(new Sphere(new Point3f(-0.45f, 0.6f, 0.4f), 0.3f));
        sphere1.init();
        primitives.add(sphere1);

        //sphere2 left
        Geometries sphere2 = new Geometries(glassM);              
        sphere2.addGeometryPrimitive(new Sphere(new Point3f(-0.5f, -0.6f, -0.4f), 0.4f));
        sphere2.init();
        primitives.add(sphere2);              
        
        accelerator.setPrimitives(primitives);
    }
    
}
