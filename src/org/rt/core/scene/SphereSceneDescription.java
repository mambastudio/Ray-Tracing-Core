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
public class SphereSceneDescription {
    
    public static ArrayList<AbstractPrimitive> getPrimitives()
    {
        ArrayList<AbstractPrimitive> primitives = new ArrayList<>();
        
        Point3f p1 = new Point3f(-1, -1, 1);
        Point3f p2 = new Point3f(-1, 1, 1);
        Point3f p3 = new Point3f(-1, 1, -1);
        Point3f p4 = new Point3f(-1, -1, -1);
        Point3f p5 = new Point3f(1, -1, 1);
        Point3f p6 = new Point3f(1, 1, 1);
        Point3f p7 = new Point3f(1, 1, -1);
        Point3f p8 = new Point3f(1, -1, -1);
        
        Material sphereM = Material.createLambert(new Color(0.9f, 0.9f, 0.9f));
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
        primitives.add(back);
        
        //left
        Geometry left = new Geometry(leftM);
        left.addGeometryPrimitive(new Quad(p1, p2, p3, p4, new Normal3f(1, 0, 0)));        
        primitives.add(left);
        
        //right
        Geometry right = new Geometry(rightM);    
        right.addGeometryPrimitive(new Quad(p5, p6, p7, p8, new Normal3f(-1, 0, 0)));             
        primitives.add(right);
                
        //bottom        
        Geometry bottom = new Geometry(bottomM);
        bottom.addGeometryPrimitive(new Quad(p1, p4, p8, p5, new Normal3f(0, 1, 0)));           
        primitives.add(bottom);
        
        //sphere 
        Geometry sphere1 = new Geometry(sphereM);          
        sphere1.addGeometryPrimitive(new Sphere(new Point3f(0f, -0.5f, 0f), 0.5f));        
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
        primitives.add(light);
        
        return primitives;
    }
}
