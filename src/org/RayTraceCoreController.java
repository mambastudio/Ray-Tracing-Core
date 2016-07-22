/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org;

import core.RayTracerAPI;
import core.display.RenderDisplay;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

/**
 * FXML Controller class
 *
 * @author user
 */
public class RayTraceCoreController implements Initializable {

    private RayTracerAPI api = new RayTracerAPI();
    private RenderDisplay display = new RenderDisplay();
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */    
       
    @FXML
    Tab viewPort;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        display.imageBegin();
        viewPort.setContent(display);
        
    }    
    
}
