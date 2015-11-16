/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.coordinates.Normal3f;
import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import core.math.DifferentialGeometry;
import core.math.Frame;
import core.math.Matrix;
import core.math.Ray;
import core.math.Rng;
import core.math.Utility;
import core.shape.Sphere;
import core.shape.Triangle;

/**
 *
 * @author user
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Point3f p = new Point3f(1, 0, 0);
        Normal3f n = (Normal3f)p.subV(new Point3f());
        System.out.println(n);
    }
    
    public static void cameraTest()
    {
        float disp = 0.05f;
        float scale = 0.2f;
        Normal3f n = new Normal3f(0, -1, 0);
        
        Triangle triangle = new Triangle(new Point3f(-1 * scale, 1 - disp, 1 * scale),
                                    new Point3f(-1 * scale, 1 - disp, -1 * scale),
                                    new Point3f(1 * scale, 1 - disp, -1 * scale), n);
        
        for(int i = 0; i<10000000; i++)
        {
            Point3f p = triangle.sampleA(Rng.getFloat(), Rng.getFloat(), null);
            Vector3f v = Point3f.sub(p, new Point3f()).normalize();
            Ray r = new Ray(new Point3f(), v);
            
            triangle.pdfW(r.o, r.d);
        }
    }
}
