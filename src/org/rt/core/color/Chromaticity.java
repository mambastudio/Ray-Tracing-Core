/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.color;

/**
 *
 * @author user
 */
public class Chromaticity {
    public float x, y, z;

    public Chromaticity()
    {
        x = y = z = 0f;
    }

    public Chromaticity(float x, float y, float z)
    {
        this.x = x; this.y = y; this.z = z;
    }

    @Override
    public String toString()
    {
        return String.format("(%.3f, %.3f, %.3f)", x, y, z);
    }
}
