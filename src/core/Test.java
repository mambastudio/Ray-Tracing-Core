/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import core.math.DifferentialGeometry;
import core.math.Ray;
import core.shape.Sphere;

/**
 *
 * @author user
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Sphere sphere = new Sphere();
        
        DifferentialGeometry dg = new DifferentialGeometry();
        Point3f p = new Point3f(0.9f, 0, -1f);
        Vector3f v = new Vector3f(0, 0, 1);
        
        Ray ray = new Ray(p, v);        
        boolean intersect = sphere.intersect(ray, dg);
        
        System.out.println(dg);
        System.out.println(ray.getMax());
                
    }
}
