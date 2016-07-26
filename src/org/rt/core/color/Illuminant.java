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
public class Illuminant {
    public String name;
    public float xWhite, yWhite;

    public Illuminant(String name, float xWhite, float yWhite)
    {
        this.name = name;
        this.xWhite = xWhite;
        this.yWhite = yWhite;
    }
}
