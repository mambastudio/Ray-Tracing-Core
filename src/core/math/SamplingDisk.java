/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.math;

import core.coordinates.Point3f;
import core.coordinates.Vector3;

/**
 *
 * @author user
 */
public class SamplingDisk 
{
    private float radius = 1;
    private Point3f pos = new Point3f();
    private final Frame frame = new Frame();
    
    public SamplingDisk()
    {
        
    }
    
    public SamplingDisk(float radius)
    {
        this.radius = radius;
    }
    
    public SamplingDisk(Vector3 dir)
    {
        frame.setFromZ(dir);
    }
}
