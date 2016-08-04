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
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import org.rt.core.AbstractPrimitive;
import org.rt.core.scene.CornellSceneDescription;
import org.rt.thread.TimerExecution;

/**
 * FXML Controller class
 *
 * @author user
 */
public class RayTraceCoreController implements Initializable {

    private final RayTracerAPI api = new RayTracerAPI();    
    private final RenderDisplay display = new RenderDisplay();
    private final Camera camera = new Camera(new Point3f(0, 0, 4), new Point3f(), new Vector3f(0, 1, 0), 45);
    private final ImageSampler renderer = new SimpleRenderer();
    
    //Update frame buffer and do other stuff
    private TimerExecution timerControl = null;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */    
       
    @FXML
    Tab viewPort;
    @FXML
    Button renderButton;
    @FXML
    Button pauseButton;
    @FXML
    Button stopButton;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
                
        viewPort.setContent(display);
                
        api.createScene(CornellSceneDescription.getPrimitives());        
        api.setCamera(camera);
        api.setImageSize(600, 600);
        api.setRenderer(renderer);       
    } 
    
    public void render(ActionEvent e)
    {
        if(!api.render(display))
            return; 
        
        timerControl = new TimerExecution(1, 5, TimeUnit.SECONDS);
        timerControl.execute(() -> {
            renderer.updateDisplay();
        });
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
                    if(timerControl != null)
                        timerControl.pause();
                    break;
                case "Resume":
                    api.resume();
                    b.setText("Pause");
                    if(timerControl != null)
                        timerControl.resume();
                    break;                
            }
        }
        
        
    }
    
    public void resume(ActionEvent e)            
    {
        api.resume();
        
        if(timerControl != null)
            timerControl.resume();
    }
    
    public void stop(ActionEvent e)
    {
        api.stop();
        Button b = (Button)e.getSource();
        switch (b.getText()) 
        {
            case "Stop":
                pauseButton.setText("Pause");                
                break;
        }
        
        if(timerControl != null)
            timerControl.shutDown();
    }
    
    public void close()
    {
        api.stop();
        
        if(timerControl != null)
            timerControl.shutDown();
    }
    
}
