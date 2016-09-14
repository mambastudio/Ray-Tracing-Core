/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objviewerfx;

import org.rt.display.DynamicDisplay;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.rt.RayTracerAPI;
import org.rt.core.AbstractPrimitive;
import org.rt.core.Camera;
import org.rt.core.coordinates.Point3f;
import org.rt.core.coordinates.Vector3f;
import org.rt.core.math.Orientation;
import org.rt.core.parser.OBJParser;
import org.rt.core.scene.SphereScene;

/**
 * FXML Controller class
 *
 * @author user
 */
public class FXMLController implements Initializable {

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @FXML
    BorderPane border;
    
    private final RayTracerAPI api = new RayTracerAPI();    
    private final Camera camera = new Camera(new Point3f(0, 0, 4), new Point3f(), new Vector3f(0, 1, 0), 45);
    private final DynamicDisplay display = new DynamicDisplay();
    private final ShadingRenderer renderer = new ShadingRenderer();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        border.setCenter(display);        
        api.createScene(new SphereScene());        
        api.setCamera(camera);        
        api.setRenderer(renderer);
        api.render(display);
        
    }    
    
    
    public void open(ActionEvent e)
    {
        //http://stackoverflow.com/questions/13585590/how-to-get-parent-window-in-fxml-controller
        Window window = ((Node)e.getTarget()).getScene().getWindow();
        File file = launchSceneFileChooser(window);
        
        if(file != null)
        {
            ArrayList<AbstractPrimitive> primitives = OBJParser.read(file.toURI());           
            
            api.createScene(primitives);
            api.buildScene();
            
            Orientation.reposition(camera, api.getWorldBounds());
            renderer.reinit();
        }        
    }
    
    public File launchSceneFileChooser(Window window)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Wavefront obj", "*.obj"));
        File file = fileChooser.showOpenDialog(window);        
        return file;
    }
    
    public void close()
    {
        renderer.stop();
    }
}
