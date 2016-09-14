/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hosekwilkie;


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.rt.core.image.writer.JPGBitmapWriter;
import org.rt.core.system.FXUtility;
import org.rt.core.system.FXUtility.FilterMode;
import org.rt.display.ImageDisplay;

/**
 * FXML Controller class
 *
 * @author user
 */
public class HosekWilkieController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    Stage stage = null;
        
    DoubleProperty exposure = new SimpleDoubleProperty(0.01);
    DoubleProperty albedo = new SimpleDoubleProperty();
    DoubleProperty turbidity = new SimpleDoubleProperty();
    DoubleProperty elevation = new SimpleDoubleProperty();
    DoubleProperty tonemap = new SimpleDoubleProperty(3.2);
    
    @FXML
    Spinner elevationSpinner;
    @FXML
    Spinner turbiditySpinner;
    @FXML
    Spinner albedoSpinner;
    @FXML
    Spinner tonemapSpinner;
    @FXML
    Spinner exposureSpinner;
    @FXML
    Spinner sunsizeSpinner;
    @FXML
    BorderPane displayViewport;
    
    ImageDisplay display = new ImageDisplay();
    SkyRenderer renderer = new SkyRenderer();
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        elevationSpinner(elevationSpinner);
        turbiditySpinner(turbiditySpinner);
        albedoSpinner(albedoSpinner);
        tonemapSpinner(tonemapSpinner);
        exposureSpinner(exposureSpinner);
        sunsizeSpinner(sunsizeSpinner);
        
        exposure.addListener((observable, old_value, new_value) -> {         
            renderer.setExposure(exposure.doubleValue());
        });
        
        turbidity.addListener((observable, old_value, new_value) -> {         
            renderer.setTurbidity(turbidity.doubleValue());
        });
        
        albedo.addListener((observable, old_value, new_value) -> {         
            renderer.setAlbedo(albedo.doubleValue());
        });
        
        tonemap.addListener((observable, old_value, new_value) -> {         
            renderer.setTonemap(tonemap.doubleValue());
        });
        
        elevation.addListener((observable, old_value, new_value) -> {         
            renderer.setElevation(elevation.doubleValue());
        });
        
        displayViewport.setCenter(display);        
        renderer.render(display);
    }    
    
    
    public void exposureSpinner(Spinner spinner)
    {        
        SpinnerValueFactory factory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, 0.05, exposure.doubleValue(), 0.01);        
        spinner.setValueFactory(factory);
        exposure.bind(((SpinnerValueFactory.DoubleSpinnerValueFactory)factory).valueProperty());
    }
    
    public void turbiditySpinner(Spinner spinner)
    {
        SpinnerValueFactory factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, turbidity.intValue(), 1);
        spinner.setValueFactory(factory);
        turbidity.bind(((SpinnerValueFactory.IntegerSpinnerValueFactory)factory).valueProperty());
    }
    
    public void albedoSpinner(Spinner spinner)
    {
        SpinnerValueFactory factory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1, albedo.doubleValue(), 0.1);
        spinner.setValueFactory(factory);
        albedo.bind(((SpinnerValueFactory.DoubleSpinnerValueFactory)factory).valueProperty());
    }
    
    public void elevationSpinner(Spinner spinner)
    {
        SpinnerValueFactory factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 180, elevation.intValue(), 5);
        spinner.setValueFactory(factory);
        elevation.bind(((SpinnerValueFactory.IntegerSpinnerValueFactory)factory).valueProperty());
    }
    
    public void sunsizeSpinner(Spinner spinner)
    {
        
    }
    
    public void tonemapSpinner(Spinner spinner)
    {
        SpinnerValueFactory factory = new SpinnerValueFactory.DoubleSpinnerValueFactory(1.0, 4, tonemap.doubleValue(), 0.1);
        spinner.setValueFactory(factory);
        tonemap.bind(((SpinnerValueFactory.DoubleSpinnerValueFactory)factory).valueProperty());
    }
    
    public void setStage(Stage stage)
    {
        this.stage = stage;
    }
    
    public void close(ActionEvent e)
    {
        System.exit(0);
    }
    
    public void save(ActionEvent e)
    {       
        Window window = stage.getScene().getWindow();
        File file = FXUtility.showSaveDialog(window, FilterMode.PNG_FILES);
        
        if(file == null) return;
        
        new Thread(() -> {            
            int w = 1920; int h = 1080;            
            JPGBitmapWriter jpgImage = new JPGBitmapWriter();
            jpgImage.openFile(file.getAbsolutePath());
            jpgImage.writeFile(0, 0, w, h, renderer.getSky(w, h), null);            
        }).start();        
    }
    
    public void informationDialog(ActionEvent e)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Hosek Wilkie Renderer");
        alert.setContentText("TODO");
        alert.showAndWait();
    }
}
