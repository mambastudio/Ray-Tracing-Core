/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.color;

import core.coordinates.Vector3f;

/**
 *
 * @author user
 */
public class Sky {
    public static class Atmosphere
    {
        public float hr;                    /// thickness of the atmosphere if density was uniform
        public float hm;                    /// same as Hr but for Mie scattering
        public float radiusEarth;           /// in the papers this is usually Rg or Re (radius ground, earth)
        public float radiusAtmosphere;      /// in the papers this is usually R or Ra (radius atmosphere)
        public Vector3f sundir;             /// sun dir
    }
}
