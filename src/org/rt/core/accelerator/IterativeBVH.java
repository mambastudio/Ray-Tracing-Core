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
package org.rt.core.accelerator;

import java.util.ArrayList;
import java.util.Stack;
import org.rt.core.AbstractAccelerator;
import org.rt.core.AbstractPrimitive;
import org.rt.core.Intersection;
import org.rt.core.math.BoundingBox;
import org.rt.core.math.Ray;
import org.rt.util.Sorting;

/**
 *
 * @author user
 */
public class IterativeBVH extends AbstractAccelerator
{
    ArrayList<AbstractPrimitive> primitives;
    BVHFlatNode[] nodes = null;
    BoundingBox bound = null;
    
    int nNodes, nLeafs, leafSize = 15;
    
    @Override
    public void build(ArrayList<AbstractPrimitive> prims) 
    {
        this.primitives = prims;        
        Stack<BVHBuildEntry> todo = new Stack<>();
        int untouched = 0xffffffff;
        int touchedtwice = 0xfffffffd;
        
        BVHBuildEntry root = new BVHBuildEntry();
        root.start = 0;
        root.end = primitives.size();
        root.parent = 0xfffffffc;
        todo.push(root);
        
        ArrayList<BVHFlatNode> buildNodes = new ArrayList<>();
        
        while(!todo.isEmpty())
        {
            BVHBuildEntry bnode = todo.pop();
            int start = bnode.start;
            int end = bnode.end;            
            int nPrims = end - start;
            
            nNodes++;
            BVHFlatNode node = new BVHFlatNode();
            node.start = start;
            node.nPrims = nPrims;            
            node.rightOffset = untouched;
            
            //Calculate the bounding box for this node
            BoundingBox bb = new BoundingBox(); bb.include(primitives.get(start).getWorldBounds());
            BoundingBox bc = new BoundingBox(); bc.include(primitives.get(start).getWorldBounds().getCenter());
            for(int p = start+1; p<end; p++)
            {
                bb.include(primitives.get(p).getWorldBounds());
                bc.include(primitives.get(p).getWorldBounds().getCenter());
            }
            node.bounds = bb;
            
            //If the number of primitives at this point is less than the leaf
            //size, then this will become a life, (Signified by rightOffset == 0)
            if(nPrims < leafSize)
            {
                node.rightOffset = 0;
                nLeafs++;
            }
            
            buildNodes.add(node);
            
            //Child touches parent...
            //Special case: Don't do this for the root.
            if(bnode.parent != 0xfffffffc)
            {
                buildNodes.get(bnode.parent).rightOffset--;
                
                //When this is the second touch, this is the right child.
                //The right child sets up the offset for the flat tree.
                if(buildNodes.get(bnode.parent).rightOffset == touchedtwice)
                    buildNodes.get(bnode.parent).rightOffset = nNodes - 1 - bnode.parent;
            }
            
            //If thi is a leaf, no need to subdivide
            if(node.rightOffset == 0)
            {                
                continue;
            }
            
            //set the split dimensions
            int split_dim = bc.maximumExtent();
            
            //split on the center of the longest axis
            float split_coord = bc.getCenter(split_dim);
            
            //partition the list of objects on this split            
            int mid = Sorting.partition(prims, start, end, split_dim, split_coord);
            
            //if we get a bad split, just choose the center...
            if(mid == start || mid == end)
                mid = start + (end-start)/2;
            
            //set parent split dimension
            node.axis = split_dim;
            
            //push the right child
            BVHBuildEntry right = new BVHBuildEntry();
            right.start = mid;
            right.end = end;
            right.parent = nNodes - 1;            
            todo.push(right);
            
            //push the left child
            BVHBuildEntry left = new BVHBuildEntry();
            left.start = start;
            left.end = mid;
            left.parent = nNodes - 1;            
            todo.push(left);
        }
        
        //copy the temp node data to a flat array
        nodes = new BVHFlatNode[nNodes];
        for(int n = 0; n<nNodes; n++)
            nodes[n] = buildNodes.get(n);       
        
        bound = nodes[0].bounds;
        //System.out.println(Arrays.toString(nodes));
    }
    
    public class BVHBuildEntry 
    {
        // If non-zero then this is the index of the parent. (used in offsets)
        int parent;
        // The range of objects in the object list covered by this node.
        int start, end;
    };
    
    @Override
    public boolean intersect(Ray r, Intersection isect) {
        int[] todo = new int[64];
        int stackptr = 0;
        boolean hit = false;
        int[] dirIsNeg = r.dirIsNeg();
                        
        while(stackptr >= 0)
        {
            int ni = todo[stackptr];
            stackptr--;            
            BVHFlatNode node = nodes[ni];
            
            if(node.bounds.intersectP(r, null))
            {
                if(node.rightOffset == 0)
                {
                    for(int i = 0; i<node.nPrims; i++)
                        if(primitives.get(node.start+i).intersect(r, isect))
                            hit = true;
                }
                else
                {
                    if (dirIsNeg[node.axis] == 1) 
                    {
                        todo[++stackptr] = ni + node.rightOffset;
                        todo[++stackptr] = ni + 1;
                    }
                    else
                    {
                        todo[++stackptr] = ni + 1;
                        todo[++stackptr] = ni + node.rightOffset;
                    }
                }
            }
        }
        
        return hit;
    }

    @Override
    public boolean intersectP(Ray r) {
        int[] todo = new int[64];
        int stackptr = 0;
        boolean hit = false;
        int[] dirIsNeg = r.dirIsNeg();
                        
        while(stackptr >= 0)
        {
            int ni = todo[stackptr];
            stackptr--;            
            BVHFlatNode node = nodes[ni];
            
            if(node.bounds.intersectP(r, null))
            {
                if(node.rightOffset == 0)
                {
                    for(int i = 0; i<node.nPrims; i++)
                        if(primitives.get(node.start+i).intersectP(r))
                            hit = true;
                }
                else
                {
                    if (dirIsNeg[node.axis] == 1) 
                    {
                        todo[++stackptr] = ni + node.rightOffset;
                        todo[++stackptr] = ni + 1;
                    }
                    else
                    {
                        todo[++stackptr] = ni + 1;
                        todo[++stackptr] = ni + node.rightOffset;
                    }
                }
            }
        }
        
        return hit;
    }

    @Override
    public BoundingBox getWorldBounds() {
        return bound;
    }
    
    public class BVHFlatNode
    {
        BoundingBox bounds;
        int start, nPrims, rightOffset, axis;
        
        @Override
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
            builder.append("start       ").append(start).append("\n");
            builder.append("nPrims      ").append(nPrims).append("\n");
            builder.append("rightOffset ").append(rightOffset).append("\n");
            builder.append("axis        ").append(axis).append("\n");
            
            return builder.toString();
        }
    }    
}
