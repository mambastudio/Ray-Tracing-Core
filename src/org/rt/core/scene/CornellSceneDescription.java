/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.scene;

import java.util.ArrayList;
import org.rt.core.AbstractPrimitive;
import org.rt.core.Material;
import org.rt.core.color.Color;
import org.rt.core.coordinates.Normal3f;
import org.rt.core.coordinates.Point3f;
import org.rt.core.primitive.Geometry;
import org.rt.core.shape.Quad;
import org.rt.core.shape.Sphere;
import org.rt.core.shape.Triangle;

/**
 *
 * @author user
 */
public class CornellSceneDescription {
    private static final Point3f p1 = new Point3f(-1, -1, 1);
    private static final Point3f p2 = new Point3f(-1, 1, 1);
    private static final Point3f p3 = new Point3f(-1, 1, -1);
    private static final Point3f p4 = new Point3f(-1, -1, -1);
    private static final Point3f p5 = new Point3f(1, -1, 1);
    private static final Point3f p6 = new Point3f(1, 1, 1);
    private static final Point3f p7 = new Point3f(1, 1, -1);
    private static final Point3f p8 = new Point3f(1, -1, -1);
    
    public static ArrayList<AbstractPrimitive> getPrimitives()
    {
        ArrayList<AbstractPrimitive> primitives = new ArrayList<>();
        
        Material leftM = Material.createLambert(new Color(0.3f, 0.3f, 0.9f));
        leftM.name = "Left";
        Material rightM = Material.createLambert(new Color(0.9f, 0.3f, 0.3f));
        rightM.name = "Right";
        Material topM = Material.createLambert(new Color(0.9f, 0.9f, 0.9f));
        topM.name = "Top";
        Material bottomM = Material.createLambert(new Color(0.9f, 0.9f, 0.9f));
        bottomM.name = "Bottom";
        Material backM = Material.createLambert(new Color(0.9f, 0.9f, 0.9f));
        backM.name = "Back";
        Material emissionM = Material.createEmission();
        emissionM.name = "Emission";
        Material glassM = Material.createGlass(new Color(0.9f, 0.9f, 0.9f));
        glassM.name = "Glass";
        Material mirrorM = Material.createMirror(new Color(0.9f, 0.9f, 0.9f));
        mirrorM.name = "Mirror";
        
        
        //back
        Geometry back = new Geometry(backM); 
        back.addGeometryPrimitive(new Quad(p4, p3, p7, p8, new Normal3f(0, 0, 1)));        
        primitives.add(back);
        
        //left
        Geometry left = new Geometry(leftM);
        left.addGeometryPrimitive(new Quad(p1, p2, p3, p4, new Normal3f(1, 0, 0)));  
        primitives.add(left);
        
        //right
        Geometry right = new Geometry(rightM);    
        right.addGeometryPrimitive(new Quad(p5, p6, p7, p8, new Normal3f(-1, 0, 0)));   
        primitives.add(right);
        
        //top
        Geometry top = new Geometry(topM);      
        top.addGeometryPrimitive(new Quad(p2, p3, p7, p6, new Normal3f(0, -1, 0)));     
        primitives.add(top);
        
        //bottom        
        Geometry bottom = new Geometry(bottomM);
        bottom.addGeometryPrimitive(new Quad(p1, p4, p8, p5, new Normal3f(0, 1, 0))); 
        primitives.add(bottom);
                        
        //light
        float disp = 0.05f;
        float scale = 0.2f;
        Normal3f n = new Normal3f(0, -1, 0);
        
        Geometry light = new Geometry(emissionM);        
        light.addGeometryPrimitive(new Triangle(new Point3f(-1 * scale, 1 - disp, 1 * scale),
                                    new Point3f(-1 * scale, 1 - disp, -1 * scale),
                                    new Point3f(1 * scale, 1 - disp, -1 * scale), n));
        light.addGeometryPrimitive(new Triangle(new Point3f(-1 * scale, 1 - disp, 1 * scale),
                                    new Point3f(1 * scale, 1 - disp, 1 * scale),
                                    new Point3f(1 * scale, 1 - disp, -1 * scale), n));         
        
        primitives.add(light);
        
        //sphere1 right
        Geometry sphere1 = new Geometry(glassM);          
        sphere1.addGeometryPrimitive(new Sphere(new Point3f(-0.45f, 0.6f, 0.4f), 0.3f));        
        primitives.add(sphere1);

        //sphere2 left
        Geometry sphere2 = new Geometry(glassM);              
        sphere2.addGeometryPrimitive(new Sphere(new Point3f(-0.5f, -0.6f, -0.4f), 0.4f));        
        primitives.add(sphere2);    
        
        return primitives;
    }
}
