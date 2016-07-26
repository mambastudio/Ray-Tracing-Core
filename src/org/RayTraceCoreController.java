/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org;

import org.rt.core.Camera;
import org.rt.core.ImageSampler;
import org.rt.RayTracerAPI;
import org.rt.core.Scene;
import org.rt.core.coordinates.Point3f;
import org.rt.core.coordinates.Vector3f;
import org.rt.display.RenderDisplay;
import org.rt.core.render.SimpleRenderer;
import org.rt.core.scene.CornellScene;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;

/**
 * FXML Controller class
 *
 * @author user
 */
public class RayTraceCoreController implements Initializable {

    private final RayTracerAPI api = new RayTracerAPI();
    private final Scene scene = new CornellScene();
    private final RenderDisplay display = new RenderDisplay();
    private final Camera camera = new Camera(new Point3f(0, 0, 4), new Point3f(), new Vector3f(0, 1, 0), 45);
    private final ImageSampler renderer = new SimpleRenderer();
    
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
                
        viewPort.setContent(display);
        
        api.setScene(scene);
        api.setCamera(camera);
        api.setImageSize(600, 600);
        api.setRenderer(renderer);
       
    }  
    
    public void render(ActionEvent e)
    {
        api.render(display);
    }
    
    public void pause(ActionEvent e)
    {
        if(e.getSource() instanceof Button)
        {
            Button b = (Button)e.getSource();
            switch (b.getText()) {
                case "Pause":
                    api.pause();
                    b.setText("Resume");
                    break;
                case "Resume":
                    api.resume();
                    b.setText("Pause");
                    break;
            }
        }
        
    }
    
    public void resume(ActionEvent e)            
    {
        api.resume();
    }
    
    public void stop(ActionEvent e)
    {
        api.stop();
    }
    
}
