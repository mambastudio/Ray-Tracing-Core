/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.light.sky;

import core.coordinates.Vector3f;

/**
 *
 * @author user
 */
public class Atmosphere {
    
    public Atmosphere()
            {
    
    Vector3f    betaR;              //Rayleigh scattering coefficients at sea level
    Vector3f    betaM;              //Mie scattering coefficients at sea level
    float       Hr;                 //Rayleigh scale scattering
    float       Hm;                 //Mie scale scattering
    float       radiusEarth;        //Earth radius
    float       radiusAtmosphere;   //Atmospheric radius
    Vector3f    sunDirection;       //Sun direction
    float       sunIntensity;       //Sun intensity
    float       g;                  //Mean cosine
            }
}
