/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
