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
package org.rt.core.parser.data;

import org.rt.core.math.FloatArray;
import org.rt.core.math.IntArray;

/**
 *
 * @author user
 */
public class MeshData
{
    FloatArray positions;
    FloatArray normals;
    FloatArray texcoords;
    IntArray indices;
    
    String name;
    
    public MeshData()
    {
        positions = new FloatArray();
        normals = new FloatArray();
        texcoords = new FloatArray();
        indices = new IntArray();
        
        name = null;
    }
        
    public void addPosition(float x, float y, float z)
    {
        positions.add(x, y, z);
    }
        
    public void addNormal(float x, float y, float z)
    {
        normals.add(x, y, z);
    }
    
    public void addTexCoord(float x, float y)
    {
        texcoords.add(x, y);
    }
    
    public void trimAll()
    {
        positions.trim();
        normals.trim();
        texcoords.trim();
        indices.trim();
    }
    
    @Override
    public String toString()
    {
        return name;
    }
}
