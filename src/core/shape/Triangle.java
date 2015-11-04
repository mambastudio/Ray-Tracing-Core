/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.shape;

import core.AbstractShape;
import core.coordinates.Normal3f;
import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import core.math.BoundingBox;
import core.math.DifferentialGeometry;
import core.math.Ray;
import static core.math.MonteCarlo.areaTriangle;
import static core.math.MonteCarlo.uniformSampleTriangle;

/**
 *
 * @author user
 */
public class Triangle extends AbstractShape
{
    Point3f p1, p2, p3;
    Normal3f n;
    
    public Triangle(Point3f p1, Point3f p2, Point3f p3, Normal3f n)
    {
        super(new Transform());
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;  
        this.n = new Normal3f(n.normalize());
        
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
        Vector3f e1, e2, h, s, q;
        float a, f, u, v;

        e1 = Point3f.sub(p2, p1);
        e2 = Point3f.sub(p3, p1);
        h = Vector3f.cross(r.d, e2);
        a = Vector3f.dot(e1, h);

        if (a > -0.00001 && a < 0.00001)
		return false;

        f = 1/a;
        s = Point3f.sub(r.o, p1);
	u = f * (Vector3f.dot(s, h));

        if (u < 0.0 || u > 1.0)
		return false;

        q = Vector3f.cross(s, e1);
	v = f * Vector3f.dot(r.d, q);

	if (v < 0.0 || u + v > 1.0)
		return false;
	
	float thit = f * Vector3f.dot(e2, q);        
        
	if (thit > 0.0001f)
        {
            if(r.isInside(thit))              
                return true;           
        }        
        return false;
    }

    @Override
    public boolean intersect(Ray r, DifferentialGeometry dg) {
        Vector3f e1, e2, h, s, q;
        float a, f, u, v;

        e1 = Point3f.sub(p2, p1);
        e2 = Point3f.sub(p3, p1);
        h = Vector3f.cross(r.d, e2);
        a = Vector3f.dot(e1, h);

        if (a > -0.00001 && a < 0.00001)
		return false;

        f = 1/a;
        s = Point3f.sub(r.o, p1);
	u = f * (Vector3f.dot(s, h));

        if (u < 0.0 || u > 1.0)
		return false;

        q = Vector3f.cross(s, e1);
	v = f * Vector3f.dot(r.d, q);

	if (v < 0.0 || u + v > 1.0)
		return false;
	
	float thit = f * Vector3f.dot(e2, q);        
        Point3f phit = r.getPoint(thit);

	if (thit > 0.0001f)
        {
            if(r.isInside(thit))
            {                                   
                Normal3f nhit;
                if(Vector3f.dot(n, r.d) < 0) 
                    nhit = n;   
                else
                    nhit = n.neg();
                
                dg.p = phit;
                dg.n = nhit;
                dg.u = u;
                dg.v = v;
                dg.shape = this;                
                r.setMax(thit);       
                
                return true;
            }
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
        n.set(this.n);
        return uniformSampleTriangle(u1, u2, p1, p2, p3);
    }
}
