/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import core.math.DifferentialGeometry;
import core.math.Matrix;
import core.math.Ray;
import core.math.Utility;
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
        
        cameraTest();
    }
    
    public static void cameraTest()
    {
        Camera camera = new Camera(new Point3f(0, 0, 4), new Point3f(), new Vector3f(0, 1, 0), 500, 500, 45);
        camera.setUp();
        System.out.println(camera.generateRay(100, 100));
    }
}
