/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.shape;

import core.AbstractShape;
import core.coordinates.Normal3f;
import core.coordinates.Point2f;
import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import core.math.BoundingBox;
import core.math.DifferentialGeometry;
import core.math.Ray;
import core.math.Transform;

/**
 *
 * @author user
 */
public class Triangle2 extends AbstractShape
{
    private final TriangleMesh mesh;
    private final int[] v;
    int offset;
    
    public Triangle2(TriangleMesh m, int n)
    {
        super(new Transform(), new Transform());
        mesh = m;
        offset = 3 * n;
        v = mesh.vertexIndex;               
    }
    
    public Point3f getP1()
    {
        return mesh.p[v[offset + 0]];
    }
    
    public Point3f getP2()
    {
        return mesh.p[v[offset + 1]];
    }
    
    public Point3f getP3()
    {
        return mesh.p[v[offset + 2]];
    }
    
    public Normal3f getN1()
    {
        return mesh.n[v[offset + 0]];
    }
    
    public Normal3f getN2()
    {
        return mesh.n[v[offset + 1]];
    }
    
    public Normal3f getN3()
    {
        return mesh.n[v[offset + 2]];
    }
    
    public Point2f getUV1()
    {
        if(mesh.uvs != null)
            return new Point2f(mesh.uvs[2 * v[offset + 0]], mesh.uvs[2 * v[offset + 0] + 1]);
        else
            return new Point2f();
    }
    
    public Point2f getUV2()
    {
        if(mesh.uvs != null)
            return new Point2f(mesh.uvs[2 * v[offset + 1]], mesh.uvs[2 * v[offset + 1] + 1]);
        else
            return new Point2f(1, 0);
    }
    
    public Point2f getUV3()
    {
        if(mesh.uvs != null)
            return new Point2f(mesh.uvs[2 * v[offset + 2]], mesh.uvs[2 * v[offset + 2] + 1]);
        else
            return new Point2f(1, 1);
    }
    
    @Override
    public BoundingBox getObjectBounds() 
    {
        // Get triangle vertices in _p1_, _p2_, and _p3_       
        return BoundingBox.union(new BoundingBox(getP1(), getP2()), getP3());
    }

    @Override
    public boolean intersectP(Ray r) 
    {
        Vector3f e1, e2, h, s, q;
        double a, f, b1, b2;

        e1 = Point3f.sub(getP2(), getP1());
        e2 = Point3f.sub(getP3(), getP1());
        h = Vector3f.cross(r.d, e2);
        a = Vector3f.dot(e1, h);

        if (a > -0.00000000000001 && a < 0.0000000000001)
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

        if (a > -0.00000000000001 && a < 0.0000000000001)
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
            if(!hasVertexNormal())
            {
                dg.n = nhit;
            } 
            else
            {
                nhit = getNormal((float)(1d - b1 - b2), (float)b1, (float)b2);
                if(Vector3f.dot(nhit, r.d) > 0)                    
                    nhit = nhit.neg();
                dg.n = nhit;
            }
            
            Point2f uv = getUV((float)(1d - b1 - b2), (float)b1, (float)b2);  
            
            dg.u = uv.x;
            dg.v = uv.y;            
            dg.shape = this;            
            dg.nn = arbitraryNormal();
                        
            return true;
        }        
        return false;
    }
    
     public boolean hasVertexNormal()
    {
        return mesh.n != null;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Normal3f getNormal(Point3f p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        
    private Normal3f arbitraryNormal()
    {
        Vector3f e1 = Point3f.sub(getP2(), getP1());
        Vector3f e2 = Point3f.sub(getP3(), getP1());
        
        Normal3f nn = new Normal3f(Vector3f.cross(e1, e2).normalize());
        if(Vector3f.dot(getN1(), nn) < 0)
            return nn.neg();
        else
            return nn;
    }    
}
