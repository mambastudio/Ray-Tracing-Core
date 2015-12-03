/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.accelerator;

import core.AbstractAccelerator;
import core.AbstractPrimitive;
import core.Intersection;
import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import core.math.BoundingBox;
import core.math.Ray;
import static core.math.Utility.clamp;
import static java.lang.Math.pow;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class UniformGrid extends AbstractAccelerator
{
    ArrayList<AbstractPrimitive> primitives = null;
    BoundingBox bounds = null;
    int nVoxels[];
    Voxel[] voxels;
    Vector3f width = new Vector3f(), invWidth = new Vector3f();
    private final static int[] cmpToAxis = {2, 1, 2, 1, 2, 2, 0, 0};
    
    public UniformGrid()
    {
        this.bounds = new BoundingBox();
        this.nVoxels = new int[3];
    }
    
    public UniformGrid(ArrayList<AbstractPrimitive> primitives)
    {
        this.bounds = new BoundingBox();
        this.nVoxels = new int[3];
        
        setPrimitives(primitives);
    }

    @Override
    public final void setPrimitives(ArrayList<AbstractPrimitive> primitives) {
        this.primitives = primitives;
                
        // Compute bounds and choose grid resolution
        for(AbstractPrimitive primitive : primitives)
            bounds.include(primitive.getWorldBounds());
        
        Vector3f delta = Point3f.sub(bounds.maximum, bounds.minimum);
        
        // Find _voxelsPerUnitDist_ for grid
        int maxAxis = bounds.maximumExtent();
        float invMaxWidth = 1.f / delta.get(maxAxis);
        float cubeRoot = 3f * (float) pow(primitives.size(), 1f / 3f);
        float voxelsPerUnitDist = cubeRoot * invMaxWidth;
        for (int axis = 0; axis < 3; ++axis) {
            nVoxels[axis] = (int) (delta.get(axis) * voxelsPerUnitDist);
            nVoxels[axis] = clamp(nVoxels[axis], 1, 64);
        }
        
        // Compute voxel widths and allocate voxels
        for (int axis = 0; axis < 3; ++axis) {
            width.set(axis, delta.get(axis) / nVoxels[axis]);
            invWidth.set(axis, (width.get(axis) == 0.f) ? 0.f : 1.f / width.get(axis));
        }
        int nv = nVoxels[0] * nVoxels[1] * nVoxels[2];
        voxels = new Voxel[nv];
        
        // Add primitives to grid voxels
        for (int i = 0; i < primitives.size(); ++i) 
        {
            // Find voxel extent of primitive
            BoundingBox pb = primitives.get(i).getWorldBounds();
            int[] vmin = new int[3], vmax = new int[3];
            for (int axis = 0; axis < 3; ++axis) 
            {
                vmin[axis] = posToVoxel(pb.minimum, axis);
                vmax[axis] = posToVoxel(pb.maximum, axis);
            }
            // Add primitive to overlapping voxels
            //PBRT_GRID_VOXELIZED_PRIMITIVE(vmin, vmax);
            for (int z = vmin[2]; z <= vmax[2]; ++z) 
            {
                for (int y = vmin[1]; y <= vmax[1]; ++y)
                {
                    for (int x = vmin[0]; x <= vmax[0]; ++x)
                    {
                        int o = offset(x, y, z);
                        if (voxels[o] == null) 
                        {
                            // Allocate new voxel and store primitive in it
                            voxels[o] = new Voxel(primitives.get(i));
                        } 
                        else 
                        {
                            // Add primitive to already-allocated voxel
                            voxels[o].addPrimitive(primitives.get(i));
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean intersect(Ray ray, Intersection isect) {
        // Check ray against overall grid bounds
        float[] rayT = new float[2];
        if (bounds.inside(ray.getPoint(ray.getMin()))) 
        {
            rayT[0] = ray.getMin();
        } else if (!bounds.intersectP(ray, rayT)) {
            //PBRT_GRID_RAY_MISSED_BOUNDS();
            return false;
        }
        Point3f gridIntersect = ray.getPoint(rayT[0]);

        // Set up 3D DDA for ray
        float[] NextCrossingT = new float[3], DeltaT = new float[3];
        int[] Step = new int[3], Out = new int[3], Pos = new int[3];
        for (int axis = 0; axis < 3; ++axis) {
            // Compute current voxel for axis
            Pos[axis] = posToVoxel(gridIntersect, axis);
            if (ray.d.get(axis) >= 0) {
                // Handle ray with positive direction for voxel stepping
                NextCrossingT[axis] = rayT[0]
                        + (voxelToPos(Pos[axis] + 1, axis) - gridIntersect.get(axis)) / ray.d.get(axis);
                DeltaT[axis] = width.get(axis) / ray.d.get(axis);
                Step[axis] = 1;
                Out[axis] = nVoxels[axis];
            } else {
                // Handle ray with negative direction for voxel stepping
                NextCrossingT[axis] = rayT[0]
                        + (voxelToPos(Pos[axis], axis) - gridIntersect.get(axis)) / ray.d.get(axis);
                DeltaT[axis] = -width.get(axis) / ray.d.get(axis);
                Step[axis] = -1;
                Out[axis] = -1;
            }
        }
        
        // Walk ray through voxel grid
        //RWMutexLock lock=(*rwMutex, READ);
        boolean hitSomething = false;
        for (;;) {
            // Check for intersection in current voxel and advance to next
            Voxel voxel = voxels[offset(Pos[0], Pos[1], Pos[2])];
            //PBRT_GRID_RAY_TRAVERSED_VOXEL(Pos, voxel ? voxel->size() : 0);
            if (voxel != null) {
                hitSomething |= voxel.intersect(ray, isect/*, lock*/);
            }

            // Advance to next voxel

            // Find _stepAxis_ for stepping to next voxel
            int bits = ((NextCrossingT[0] < NextCrossingT[1]) ? 4 : 0)
                    + ((NextCrossingT[0] < NextCrossingT[2]) ? 2 : 0)
                    + ((NextCrossingT[1] < NextCrossingT[2]) ? 1 : 0);
            int stepAxis = cmpToAxis[bits];
            if (ray.getMax() < NextCrossingT[stepAxis]) {
                break;
            }
            Pos[stepAxis] += Step[stepAxis];
            if (Pos[stepAxis] == Out[stepAxis]) {
                break;
            }
            NextCrossingT[stepAxis] += DeltaT[stepAxis];
        }
        return hitSomething;
    }

    @Override
    public boolean intersectP(Ray ray) {
        //PBRT_GRID_INTERSECTIONP_TEST(const_cast<GridAccel *>(this), const_cast<Ray *>(&ray));
        //RWMutexLock lock(*rwMutex, READ);
        // Check ray against overall grid bounds
        float[] rayT = new float[2];
        if (bounds.inside(ray.getPoint(ray.getMin()))) {
            rayT[0] = ray.getMin();
        } else if (!bounds.intersectP(ray, rayT)) {
            //PBRT_GRID_RAY_MISSED_BOUNDS();
            return false;
        }
        Point3f gridIntersect = ray.getPoint(rayT[0]);

        // Set up 3D DDA for ray
        float[] NextCrossingT = new float[3], DeltaT = new float[3];
        int[] Step = new int[3], Out = new int[3], Pos = new int[3];
        for (int axis = 0; axis < 3; ++axis) {
            // Compute current voxel for axis
            Pos[axis] = posToVoxel(gridIntersect, axis);
            if (ray.d.get(axis) >= 0) {
                // Handle ray with positive direction for voxel stepping
                NextCrossingT[axis] = rayT[0]
                        + (voxelToPos(Pos[axis] + 1, axis) - gridIntersect.get(axis)) / ray.d.get(axis);
                DeltaT[axis] = width.get(axis) / ray.d.get(axis);
                Step[axis] = 1;
                Out[axis] = nVoxels[axis];
            } else {
                // Handle ray with negative direction for voxel stepping
                NextCrossingT[axis] = rayT[0]
                        + (voxelToPos(Pos[axis], axis) - gridIntersect.get(axis)) / ray.d.get(axis);
                DeltaT[axis] = -width.get(axis) / ray.d.get(axis);
                Step[axis] = -1;
                Out[axis] = -1;
            }
        }

        // Walk grid for shadow ray
        for (;;) {
            int o = offset(Pos[0], Pos[1], Pos[2]);
            Voxel voxel = voxels[o];
            //PBRT_GRID_RAY_TRAVERSED_VOXEL(Pos, voxel ? voxel->size() : 0);
            if (voxel != null && voxel.intersectP(ray/*, lock*/)) {
                return true;
            }
            // Advance to next voxel

            // Find _stepAxis_ for stepping to next voxel
            int bits = ((NextCrossingT[0] < NextCrossingT[1]) ? 4 : 0)
                    + ((NextCrossingT[0] < NextCrossingT[2]) ? 2 : 0)
                    + ((NextCrossingT[1] < NextCrossingT[2]) ? 1 : 0);
            int stepAxis = cmpToAxis[bits];
            if (ray.getMax() < NextCrossingT[stepAxis]) {
                break;
            }
            Pos[stepAxis] += Step[stepAxis];
            if (Pos[stepAxis] == Out[stepAxis]) {
                break;
            }
            NextCrossingT[stepAxis] += DeltaT[stepAxis];
        }
        return false;
    }

    @Override
    public BoundingBox getWorldBounds() {
        return bounds;
    }
    
    private int posToVoxel(Point3f P, int axis) {
        int v = (int) ((P.get(axis) - bounds.minimum.get(axis))
                * invWidth.get(axis));
        return clamp(v, 0, nVoxels[axis] - 1);
    }
    
    public float voxelToPos(int p, int axis) {
        return bounds.minimum.get(axis) + p * width.get(axis);
    }

    public int offset(int x, int y, int z) {
        return z * nVoxels[0] * nVoxels[1] + y * nVoxels[0] + x;
    }
    
    public class Voxel
    {
        private final ArrayList<AbstractPrimitive> primitives = new ArrayList<>();
        
        public Voxel(){}
        public Voxel(AbstractPrimitive primitive) {primitives.add(primitive);}
        public void addPrimitive(AbstractPrimitive prim) {
            primitives.add(prim);
        }
        
        public boolean intersect(Ray r, Intersection isect) 
        {        
            boolean hasIntersected = false;
            for(AbstractPrimitive prim: primitives)
                hasIntersected |= prim.intersect(r, isect);        
            return hasIntersected;
        }
        
        public boolean intersectP(Ray r) 
        {        
            for(AbstractPrimitive prim: primitives)
                if(prim.intersectP(r))
                    return true;
            return false;
        }
    }
}
