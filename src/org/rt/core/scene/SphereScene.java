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
package org.rt.core.scene;

import java.util.ArrayList;
import org.rt.core.AbstractPrimitive;
import org.rt.core.AbstractSceneDescription;
import org.rt.core.Material;
import org.rt.core.color.Color;
import org.rt.core.coordinates.Point3f;
import org.rt.core.light.BackgroundLight;
import org.rt.core.primitive.Geometry;
import org.rt.core.shape.Sphere;

/**
 *
 * @author user
 */
public class SphereScene  extends AbstractSceneDescription
{
    
    @Override
    public ArrayList<AbstractPrimitive> getPrimitives()
    {
        ArrayList<AbstractPrimitive> primitives = new ArrayList<>();
        
        Material sphereM1 = Material.createLambert(new Color(0.9f, 0.3f, 0.3f));
        Material sphereM2 = Material.createLambert(new Color(0.3f, 0.9f, 0.3f));
        Material sphereM3 = Material.createLambert(new Color(0.9f, 0.3f, 0.9f));
        
        //sphere 
        Geometry sphere1 = new Geometry(sphereM1);          
        sphere1.addGeometryPrimitive(new Sphere(new Point3f(0.6f, 0.0f, 2f), 0.5f));        
        primitives.add(sphere1);
        
        //sphere 
        Geometry sphere2 = new Geometry(sphereM2);          
        sphere2.addGeometryPrimitive(new Sphere(new Point3f(0f, 0.0f, 0f), 0.5f));        
        primitives.add(sphere2);
        
        //sphere 
        Geometry sphere3 = new Geometry(sphereM3);          
        sphere3.addGeometryPrimitive(new Sphere(new Point3f(-0.9f, 0.0f, -2f), 0.5f));        
        primitives.add(sphere3);
        
        
        
        return primitives;
    }

    @Override
    public BackgroundLight getBackground() {
        return new BackgroundLight(Color.WHITE);
    }
}
