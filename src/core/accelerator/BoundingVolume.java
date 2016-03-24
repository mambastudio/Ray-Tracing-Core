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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author user
 */
public final class BoundingVolume extends AbstractAccelerator
{
    int maxPrimsInNode = 10;
    ArrayList<AbstractPrimitive> primitives = null;
    LinearBVHNode [] nodes = null;
    BoundingBox bound = null;
    
    public BoundingVolume()
    {
        primitives = new ArrayList<>();
    }
    
    public BoundingVolume(ArrayList<AbstractPrimitive> primitives)
    {        
        setPrimitives(primitives);
    }

    @Override
    public void setPrimitives(ArrayList<AbstractPrimitive> primitives) {
        //Initialize buildData array for primitives
        
        this.primitives = primitives;
        
        ArrayList<BVHPrimitiveInfo> buildData = new ArrayList<>();
        for(int i = 0; i < primitives.size(); i++)
        {
            BoundingBox bbox = primitives.get(i).getWorldBounds();
            buildData.add(new BVHPrimitiveInfo(i, bbox));
        }
        
        //Recursively build BVH tree for primitives
        int totalNodes[] = new int[1];
        ArrayList<AbstractPrimitive> orderedPrims = new ArrayList<>();
        BVHBuildNode root = recursiveBuild(buildData, 0, 
                                           primitives.size(), totalNodes, 
                                           orderedPrims);
        swap(primitives, orderedPrims);
        
        bound = root.bounds;
        
        //Compute representation of depth-first traversal of BVH tree
        nodes = new LinearBVHNode[totalNodes[0]];
        for(int i = 0; i<totalNodes[0]; ++i)
            nodes[i] = new LinearBVHNode();
        int offset[] = new int[1];
        flattenBVHTree(root, offset);
    }
    
    public int flattenBVHTree(BVHBuildNode node, int[] offset)
    {
        LinearBVHNode linearNode = nodes[offset[0]];
        linearNode.bounds = node.bounds;
        int myOffset = offset[0]++;
        if(node.nPrimitives > 0)
        {
            linearNode.primitivesOffset = node.firstPrimOffset;
            linearNode.nPrimitives = node.nPrimitives;
        }
        else
        {
            //Creater interior flattened BVH node
            linearNode.axis = node.splitAxis;
            linearNode.nPrimitives = 0;
            flattenBVHTree(node.children[0], offset);
            linearNode.secondChildOffset = flattenBVHTree(node.children[1], offset);
        }
        return myOffset;
    }
    
    public BVHBuildNode recursiveBuild(ArrayList<BVHPrimitiveInfo> buildData, int start, int end, int[] totalNodes, ArrayList<AbstractPrimitive> orderedPrims)
    {
        totalNodes[0]++;
        BVHBuildNode node = new BVHBuildNode();
        
        //Compute bounds of all primitives in BVH node
        BoundingBox bbox = new BoundingBox();
        for(int i = start; i < end; ++i)
            bbox = BoundingBox.union(bbox, buildData.get(i).bounds);
        
        int nPrimitives = end - start;
        if(nPrimitives  < 15)
        {
            //Create leaf BVHBuildNode
            int firstPrimOffset = orderedPrims.size();
            for(int i = start; i<end; ++i)
            {
                int primNum = buildData.get(i).primitiveNumber;
                orderedPrims.add(primitives.get(primNum));
            }
            node.initLeaf(firstPrimOffset, nPrimitives, bbox);
        }
        else
        {
            //Compute bound of primitive centroids, choose split dimension dim
            BoundingBox centroidBounds = new BoundingBox();
            for(int i = start; i<end; ++i)
                centroidBounds = BoundingBox.union(centroidBounds, buildData.get(i).centroid);
            int dim = centroidBounds.maximumExtent();
            
            //Partition primitives into two sets and build children
            int mid = (start + end)/2;
            if(centroidBounds.maximum.get(dim) == centroidBounds.minimum.get(dim))
            {
                //Create leaf BVHBuildNode
                int firstPrimOffset = orderedPrims.size();
                for(int i = start; i<end; ++i)
                {
                    int primNum = buildData.get(i).primitiveNumber;
                    orderedPrims.add(primitives.get(primNum));
                }
                node.initLeaf(firstPrimOffset, nPrimitives, bbox);
                return node;
            }
            
            //Partition primitives into equally-sized subsets            
            sort(buildData, start, end, dim);         
            node.initInterior(dim, recursiveBuild(buildData, start, mid,
                                                  totalNodes, orderedPrims),
                                   recursiveBuild(buildData, mid, end,
                                                  totalNodes, orderedPrims));
        }        
        return node;
    }
        
    public void swap(ArrayList<AbstractPrimitive> list1, ArrayList<AbstractPrimitive> list2)
    {
        ArrayList<AbstractPrimitive> tmpList = new ArrayList<>(list1);
        list1.clear();
        list1.addAll(list2);
        list2.clear();
        list2.addAll(tmpList);
    }
    
    public void sort(ArrayList<BVHPrimitiveInfo> list, int start, int end, int dim)
    {
        Collections.sort(list.subList(start, end), new ComparePoints(dim));
    }

    @Override
    public boolean intersect(Ray r, Intersection isect) {
        if(nodes == null) return false;
        boolean hit = false;        
        int[] dirIsNeg = r.dirIsNeg();
        
        //Follow ray through BVH nodes to find primitive intersections
        int todoOffset = 0, nodeNum = 0;
        int[] todo = new int[64];
        while (true)
        {
            LinearBVHNode node = nodes[nodeNum];
            //Check ray against BVH Node
            if(node.bounds.intersectP(r, null))
            {
                if(node.nPrimitives > 0)
                {
                    //Intersect ray with primitives in leaf BVH node
                    for (int i = 0; i < node.nPrimitives; ++i)
                        if(primitives.get(node.primitivesOffset+i).intersect(r, isect))
                            hit = true;
                    if(todoOffset == 0) break;
                    nodeNum = todo[--todoOffset];
                }
                else
                {
                    //Put far BVH node on todo stack, advance to near node
                    if (dirIsNeg[node.axis] == 1) 
                    {
                        todo[todoOffset++] = nodeNum + 1;
                        nodeNum = node.secondChildOffset;
                    }
                    else 
                    {
                        todo[todoOffset++] = node.secondChildOffset;
                        nodeNum = nodeNum + 1;
                    }
                }                
            }
            else
            {
                if (todoOffset == 0) break;
                nodeNum = todo[--todoOffset];                
            }
        }
        return hit;
    }

    @Override
    public boolean intersectP(Ray r) {
        if(nodes == null) return false;
        boolean hit = false;        
        int[] dirIsNeg = r.dirIsNeg();
        
        //Follow ray through BVH nodes to find primitive intersections
        int todoOffset = 0, nodeNum = 0;
        int[] todo = new int[64];
        while (true)
        {
            LinearBVHNode node = nodes[nodeNum];
            //Check ray against BVH Node
            if(node.bounds.intersectP(r, null))
            {
                if(node.nPrimitives > 0)
                {
                    //Intersect ray with primitives in leaf BVH node
                    for (int i = 0; i < node.nPrimitives; ++i)
                        if(primitives.get(node.primitivesOffset+i).intersectP(r))
                            hit = true;
                    if(todoOffset == 0) break;
                    nodeNum = todo[--todoOffset];
                }
                else
                {
                    //Put far BVH node on todo stack, advance to near node
                    if (dirIsNeg[node.axis] == 1) 
                    {
                        todo[todoOffset++] = nodeNum + 1;
                        nodeNum = node.secondChildOffset;
                    }
                    else 
                    {
                        todo[todoOffset++] = node.secondChildOffset;
                        nodeNum = nodeNum + 1;
                    }
                }                
            }
            else
            {
                if (todoOffset == 0) break;
                nodeNum = todo[--todoOffset];                
            }
        }
        return hit;
    }

    @Override
    public BoundingBox getWorldBounds() {
        return bound;
    }
    
    public class BVHBuildNode
    {
        BoundingBox bounds;
        BVHBuildNode children[] = new BVHBuildNode[2];
        int splitAxis, firstPrimOffset, nPrimitives;
        
        public void initLeaf(int first, int n, BoundingBox b)
        {
            firstPrimOffset = first;
            nPrimitives = n;
            //System.out.println(n);
            bounds = b;
        }
        
        public void initInterior(int axis, BVHBuildNode c0, BVHBuildNode c1)
        {
            children[0] = c0;
            children[1] = c1;
            bounds = BoundingBox.union(c0.bounds, c1.bounds);
            splitAxis = axis;
            nPrimitives = 0;
        }
    }
    
    public class BVHPrimitiveInfo
    {
        int primitiveNumber;
        Point3f centroid;
        BoundingBox bounds;
        
        public BVHPrimitiveInfo(int pn, BoundingBox b)
        {
            primitiveNumber = pn;
            bounds = b;
            
            Point3f p1 = b.minimum.mul(0.5f);
            Point3f p2 = b.maximum.mul(0.5f);
            
            centroid = new Point3f(p1.x + p2.x, p1.y + p2.y, p1.z + p2.z);
        }
    }
    
    public class LinearBVHNode
    {
        BoundingBox bounds;
        
        int primitivesOffset;  //leaf
        int secondChildOffset; //interior
        
        int nPrimitives;        // 0 -> interior node
        int axis;               // interior node: xyz
        int pad[] = new int[2]; // ensure 32 byte total size
    }
    
    public class ComparePoints implements Comparator<BVHPrimitiveInfo>
    {
        int dim;
        public ComparePoints(int dim)
        {
            this.dim = dim;
        }
        
        @Override
        public int compare(BVHPrimitiveInfo o1, BVHPrimitiveInfo o2) {
            if(o1.centroid.get(dim) < o2.centroid.get(dim))
                return -1;
            else if(o1.centroid.get(dim) == o2.centroid.get(dim))
                return 0;
            else 
                return 1;
        }        
    }     
    
    
}
