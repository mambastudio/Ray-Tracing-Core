/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class Main extends Application
{    
    RayTraceCoreController controller = null;
    
    public static void main(String... args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RayTraceCore.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        
        Scene scene = new Scene(root); 
        
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.setTitle("Ray Tracer Core");
        primaryStage.show();
                
        primaryStage.setOnCloseRequest(evt -> {
            //do changes in code when
        });        
    }
    
    @Override
    public void stop()
    {
        controller.stop(null);
    }
    
}
