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
import core.math.MonteCarlo;
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
        
        float r1 = Rng.getFloat();
        float r2 = Rng.getFloat();
        
        Point3f v1 = new Point3f(-1, 0, 0);
        Point3f v2 = new Point3f(0, 1, 0);
        Point3f v3 = new Point3f(1, 0, 0);
        
        Point3f p1 = MonteCarlo.uniformSampleTriangle(r1, r2, v1, v2, v3);
        Point3f p2 = v1.mul(r1).add(v2.mul(r2).asVector()).add(v3.mul(1 - r1 - r2).asVector());
        
        System.out.println(p1 + " " +p2);
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
