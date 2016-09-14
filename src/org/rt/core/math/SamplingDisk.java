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

import org.rt.core.coordinates.Point2f;
import org.rt.core.coordinates.Point3f;
import org.rt.core.coordinates.Vector3f;
import static org.rt.core.math.Utility.PI_F;
import static org.rt.core.math.Utility.cosf;
import static org.rt.core.math.Utility.sinf;
import static org.rt.core.math.Utility.sqrtf;
/**
 *
 * @author user
 */
public class SamplingDisk 
{
    private float radius = 1;    
    private final Frame frame = new Frame();
        
    public SamplingDisk(float radius)
    {
        this.radius = radius;
    }
    
    public SamplingDisk(Vector3f dir)
    {
        frame.setFromZ(dir);
    }
    
    public Point2f sampleDisk(float r1, float r2)
    {
        float r = sqrtf(r1);
        float theta = 2f * PI_F * r2;
        
        float x = r * cosf(theta);
        float y = r * sinf(theta);
                
        return new Point2f(x * radius, y * radius);
    }
    
    
}
