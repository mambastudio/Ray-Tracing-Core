/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.coordinates.Normal3f;
import core.coordinates.Point2f;
import core.coordinates.Point2i;
import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import core.light.Envmap;
import core.light.sky.Sunsky;
import core.math.Distribution2D;
import core.math.FloatValue;
import core.math.Ray;
import core.math.Rng;
import core.math.Utility;
import core.shape.Quad;
import core.shape.Triangle;

/**
 *
 * @author user
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    static boolean kwenda = false;
    
    public static void main(String[] args) 
    {
        Sunsky sunsky = new Sunsky();
        
        for(int i = 0; i<100; i++)
        {
           
        }
    }
    
    public static class Superthread
    {
        public void execute(Joe joe)
        {
            joe.run();
        }
    }
    
    public static interface Joe
    {
        
        public void run();
    }
    
    public static void testEnvMap()
    {       
        Envmap map = new Envmap();
        
        Point2i uv = map.sampleUV(null);
        System.out.println(map.getColor(uv));
        Vector3f dir = map.toDirection(uv);
        System.out.println(map.getColor(dir));
                
        
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
