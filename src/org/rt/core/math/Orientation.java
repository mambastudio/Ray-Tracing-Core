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
package org.rt.core.math;

import org.rt.core.Camera;
import org.rt.core.coordinates.Point3f;
import org.rt.core.coordinates.Vector3f;
import static org.rt.core.math.Utility.sqrtf;
import static java.lang.Math.acos;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 *
 * @author user
 */
public class Orientation {
    private static float maxExt;
    
    public static void repositionLocation(Camera camera, BoundingBox bound)
    {
        Point3f center = bound.getCenter();
        float hypotenuseDist = center.distanceTo(camera.position);
        Vector3f toCenter = Point3f.sub(center, camera.position).normalize();
        
        float cosTheta = Vector3f.dot(toCenter, camera.forward());
        float adjacent = cosTheta * hypotenuseDist;
        
        Point3f newPoint = new Point3f();
        newPoint.x = camera.forward().x * adjacent + camera.position.x;
        newPoint.y = camera.forward().y * adjacent + camera.position.y;
        newPoint.z = camera.forward().z * adjacent + camera.position.z;
        
        Vector3f translation = Point3f.sub(center, newPoint);
        
        Transform translate = Transform.translate(translation);
        translate.transformAssign(camera.lookat);
        translate.transformAssign(camera.position);        
    }
    
    public static void reposition(Camera camera, BoundingBox bound)
    {
        Point3f center = bound.getCenter();
        float distance = bound.minimum.distanceTo(bound.maximum);
        
        camera.position.x = center.x;
        camera.position.y = center.y;
        camera.position.z = center.z - 1;
        
        camera.lookat.x = center.x;
        camera.lookat.y = center.y;
        camera.lookat.z = center.z;
        
        camera.up.x = 0;
        camera.up.y = 1;
        camera.up.z = 0;
        
        distance(camera, distance);
    }
    
    public static void rotateY(Camera camera, float angle)
    {
        Transform toOrigin = Transform.translate(camera.lookat.asVector().neg());
        
        //Transform camera to origin for proper tranform (i.e. rotation, translation)
        toOrigin.transformAssign(camera.up);
        toOrigin.transformAssign(camera.position);
        
        //TODO : transform camera
        Vector3f look = Point3f.sub(camera.lookat, camera.position);
        Vector3f Du   = Vector3f.cross(look, camera.up).normalize();
        Transform transform = Transform.rotate(angle, Du);
        transform.transformAssign(camera.up);
        transform.transformAssign(camera.position);
        
        //Untransform camera
        toOrigin.inverse().transformAssign(camera.up);
        toOrigin.inverse().transformAssign(camera.position);
    }
    
    public static void rotateX(Camera camera, float angle)
    {           
        Transform toOrigin = Transform.translate(camera.lookat.asVector().neg()); 
                
        //Transform camera to origin for proper tranform (i.e. rotation, translation)               
        toOrigin.transformAssign(camera.position);
        
        //TODO : transform camera        
        Transform transform = Transform.rotate(angle, camera.up);  
        transform.transformAssign(camera.position);
        
        //Untransform camera        
        toOrigin.inverse().transformAssign(camera.position);        
    }
    
    public static void distance(Camera camera, float distance)
    {
        Transform toOrigin = Transform.translate(camera.lookat.asVector().neg());        
        
        //Transform camera to origin for proper tranform (i.e. rotation, translation)
        toOrigin.transformAssign(camera.up);
        toOrigin.transformAssign(camera.position);
        
        //TODO : transform camera
        Vector3f sphCoord = sphericalCoordinates(camera.position.asVector());

        float p = sphCoord.x;
        float phi = sphCoord.y;
        float theta = sphCoord.z;

        float t = p + distance;
       
        if(t < 0.0f)
        {
            t = 0.00001f;
        }
        p = t;      
        
        Vector3f cartCoord = cartesianCoordinates(new Vector3f(p, phi, theta));

        camera.position.x = cartCoord.x;
        camera.position.y = cartCoord.y;
        camera.position.z = cartCoord.z;
        
        //Untransform camera
        toOrigin.inverse().transformAssign(camera.up);
        toOrigin.inverse().transformAssign(camera.position);        
        
        //System.out.println(camera);
    }
    
    public static void translateDistance(Camera camera, float distance)
    {
        float jump = maxExt * 0.01f;
        jump = Math.copySign(jump, distance);
        distance(camera, distance + jump);
    }
    
    //returns p, phi, theta from cartesian coordinates x, y, z
    public static Vector3f sphericalCoordinates(Vector3f coordinates)
    {
        float p, phi, theta;

        p = (float) Math.sqrt((coordinates.x * coordinates.x) + 
                              (coordinates.y * coordinates.y) + 
                              (coordinates.z * coordinates.z));
        
        Vector3f coord = new Vector3f(coordinates.x, coordinates.y, coordinates.z).normalize();
        
        phi = (float)acos(Vector3f.dot(coord, new Vector3f(0, 1, 0)));
        theta = (float)atan2(coord.z, coord.x);

        return new Vector3f(p, phi, theta);
    }

    //returns x, y, z from spherical coordinates p, phi, theta
    public static Vector3f cartesianCoordinates(Vector3f coordinates)
    {
        float x, y, z;
        float p = coordinates.x;
        float phi = coordinates.y;
        float theta = coordinates.z;

        x = (float) (p * sin(phi) * cos(theta));
        y = (float) (p * cos(phi));
        z = (float) (p * sin(phi) * sin(theta));

        return new Vector3f(x, y, z);
    }
}
