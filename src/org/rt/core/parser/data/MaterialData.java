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

import org.rt.core.color.Color;

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
