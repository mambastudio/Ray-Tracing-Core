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
package org.rt.core.system;

/**
 *
 * @author user
 */
public class ByteUtil 
{
    public static final byte[] get2Bytes(int i) 
    {
        byte[] b = new byte[2];

        b[0] = (byte) (i & 0xFF);
        b[1] = (byte) ((i >> 8) & 0xFF);

        return b;
    }

    public static final byte[] get4Bytes(int i) 
    {
        byte[] b = new byte[4];

        b[0] = (byte) (i & 0xFF);
        b[1] = (byte) ((i >> 8) & 0xFF);
        b[2] = (byte) ((i >> 16) & 0xFF);
        b[3] = (byte) ((i >> 24) & 0xFF);

        return b;
    }

    public static final byte[] get4BytesInv(int i) {
        byte[] b = new byte[4];

        b[3] = (byte) (i & 0xFF);
        b[2] = (byte) ((i >> 8) & 0xFF);
        b[1] = (byte) ((i >> 16) & 0xFF);
        b[0] = (byte) ((i >> 24) & 0xFF);

        return b;
    }
    
    public static final int toInt(byte in0, byte in1, byte in2, byte in3) {
        return (in0 & 0xFF) | ((in1 & 0xFF) << 8) | ((in2 & 0xFF) << 16) | ((in3 & 0xFF) << 24);
    }

    public static final int toInt(byte[] in) {
        return toInt(in[0], in[1], in[2], in[3]);
    }
}
