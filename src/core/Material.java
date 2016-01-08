/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.color.Color;

/**
 *
 * @author user
 */
public class Material {
    // diffuse is simply added to the others
    public Color diffuse;
    // Phong is simply added to the others
    public Color phong;
    public float phongExponent;

    // mirror can be either simply added, or mixed using Fresnel term
    // this is governed by mIOR, if it is >= 0, fresnel is used, otherwise
    // it is not
    public Color mirrorReflectance;
}
