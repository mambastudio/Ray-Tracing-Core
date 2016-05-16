/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.parser;

import core.color.Color;
import core.coordinates.Normal3f;
import core.coordinates.Point2f;
import core.coordinates.Point3f;
import core.math.FloatArray;
import core.math.IntArray;
import core.shape.TriangleMesh;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class OBJParser2 
{
    public static ArrayList<Mesh_t> meshes = new ArrayList<>();
    public static ArrayList<Material_t> materials = new ArrayList<>();
    
    public static class Mesh_t
    {
        ArrayList<Point3f> positions;
        ArrayList<Normal3f> normals;
        ArrayList<Point2f> texcoords;
        ArrayList<Integer> indices;
        
        int number_vertices;            //number of vertices per face 
        
        String name;
    }
    
    public static class Material_t
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
}
