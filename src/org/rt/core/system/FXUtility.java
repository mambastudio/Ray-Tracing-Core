/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.system;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

/**
 *
 * @author user
 */
public class FXUtility 
{
    private static final SimpleObjectProperty<File> lastKnownDirectoryProperty = new SimpleObjectProperty<>();
    private static FileChooser instance = null;
    
    public enum FilterMode {
        //Setup supported filters
        PNG_FILES("png files (*.png)", "*.png"),
        TXT_FILES("txt files (*.txt)", "*.txt");

        private final ExtensionFilter extensionFilter;

        FilterMode(String extensionDisplayName, String... extensions){
            extensionFilter = new ExtensionFilter(extensionDisplayName, extensions);
        }

        public ExtensionFilter getExtensionFilter(){
            return extensionFilter;
        }
    }

    public static void bind(BooleanProperty value1, BooleanProperty value2)
    {
        value1.bind(value2);        
    }
    
    public static void bind(BooleanProperty value, BooleanProperty value1, BooleanProperty value2)
    {
        value.bind(Bindings.or(value1, value2));
    }
    
    public static void bind(BooleanProperty value1, BooleanBinding value2)
    {
        value1.bind(value2);        
    }
    
     public static void bindBidirectional(BooleanProperty value1, BooleanProperty value2)
    {
        value1.bindBidirectional(value2);        
    }
     
    public static void bind(StringProperty value1, StringProperty value2)
    {
        value1.bind(value2);        
    }
    
     public static void bindBidirectional(StringProperty value1, StringProperty value2)
    {
        value1.bindBidirectional(value2);        
    }
    
    public static void bindBidirectional(DoubleProperty value1, DoubleProperty value2)
    {
        value1.bindBidirectional(value2);
    }
    
    public static void bind(DoubleProperty value1, DoubleProperty value2)
    {
        value1.bind(value2);        
    }
    
    public static void bindBidirectional(ObjectProperty value1, ObjectProperty value2)
    {
        value1.bindBidirectional(value2);
    }
    
    public static void bind(ObjectProperty value1, ObjectProperty value2)
    {
        value1.bind(value2);
    }
    
    
    public static void bindBidirectional(FloatProperty value1, DoubleProperty value2)
    {
        value1.bindBidirectional(value2);
    }
    
    public static void bind(FloatProperty value1, DoubleProperty value2)
    {
        value1.bind(value2);
    }
    
    public static void bindBidirectional(DoubleProperty value1, FloatProperty value2)
    {
        value1.bindBidirectional(value2);
    }
    
    public static void bind(DoubleProperty value1, FloatProperty value2)
    {
        value1.bind(value2);
    }
    
    public static void bindBidirectional(FloatProperty value1, FloatProperty value2)
    {
        value1.bindBidirectional(value2);
    }
    
    public static void bind(FloatProperty value1, FloatProperty value2)
    {
        value1.bind(value2);
    }
    
    public static void bind(StringProperty value1, FloatProperty value2, String format)
    {
        value1.bind(Bindings.format(format, value2));
    }
    
    public static void bind(StringProperty value1, DoubleProperty value2, String format)
    {
        value1.bind(Bindings.format(format, value2));
    }
    
    public static void bind(Spinner spinner, DoubleProperty value, double min, double max, double initialValue, double stepBy, boolean editBinding)
    {
        SpinnerValueFactory factory = new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initialValue, stepBy);
        spinner.setValueFactory(factory);
        value.bind(((SpinnerValueFactory.DoubleSpinnerValueFactory)factory).valueProperty());
        
        if(editBinding)
        {
            TextFormatter formatter = new TextFormatter(factory.getConverter(), factory.getValue());
            spinner.getEditor().setTextFormatter(formatter);
            factory.valueProperty().bindBidirectional(formatter.valueProperty());
        }
    }
    
    public static void bind(Spinner spinner, IntegerProperty value, int min, int max, int initialValue, int stepBy, boolean editBinding)
    {
        SpinnerValueFactory factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue, stepBy);
        spinner.setValueFactory(factory);
        value.bind(((SpinnerValueFactory.IntegerSpinnerValueFactory)factory).valueProperty());
        
        if(editBinding)
        {
            TextFormatter formatter = new TextFormatter(factory.getConverter(), factory.getValue());
            spinner.getEditor().setTextFormatter(formatter);
            factory.valueProperty().bindBidirectional(formatter.valueProperty());
        }
    }
    
    private static FileChooser getInstance(FilterMode... filterModes){
        if(instance == null) {
            instance = new FileChooser();
            instance.initialDirectoryProperty().bindBidirectional(lastKnownDirectoryProperty);
        }
        //Set the filters to those provided
        //You could add check's to ensure that a default filter is included, adding it if need be
        instance.getExtensionFilters().setAll(
                Arrays.stream(filterModes)
                        .map(FilterMode::getExtensionFilter)
                        .collect(Collectors.toList()));
        return instance;
    }
    
    private static FileChooser getInstance()
    {
        if(instance == null)
        {
            instance = new FileChooser();
            instance.initialDirectoryProperty().bindBidirectional(lastKnownDirectoryProperty);
            //Set the FileExtensions you want to allow
            instance.getExtensionFilters().setAll(new ExtensionFilter("png files (*.png)", "*.png"));
        }
        return instance;
    }
    
    public static File showSaveDialog()
    {
        return showSaveDialog(null);
    }

    public static File showSaveDialog(Window ownerWindow){
        File chosenFile = getInstance().showSaveDialog(ownerWindow);
        if(chosenFile != null){
            //Set the property to the directory of the chosenFile so the fileChooser will open here next
            lastKnownDirectoryProperty.setValue(chosenFile.getParentFile());
        }
        return chosenFile;
    }
    
    public static File showSaveDialog(Window ownerWindow, FilterMode... filterModes){
        File chosenFile = getInstance(filterModes).showSaveDialog(ownerWindow);
        if(chosenFile != null){
            lastKnownDirectoryProperty.setValue(chosenFile.getParentFile());
        }
        return chosenFile;
    }
}
