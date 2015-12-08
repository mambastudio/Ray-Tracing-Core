/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.accelerator.UniformGrid;
import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import core.math.BoundingBox;
import core.math.BoundingSphere;
import core.math.Ray;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class Scene 
{    
    public Camera camera = new Camera(new Point3f(0, 0, 4), new Point3f(), new Vector3f(0, 1, 0), 500, 500, 45);
    public AbstractAccelerator accelerator = null;
    public ArrayList<AbstractPrimitive> primitives = null;
    public LightCache lights = null;    
    
    public Scene()
    {
        lights = new LightCache();
    }
    
    public void init()
    {
        
    }
    
    public void setPrimitives(ArrayList<AbstractPrimitive> primitives)
    {
        this.primitives = primitives;
        this.accelerator = new UniformGrid(primitives);
    }
    
    public boolean intersect(Ray ray, Intersection isect)
    {
        boolean hit = accelerator.intersect(ray, isect);
        
        return hit;
    }
    
    public boolean intersectP(Ray ray)
    {
        return accelerator.intersectP(ray);
    }
    
    public boolean occluded(Ray ray)
    {
        return accelerator.intersectP(ray);
    }
    
    public BoundingBox getWorldBounds()
    {
        return accelerator.getWorldBounds();
    }
    
    public BoundingSphere getBoundingSphere()
    {
        return getWorldBounds().getBoundingSphere();
    }
    
    public void initLights()
    {        
        lights.clear();
        
        for(AbstractPrimitive prim : primitives)
           lights.add(prim);
        
        lights.addBackgroundLight();
    }
}
