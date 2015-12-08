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
import static core.math.MonteCarlo.areaTriangle;
import static core.math.MonteCarlo.uniformSampleTriangle;

/**
 *
 * @author user
 */
public class Triangle extends AbstractShape
{
    Point3f p1, p2, p3;
    Normal3f n1, n2, n3;    
    Point2f uv1, uv2, uv3;
    
    public Normal3f n;
    
    public Triangle(Point3f p1, Point3f p2, Point3f p3, Normal3f n)
    {        
        super(new Transform(), new Transform());
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;  
        this.n = new Normal3f(n.normalize());
        
        n1 = n2 = n3 = null;
        
    }
    
    public Triangle(Point3f p1, Point3f p2, Point3f p3, 
            Normal3f n1, Normal3f n2, Normal3f n3)
    {
        super(new Transform(), new Transform());
        
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;  
        
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;  
        
        //for light sampling
        Normal3f nn = arbitraryNormal();
        if(Vector3f.dot(n1, nn) < 0)
            n = nn.neg();
        else
            n = nn;
    }
    
    public Triangle(Point3f p1, Point3f p2, Point3f p3, 
            Normal3f n1, Normal3f n2, Normal3f n3, 
            Point2f uv1, Point2f uv2, Point2f uv3)
    {
        super(new Transform(), new Transform());
        
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;  
        
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;  
        
        this.uv1 = uv1;
        this.uv2 = uv2;
        this.uv3 = uv3;    
        
        
        //for light sampling
        Normal3f nn = arbitraryNormal();
        if(Vector3f.dot(n1, nn) < 0)
            n = nn.neg();
        else
            n = nn;
    }
    
    
    @Override
    public BoundingBox getObjectBounds() {
        BoundingBox bound = new BoundingBox();
        bound.include(p1);
        bound.include(p2);
        bound.include(p3);
        return bound;
    }

    @Override
    public boolean intersectP(Ray r) {
        Vector3f ao = p1.subV(r.o);
        Vector3f bo = p2.subV(r.o);
        Vector3f co = p3.subV(r.o);
        
        Vector3f v0 = Vector3f.cross(co, bo);
        Vector3f v1 = Vector3f.cross(bo, ao);
        Vector3f v2 = Vector3f.cross(ao, co);

        float v0d = Vector3f.dot(v0, r.d);
        float v1d = Vector3f.dot(v1, r.d);
        float v2d = Vector3f.dot(v2, r.d);
        
        if(((v0d < 0.f)  && (v1d < 0.f)  && (v2d < 0.f)) ||
           ((v0d >= 0.f) && (v1d >= 0.f) && (v2d >= 0.f)))
        {
            float distance = Vector3f.dot(n, ao) / Vector3f.dot(n, r.d);
            
            if((distance > r.getMin()) & (distance < r.getMax()))
            {              
                return true;
            }
        }
        
        return false;
    }

    @Override
    public boolean intersect(Ray r, DifferentialGeometry dg) {
        Vector3f e1, e2, h, s, q;
        double a, f, b1, b2;

        e1 = Point3f.sub(p2, p1);
        e2 = Point3f.sub(p3, p1);
        h = Vector3f.cross(r.d, e2);
        a = Vector3f.dot(e1, h);

        if (a > -0.00000000000001 && a < 0.0000000000001)
            return false;

        f = 1/a;
        s = Point3f.sub(r.o, p1);
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
            if(Vector3f.dot(n, r.d) < 0) 
                nhit = n;   
            else
                nhit = n.neg();
                
            r.setMax(t);
            dg.p = r.getPoint();
            if(!hasVertexNormal())
            {
                dg.n = nhit;
            } 
            else
            {
                dg.n = getNormal((float)(1d - b1 - b2), (float)b1, (float)b2);
            }
            if(nullUV())
            {
                dg.u = (float) b1;
                dg.v = (float) b2;
            }
            else
            {
                Point2f uv = getUV((float)(1d - b1 - b2), (float)b1, (float)b2);         
                dg.u = uv.x;
                dg.v = uv.y;
            }
            dg.shape = this;
                        
            return true;
        }        
        return false;
    }

    @Override
    public float getArea() 
    {
        return areaTriangle(p1, p2, p3);
    }
    
    @Override
    public Point3f sampleA(float u1, float u2, Normal3f n) 
    {
        if(n != null)
            n.set(this.n);
        
        return uniformSampleTriangle(u1, u2, p1, p2, p3);
    }

    @Override
    public Normal3f getNormal(Point3f p) {
        return n.clone();
    }
    
    public boolean hasVertexNormal()
    {
        return n1 != null || n2 != null || n3 != null;
    }
    
    public boolean nullUV()
    {
        return uv1 == null || uv2 == null || uv3 == null;
    }
       
    private Normal3f arbitraryNormal()
    {
        Vector3f e1 = Point3f.sub(p2, p1);
        Vector3f e2 = Point3f.sub(p3, p1);
        
        return new Normal3f(Vector3f.cross(e1, e2).normalize());
    }
    
    public Normal3f getNormal(float b0, float b1, float b2)
    {
        Normal3f norm = new Normal3f();
        norm.x = n1.x*b0 + n2.x*b1 + n3.x*b2;
        norm.y = n1.y*b0 + n2.y*b1 + n3.y*b2;
        norm.z = n1.z*b0 + n2.z*b1 + n3.z*b2;        
        return norm;
    }
    
    public Point2f getUV(float b0, float b1, float b2)
    {
        Point2f puv = new Point2f();
        puv.x = uv1.x*b0 + uv2.x*b1 + uv3.x*b2;
        puv.y = uv1.y*b0 + uv2.y*b1 + uv3.y*b2;
        return puv;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        
        builder.append("triangle ").append("p ").append(p1).append(p2).append(p3).append("\n");
        builder.append("         ").append("n ").append(n1).append(n2).append(n3);
            
        return builder.toString();
    }
}
