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

import org.rt.core.coordinates.Point3f;
import org.rt.core.coordinates.Vector3f;
import static org.rt.core.math.Utility.PI_F;
import static org.rt.core.math.Utility.acosf;
import static org.rt.core.math.Utility.cosf;
import static org.rt.core.math.Utility.lerp;
import static org.rt.core.math.Utility.sinf;
import static org.rt.core.math.Utility.sphericalDirection;
import static org.rt.core.math.Utility.sqrtf;
import org.rt.core.shape.Quad;

/**
 *
 * @author user
 */
public class MonteCarlo 
{
    public final static Vector3f uniformSampleCone(float u1, float u2, float costhetamax) 
    {
        float costheta = (1.f - u1) + u1 * costhetamax;
        float sintheta = sqrtf(1.f - costheta * costheta);
        float phi = u2 * 2.f * PI_F;
        return new Vector3f(cosf(phi) * sintheta, sinf(phi) * sintheta, costheta);
    }
    
    public final static Vector3f uniformSampleCone(float u1, float u2, float costhetamax,
            Vector3f x, Vector3f y, Vector3f z) 
    {
        float costheta = lerp(u1, costhetamax, 1.f);
        float sintheta = sqrtf(1.f - costheta * costheta);
        float phi = u2 * 2.f * PI_F;
        
        return x.mul(cosf(phi) * sintheta).addAssign(y.mul(sinf(phi) * sintheta)).addAssign(
                z.mul(costheta));
    }
    public final static Point3f uniformSampleSphere(float u1, float u2) 
    {
        float phi = 2 * PI_F * u1;
        float theta = acosf(1 - 2 * u2);
        
        Vector3f dir = sphericalDirection(theta, phi);
        
        return new Point3f(dir.x, dir.y, dir.z);
    }
    
    public static Point3f uniformSampleQuad(float r1, float r2, Quad quad)
    {
        float prob1 = quad.getSideOneProbability();
        
        if(Rng.getFloat() < prob1)
        {
            return quad.sampleSideOne(r1, r2);
        }
        else
        {
            return quad.sampleSideTwo(r1, r2);
        }        
    }
    
    public static Point3f uniformSampleTriangle(float r1, float r2, Point3f p1, Point3f p2, Point3f p3)
    {
        float term = (float) Math.sqrt(r1);
        float u = 1.f - term;
        float v = r2 * term;
        
        Vector3f e1 = p2.subV(p1);
        Vector3f e2 = p3.subV(p1);
        
        Point3f p = new Point3f();
        p.set(p1.add(e1.mul(u)).add(e2.mul(v)));
        
        return p;
    }
     
    public static float areaTriangle(Point3f p1, Point3f p2, Point3f p3)
    {
        float a = p1.distanceTo(p2);
        float b = p1.distanceTo(p3);
        float c = p2.distanceTo(p3);
        
        float s = 0.5f*(a + b + c);
        
        return (float)Math.sqrt(s*(s-a)*(s-b)*(s-c));
    } 
}
