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

import java.util.ArrayList;
import org.rt.util.FloatArray;
import org.rt.util.IntArray;
import static org.rt.core.parser.data.MeshData.TriangleType.FACE;
import static org.rt.core.parser.data.MeshData.TriangleType.FACE_NORMAL;
import static org.rt.core.parser.data.MeshData.TriangleType.FACE_UV;
import static org.rt.core.parser.data.MeshData.TriangleType.FACE_UV_NORMAL;

/**
 *
 * @author user
 */
public class MeshData
{
    FloatArray points;
    FloatArray normals;
    FloatArray uvs;
    
    IntArray triangles;
        
    ArrayList<String> objects;
    ArrayList<String> groups;
    ArrayList<MaterialData> materials;
    
    String name;  
    
    public enum TriangleType {FACE, FACE_UV, FACE_NORMAL, FACE_UV_NORMAL, ALL};
    
    public MeshData()
    {
        points = new FloatArray();
        normals = new FloatArray();
        uvs = new FloatArray();
        
        triangles = new IntArray();
        
        objects = new ArrayList<>();
        groups = new ArrayList<>();
        materials = new ArrayList<>();
        
        name = null;
    }
    
    public int getFaceData(int index, int offset)
    {
        return triangles.get(index * 10 + offset);
    }
    
    public void addObject(String name)
    {
        objects.add(name);
    }
    
    public void addGroup(String name)            
    {
        groups.add(name);
    }
        
    public void addVertex(float x, float y, float z)
    {
        points.add(x, y, z);
    }
        
    public void addNormal(float x, float y, float z)
    {
        normals.add(x, y, z);
    }
    
    public void addTexCoord(float x, float y)
    {
        uvs.add(x, y);
    }
    
    public void addTriangle(TriangleType type, int... value)
    {
        if(type == FACE)           
            addTriangle(value[0], value[1], value[2], -1, -1, -1, -1, -1, -1, -1);
        else if(type == FACE_UV)
            addTriangle(value[0], value[1], value[2], value[3], value[4], value[5], -1, -1, -1, -1);
        else if(type == FACE_NORMAL)
            addTriangle(value[0], value[1], value[2], -1, -1, -1, value[3], value[4], value[5], -1);
        else if(type == FACE_UV_NORMAL)
            addTriangle(value[0], value[1], value[2], value[3], value[4], value[5], value[6], value[7], value[8], -1);
    }
        
    public void addTriangle(int vert1, int vert2, int vert3, int uv1, int uv2, int uv3, int norm2, int norm3, int norm1, int material)
    {
        triangles.add(vert1, vert2, vert3,  uv1, uv2, uv3, norm1, norm2, norm3, material);
    }
        
    public void trimAll()
    {
        points.trim();
        normals.trim();
        uvs.trim(); 
        triangles.trim();
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("objects = ").append(objects.size()).append("\n");
        builder.append("groups  = ").append(groups.size()).append("\n").append("\n");
        builder.append("points  = ").append(points.size()/3).append("\n");
        builder.append("normals = ").append(normals.size()/3).append("\n");
        builder.append("uvs     = ").append(uvs.size()/3).append("\n").append("\n");        
        builder.append("faces   = ").append(triangles.size()/10).append("\n");
        return builder.toString();
    }    
}
