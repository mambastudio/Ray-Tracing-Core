/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.parser.data;

import core.color.Color;

/**
 *
 * @author user
 */
public class MaterialData 
{
    Color ambient;
    Color diffuse;
    Color specular;
    Color transmittance;
    Color emission;

    float shininess;
    float ior;      // index of refraction
    float dissolve; // 1 == opaque; 0 == fully transparent

    // illumination model (see http://www.fileformat.info/format/material/
    int illum;

    String ambient_texname;            // map_Ka
    String diffuse_texname;            // map_Kd
    String specular_texname;           // map_Ks
    String specular_highlight_texname; // map_Ns
    String bump_texname;               // map_bump, bump
    String displacement_texname;       // disp
    String alpha_texname;              // map_d

    String name;
}
