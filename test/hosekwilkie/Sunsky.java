/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hosekwilkie;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class Sunsky extends Application
{
    HosekWilkieController controller = null;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HosekWilkie.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        controller.setStage(primaryStage);        
        
        Scene scene = new Scene(root); 
        primaryStage.setTitle("Hosek Wilkie");
        primaryStage.setScene(scene);       
        primaryStage.show();          
    }
    
    @Override
    public void stop()
    {
        controller.close(null);
    }
    
}
