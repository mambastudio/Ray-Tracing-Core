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
package org;

import org.rt.core.Camera;
import org.rt.core.ImageSampler;
import org.rt.RayTracerAPI;
import org.rt.core.coordinates.Point3f;
import org.rt.core.coordinates.Vector3f;
import org.rt.display.RenderDisplay;
import org.rt.core.render.SimpleRenderer;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import org.rt.core.scene.CornellSingleSphere;
import org.rt.core.system.UI;
import static org.rt.core.system.UI.Module.DISP;
import org.rt.core.system.UserInterface;
import org.rt.core.system.ui.SimpleInterface;
import org.rt.thread.TimerExecution;

/**
 * FXML Controller class
 *
 * @author user
 */
public class RayTraceCoreController implements Initializable {

    private final RayTracerAPI api = new RayTracerAPI();    
    private final RenderDisplay display = new RenderDisplay(false);
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
    
    @FXML
    Label renderSize;
    
    
    SimpleInterface sinterface;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
                
        viewPort.setContent(display);
                
        api.setScene(new CornellSingleSphere());        
        api.setCamera(camera);
        api.setImageSize(600, 600);
        api.setRenderer(renderer);     
        //api.setBackroundLight(new BackgroundLight(Color.WHITE));
        
        sinterface = new SimpleInterface();
        UI.set(sinterface);
        
        bind();
        
    } 
    
    public void bind()
    {
        renderSize.textProperty().bind(sinterface.displaySize);
    }
    
    public void render(ActionEvent e)
    {                
        sinterface.print(DISP, "600 x 600");
        
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
