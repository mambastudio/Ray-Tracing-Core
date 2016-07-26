/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.shape;

import org.rt.core.AbstractShape;
import org.rt.core.coordinates.Normal3f;
import org.rt.core.coordinates.Point2f;
import org.rt.core.coordinates.Point3f;
import org.rt.core.coordinates.Vector3f;
import org.rt.core.math.BoundingBox;
import org.rt.core.math.DifferentialGeometry;
import static org.rt.core.math.MonteCarlo.areaTriangle;
import static org.rt.core.math.MonteCarlo.uniformSampleTriangle;
import org.rt.core.math.Ray;
import org.rt.core.math.Transform;

/**
 *
 * @author user
 */
public class TriangleM extends AbstractShape
{
    TriangleMesh mesh;
    int offset;
    
    public TriangleM(TriangleMesh mesh, int index)
    {
        super(new Transform(), new Transform());
        this.mesh = mesh;
        this.offset = index * 3;
    }
    
    @Override
    public BoundingBox getObjectBounds() 
    {        
        BoundingBox bound = new BoundingBox();
        bound.include(getP1());
        bound.include(getP2());
        bound.include(getP3());
        return bound;
    }
    
    private Point3f getP1()
    {
        return TriangleMesh.p.get(mesh.vertexIndex.get(offset));
    }
    
    private Point3f getP2()
    {
        return TriangleMesh.p.get(mesh.vertexIndex.get(offset + 1));
    }
    
    private Point3f getP3()
    {
        return TriangleMesh.p.get(mesh.vertexIndex.get(offset + 2));
    }
    
    private Normal3f getN1()
    {
        return TriangleMesh.n.get(mesh.normalIndex.get(offset));
    }
    
    private Normal3f getN2()
    {
        return TriangleMesh.n.get(mesh.normalIndex.get(offset + 1));
    }
    
    private Normal3f getN3()
    {
        return TriangleMesh.n.get(mesh.normalIndex.get(offset + 2));
    }
    
    private Point2f getUV1()
    {        
        return TriangleMesh.uv.get(mesh.uvIndex.get(offset));
    }
    
    private Point2f getUV2()
    {
        return TriangleMesh.uv.get(mesh.uvIndex.get(offset + 1));
    }
    
    private Point2f getUV3()
    {
        return TriangleMesh.uv.get(mesh.uvIndex.get(offset + 2));
    }

    @Override
    public boolean intersectP(Ray r) {
        Vector3f e1, e2, h, s, q;
        double a, f, b1, b2;
        
        e1 = Point3f.sub(getP2(), getP1());
        e2 = Point3f.sub(getP3(), getP1());
        h = Vector3f.cross(r.d, e2);
        a = Vector3f.dot(e1, h);

        if (a > -0.0000001 && a < 0.0000001)
            return false;

        f = 1/a;
        s = Point3f.sub(r.o, getP1());
	b1 = f * (Vector3f.dot(s, h));

        if (b1 < 0.0 || b1 > 1.0)
            return false;

        q = Vector3f.cross(s, e1);
	b2 = f * Vector3f.dot(r.d, q);

	if (b2 < 0.0 || b1 + b2 > 1.0)
            return false;

	float t = (float) (f * Vector3f.dot(e2, q));
        
        return r.isInside(t);
    }

    @Override
    public boolean intersect(Ray r, DifferentialGeometry dg) {
        Vector3f e1, e2, h, s, q;
        double a, f, b1, b2;

        e1 = Point3f.sub(getP2(), getP1());
        e2 = Point3f.sub(getP3(), getP1());
        h = Vector3f.cross(r.d, e2);
        a = Vector3f.dot(e1, h);

        if (a > -0.0000001 && a < 0.0000001)
            return false;

        f = 1/a;
        
        s = Point3f.sub(r.o, getP1());
	b1 = f * (Vector3f.dot(s, h));

        if (b1 < 0.0 || b1 > 1.0)
            return false;

        q = Vector3f.cross(s, e1);
	b2 = f * Vector3f.dot(r.d, q);

	if (b2 < 0.0 || b1 + b2 > 1.0)
            return false;

	float t = (float) (f * Vector3f.dot(e2, q));

        if (r.isInside(t))
        {   
            Normal3f nhit;
            if(Vector3f.dot(arbitraryNormal(), r.d) < 0) 
                nhit = arbitraryNormal();   
            else
                nhit = arbitraryNormal().neg();
                
            r.setMax(t);
            dg.p = r.getPoint();
                       
            //Use normal mesh if there is
            if(mesh.hasNormal())
            {
                nhit = getNormal((float)(1d - b1 - b2), (float)b1, (float)b2);
                if(Vector3f.dot(nhit, r.d) > 0)                    
                    nhit = nhit.neg();
                dg.n = nhit;
            }
            else
            {
                dg.n = nhit;
            }
            
            //Use uv mesh if there is
            if(mesh.hasUV())
            {
                Point2f uv = getUV((float)(1d - b1 - b2), (float)b1, (float)b2);         
                dg.u = uv.x;
                dg.v = uv.y;
            }
            else
            {
                dg.u = (float) b1;
                dg.v = (float) b2;
            }
            
            dg.shape = this;            
            dg.nn = arbitraryNormal();
            
            //System.out.println(dg.p);
                        
            return true;
        }        
        return false;
    }
    
    private Normal3f arbitraryNormal()
    {
        Vector3f e1 = Point3f.sub(getP2(), getP1());
        Vector3f e2 = Point3f.sub(getP3(), getP1());
        
        return new Normal3f(Vector3f.cross(e1, e2).normalize());
    }
    
    
    public Normal3f getNormal(float b0, float b1, float b2)
    {
        Normal3f norm = new Normal3f();
        norm.x = getN1().x*b0 + getN2().x*b1 + getN3().x*b2;
        norm.y = getN1().y*b0 + getN2().y*b1 + getN3().y*b2;
        norm.z = getN1().z*b0 + getN2().z*b1 + getN3().z*b2;        
        return norm;
    }
    
    public Point2f getUV(float b0, float b1, float b2)
    {
        Point2f puv = new Point2f();
        puv.x = getUV1().x*b0 + getUV2().x*b1 + getUV3().x*b2;
        puv.y = getUV1().y*b0 + getUV2().y*b1 + getUV3().y*b2;
        return puv;
    }
   
    @Override
    public float getArea() {
        return areaTriangle(getP1(), getP2(), getP3());
    }

    @Override
    public Normal3f getNormal(Point3f p) {
        if(mesh.hasNormal())
            return this.getN1();
        else
            return arbitraryNormal();
    }
    
    @Override
    public Point3f sampleA(float u1, float u2, Normal3f n) 
    {
        Point3f p = uniformSampleTriangle(u1, u2, getP1(), getP2(), getP3());
        
        if(mesh.hasNormal())
            n.set(this.getN1());
        else
            n.set(arbitraryNormal());
        
        return p;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("p1 ").append(getP1()).append(" p2 ").append(getP2()).append(" p3 ").append(getP3());        
        return builder.toString();
    }
   
}
