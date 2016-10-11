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
package accel;

import org.rt.core.math.BoundingBox;

/**
 *
 * @author user
 */
public class FlatNode implements Struct {
    BoundingBox bounds;
    int start, nPrims, rightOffset, axis;

    @Override
    public int[] getArray() {
        int[] array = new int[10];
        array[0] = Float.floatToRawIntBits(bounds.minimum.x);
        array[1] = Float.floatToRawIntBits(bounds.minimum.y);
        array[2] = Float.floatToRawIntBits(bounds.minimum.z);
        array[3] = Float.floatToRawIntBits(bounds.maximum.x);
        array[4] = Float.floatToRawIntBits(bounds.maximum.y);
        array[5] = Float.floatToRawIntBits(bounds.maximum.z);
        array[6] = start;
        array[7] = nPrims;
        array[8] = rightOffset;
        array[9] = axis;        
        return array;
    }

    @Override
    public void setArray(int... array) {
        if(array != null && array.length == size())
        {
            bounds.minimum.x = Float.intBitsToFloat(array[0]);
            bounds.minimum.y = Float.intBitsToFloat(array[1]);
            bounds.minimum.z = Float.intBitsToFloat(array[2]);
            bounds.maximum.x = Float.intBitsToFloat(array[3]);
            bounds.maximum.y = Float.intBitsToFloat(array[4]);
            bounds.maximum.z = Float.intBitsToFloat(array[5]);
            start            = array[6];
            nPrims           = array[7];
            rightOffset      = array[8];
            axis             = array[9];
        }
    }

    @Override
    public int size() {
        return 10;
    }
}
