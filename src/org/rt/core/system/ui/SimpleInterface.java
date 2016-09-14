/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.system.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.rt.core.system.UI.Module;
import static org.rt.core.system.UI.Module.DISP;
import org.rt.core.system.UserInterface;

/**
 *
 * @author user
 */
public class SimpleInterface implements UserInterface
{
    public StringProperty displaySize = new SimpleStringProperty("__");

    @Override
    public void print(Module m, String s) 
    {
        if(m == DISP)
            printDisplay(s);
    }
    
    private void printDisplay(String string)
    {
        displaySize.setValue(string);
    }
}
