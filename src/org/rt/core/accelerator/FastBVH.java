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
import java.util.Collections;
import java.util.Stack;
import org.rt.core.AbstractAccelerator;
import org.rt.core.AbstractPrimitive;
import org.rt.core.Intersection;
import org.rt.core.math.BoundingBox;
import org.rt.core.math.Ray;

/**
 *
 * @author user
 */
public class FastBVH extends AbstractAccelerator
{
    ArrayList<AbstractPrimitive> primitives = new ArrayList<>();
    BVHFlatNode[] flatTree;
    int nNodes, leafSize = 15, nLeafs;
            
    @Override
    public void build(ArrayList<AbstractPrimitive> prims) 
    {
        this.primitives.addAll(prims);        
        Stack<BVHBuildEntry> todo = new Stack<>();
        int untouched    = 0xffffffff;
        int touchedTwice = 0xfffffffd;
        
        //Push the root
        BVHBuildEntry root = new BVHBuildEntry();
        root.start = 0;
        root.end = primitives.size();
        root.parent = 0xfffffffc;
        todo.push(root);
        
        
        ArrayList<BVHFlatNode> buildNodes = new ArrayList<>();
        
        while(!todo.isEmpty())
        {
            // Pop the next item off of the stack
            BVHBuildEntry bnode = todo.pop();
            int start = bnode.start;
            int end = bnode.end;
            int nPrims = end - start;
            
            nNodes++;
            BVHFlatNode node = new BVHFlatNode();
            node.start = start;
            node.nPrims = nPrims;
            node.rightOffset = untouched;
            
            // Calculate the bounding box for this node
            BoundingBox bb = new BoundingBox(); bb.include(primitives.get(start).getWorldBounds());
            BoundingBox bc = new BoundingBox(); bc.include(primitives.get(start).getWorldBounds().getCenter());
            for(int p = start+1; p < end; ++p) 
            {
                bb.include(primitives.get(p).getWorldBounds());
                bc.include(primitives.get(p).getWorldBounds().getCenter());
            }
            node.bbox = bb;
            
            // If the number of primitives at this point is less than the leaf
            // size, then this will become a leaf. (Signified by rightOffset == 0)
            if(nPrims <= leafSize) {
                node.rightOffset = 0;
                nLeafs++;
            }
            
            buildNodes.add(node);
            
            // Child touches parent...
            // Special case: Don't do this for the root.            
            if(bnode.parent != 0xfffffffc) {
                buildNodes.get(bnode.parent).rightOffset--;
                
                // When this is the second touch, this is the right child.
                // The right child sets up the offset for the flat tree.
                if( buildNodes.get(bnode.parent).rightOffset == touchedTwice)
                    buildNodes.get(bnode.parent).rightOffset = nNodes - 1 - bnode.parent;
            }
            
            // If this is a leaf, no need to subdivide.
            if(node.rightOffset == 0)
                continue;
            
            // Set the split dimensions
            int split_dim = bc.maximumExtent();
            
            // Split on the center of the longest axis
            float split_coord = .5f * (bc.minimum.get(split_dim) + bc.maximum.get(split_dim));

            // Partition the list of objects on this split
            int mid = start;
            
            for(int i=start;i<end;++i) 
            {
                if((primitives.get(i).getWorldBounds().getCenter().get(split_dim)) < split_coord ) 
                {
                    Collections.swap(primitives, i, mid);                    
                    ++mid;
                }
            }
            
            // If we get a bad split, just choose the center...
            if(mid == start || mid == end) {
                mid = start + (end-start)/2;
            }
            
            // Push right child
            BVHBuildEntry right = new BVHBuildEntry();
            right.start = mid;
            right.end = end;
            right.parent = nNodes-1;
            todo.push(right);
            
            // Push left child
            BVHBuildEntry left = new BVHBuildEntry();
            left.start = start;
            left.end = mid;
            left.parent = nNodes-1;
            todo.push(left);
        }       
        
        // Copy the temp node data to a flat array
        flatTree = new BVHFlatNode[nNodes];
        for(int n=0; n<nNodes; ++n) 
            flatTree[n] = buildNodes.get(n);
  
        System.out.println("f Nodes " +nNodes);
    }

    @Override
    public boolean intersect(Ray r, Intersection isect) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean intersectP(Ray r) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BoundingBox getWorldBounds() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    
    
    public class BVHFlatNode {
        BoundingBox bbox;
        int start, nPrims, rightOffset;
    }
    
    public class BVHBuildEntry {
        // If non-zero then this is the index of the parent. (used in offsets)
        int parent;
        // The range of objects in the object list covered by this node.
        int start, end;
    }
}
