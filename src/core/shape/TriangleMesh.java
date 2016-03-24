/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.shape;

import core.AbstractShape;
import core.coordinates.Normal3f;
import core.coordinates.Point3f;
import core.math.BoundingBox;
import core.math.DifferentialGeometry;
import core.math.Ray;
import core.math.Transform;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class TriangleMesh extends AbstractShape
{
    protected int ntris, nverts;    // number of triangles & vertices   
    protected int[] vertexIndex;    // pointer to an array of vertex indices
    protected Point3f[] p;          // array of nv vertex positions
    protected Normal3f[] n;         // an optional array of normal vectors, one per vertex in the mesh   
    protected float[] uvs;           // an optional array uv values, one of each vertex
    
    BoundingBox bounds = new BoundingBox();
    
    public TriangleMesh(int nt, int nv, int[] vi,
                 Point3f[] P, Normal3f[] N,
                 float[] uv)
    {
        super(new Transform(), new Transform());
        this.ntris = nt;
        this.nverts = nv;
        
        this.vertexIndex = new int[ntris*3];        
        System.arraycopy(vi,0,vertexIndex,0,ntris*3);
        // Copy _uv_, _N_, and _S_ vertex data, if present
        if (uv!=null) 
        {
            uvs = new float[2*nverts];
            System.arraycopy(uv,0,uvs,0,2*nverts);
        }
        else 
            uvs = null;
        p = new Point3f[nverts];
        if (N!=null) 
        {
            n = new Normal3f[nverts];
            System.arraycopy(N,0,n,0,nverts);
        }
        else n = null; 
        
        // Transform mesh vertices to world space
        
        for (int i = 0; i < nverts; ++i)
        {
            p[i] = P[i].clone();
            bounds.include(p[i]);
        }        
    }
    
    @Override
    public BoundingBox getWorldBounds() {
        return bounds;
    }

    @Override
    public BoundingBox getObjectBounds() {
        return bounds;
    }

    @Override
    public boolean intersectP(Ray ray) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean intersect(Ray r, DifferentialGeometry dg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float getArea() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Normal3f getNormal(Point3f p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void refine(ArrayList<AbstractShape> refined)
    {
        for (int i = 0; i < ntris; ++i)
            refined.add(new Triangle2(this, i));
    }
}
