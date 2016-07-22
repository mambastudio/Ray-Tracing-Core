/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.primitive;

import core.AbstractBSDF;
import core.AbstractPrimitive;
import core.Intersection;
import core.Material;
import core.coordinates.Normal3f;
import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import core.light.AreaLight;
import core.math.BoundingBox;
import core.math.Ray;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class GeometryMesh extends AbstractPrimitive
{
    protected int ntris, nverts;
    protected int[] vertexIndex;
    protected Point3f[] p;
    protected Normal3f[] n;
    protected Vector3f[] s;
    protected float[] uvs;   

    @Override
    public BoundingBox getWorldBounds() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean intersect(Ray ray, Intersection isect) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean intersectP(Ray ray) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AreaLight getAreaLight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Material getMaterial() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractBSDF getBSDF(Normal3f worldNormal, Vector3f worldWi) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void refine(ArrayList<AbstractPrimitive> refined) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
