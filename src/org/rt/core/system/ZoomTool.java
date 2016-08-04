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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 *
 * @author user
 * http://awhite.blogspot.co.ke/2013/02/javafx-pan-zoom.html
 * 
 */
public class ZoomTool 
{
    private static final double SCALE_DELTA = 1.1; 
    
    private static final DoubleProperty accumX = new SimpleDoubleProperty(), accumY = new SimpleDoubleProperty();    
    private static final DoubleProperty mouseX = new SimpleDoubleProperty(), mouseY = new SimpleDoubleProperty();    
    
    public static void reset()
    {
        accumX.set(0);
        accumY.set(0);
    }
    
    public static void setMouseValue(double mouseValueX, double mouseValueY)
    {
        mouseX.set(mouseValueX);
        mouseY.set(mouseValueY);
    }
    
    public static void zoom(Node node, ScrollEvent event) 
    {
        event.consume();
       
        if (event.getDeltaY() == 0) 
            return;
        
        double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA
                    : 1 / SCALE_DELTA;
                
        node.setScaleX(node.getScaleX() * scaleFactor);  
        node.setScaleY(node.getScaleY() * scaleFactor);         
    }
    
    public static void pan(Node node, MouseEvent event)
    {
        double dx = event.getX() - mouseX.doubleValue();
        double dy = event.getY() - mouseY.doubleValue();
        
        accumX.set(accumX.doubleValue() + dx);
        accumY.set(accumY.doubleValue() + dy);
        
        node.setTranslateX(accumX.doubleValue());
        node.setTranslateY(accumY.doubleValue());
            
        mouseX.set(event.getX());
        mouseY.set(event.getY());
    }
}
