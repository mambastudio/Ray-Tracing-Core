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
public class Timer {
    private long startTime, endTime;

    public Timer() {
        startTime = endTime = 0;
    }

    public void start() {
        startTime = endTime = System.nanoTime();
    }

    public void end() {
        endTime = System.nanoTime();
    }

    public long nanos() {
        return endTime - startTime;
    }

    public double seconds() {
        return (endTime - startTime) * 1e-9;
    }

    public static String toString(long nanos) {
        Timer t = new Timer();
        t.endTime = nanos;
        return t.toString();
    }

    public static String toString(double seconds) {
        Timer t = new Timer();
        t.endTime = (long) (seconds * 1e9);
        return t.toString();
    }

    @Override
    public String toString() {
        long millis = nanos() / (1000 * 1000);
        if (millis < 10000)
            return String.format("%dms", millis);
        long hours = millis / (60 * 60 * 1000);
        millis -= hours * 60 * 60 * 1000;
        long minutes = millis / (60 * 1000);
        millis -= minutes * 60 * 1000;
        long seconds = millis / 1000;
        millis -= seconds * 1000;
        return String.format("%d:%02d:%02d.%1d", hours, minutes, seconds, millis / 100);
    }
}
