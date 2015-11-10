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
        
        cameraTest();
    }
    
    public static void cameraTest()
    {
        Frame frame = new Frame();
        System.out.println(frame.toWorld(new Vector3f()));
    }
}
