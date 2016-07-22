/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.coordinates.Point2f;
import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import core.math.Matrix;
import core.math.Ray;
import core.math.Transform;

/**
 *
 * @author user
 */
public class Camera 
{
    public Point3f position;
    public Point3f lookat;
    public Vector3f up;
    
    public float fov;
    
    public Transform cameraTransform;   
    
    public float mImagePlaneDist;
   
    public Camera(Point3f position, Point3f lookat, Vector3f up, float horizontalFOV)
    {
        this.position = position.clone();
        this.lookat = lookat.clone();
        this.up = up.clone();        
        this.fov = horizontalFOV;        
        this.cameraTransform = new Transform();
    }
    
    public void setUp()
    {
        Vector3f d = Vector3f.normalize(lookat.subV(position));
        Vector3f r = Vector3f.normalize(Vector3f.cross(d, up));
        Vector3f u      = Vector3f.normalize(Vector3f.cross(r, d));
                
        Matrix e = Matrix.identity();
        e.set(0, 3, -position.x);
        e.set(1, 3, -position.y);
        e.set(2, 3, -position.z);
                
        Matrix eInv = Matrix.identity();
        eInv.set(0, 3, position.x);
        eInv.set(1, 3, position.y);
        eInv.set(2, 3, position.z);
        
        Matrix viewToWorld = Matrix.identity();
        viewToWorld.setRow(0, r.x, u.x, -d.x, 0);
        viewToWorld.setRow(1, r.y, u.y, -d.y, 0);
        viewToWorld.setRow(2, r.z, u.z, -d.z, 0);
        
        Matrix worldToView = viewToWorld.transpose();     
        Matrix mV = worldToView.mul(e);        
        Matrix mV_Inv = eInv.mul(viewToWorld);
                                        
        cameraTransform.m = mV;
        cameraTransform.mInv = mV_Inv;                      
    }
    
    public Ray getFastRay(float x, float y, float xRes, float yRes)
    {        
        float fov = (float)Math.toRadians(this.fov);
        
        Vector3f look = lookat.subV(position);
        Vector3f Du = Vector3f.cross(look, up).normalize();
        Vector3f Dv = Vector3f.cross(look, Du).normalize();
       
        float fl = xRes / (2.0F * (float)Math.tan(0.5F * fov));
        
        Vector3f vp = look.normalize();
        vp.x = (vp.x * fl - 0.5F * (xRes * Du.x + yRes * Dv.x));
        vp.y = (vp.y * fl - 0.5F * (xRes * Du.y + yRes * Dv.y));
        vp.z = (vp.z * fl - 0.5F * (xRes * Du.z + yRes * Dv.z));
        
        Vector3f dir = new Vector3f(x * Du.x + y * Dv.x + vp.x, x * Du.y + y * Dv.y + vp.y, x * Du.z + y * Dv.z + vp.z).normalize();
        
        return new Ray(position.x, position.y, position.z, dir.x, dir.y, dir.z);
    }
    
    public Vector3f forward()
    {
        return lookat.subV(position).normalize();
    }
    
    public Point3f position()
    {
        return position;
    }
    
    public Ray generateRay(float x, float y, float xResolution, float yResolution)
    {
        float d = (float) (1./Math.tan(Math.toRadians(fov)/2));
        
        float a = xResolution/yResolution;
        float px = a * (2 * x/xResolution - 1);
        float py = -2 * y/yResolution + 1;
        float pz = -d;
        
        Vector3f rd = new Vector3f(px, py, pz).normalize();
        Point3f ro = new Point3f();
        
        cameraTransform.inverse().transformAssign(ro);
        cameraTransform.inverse().transformAssign(rd);
        
        return new Ray(ro, rd);
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        
        builder.append("Camera: ").append("\n");
        builder.append("         eye    ").append(String.format("(%.5f, %.5f, %.5f)", position.x, position.y, position.z)).append("\n");
        builder.append("         lookat ").append(String.format("(%.5f, %.5f, %.5f)", lookat.x, lookat.y, lookat.z)).append("\n");
        
        return builder.toString(); 
    }

    public boolean checkRaster(float x, float y, float xResolution, float yResolution) 
    {
        return x >= 0 && y >= 0 &&
            x < xResolution && y < yResolution;
    }
    
    public Point2f worldToRaster(Point3f aHitpoint, float xResolution, float yResolution)            
    {
        Point3f cHitpoint = cameraTransform.transform(aHitpoint);
        
        float d = (float) (1./Math.tan(Math.toRadians(fov)/2));
        float a = xResolution/yResolution;
        
        float xndc = d*cHitpoint.x/(-a*cHitpoint.z);
        float yndc = d*cHitpoint.y/(-cHitpoint.z);
        
        float xs = xResolution/2*xndc + xResolution/2;
        float ys = -yResolution/2*yndc + yResolution/2;
        
        return new Point2f(xs, ys);
    }
    
    public Camera copy()
    {
        Camera camera = new Camera(position, lookat, up, fov);
        camera.setUp();
        return camera;
    }
}
