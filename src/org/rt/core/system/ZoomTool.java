/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
