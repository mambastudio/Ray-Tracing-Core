/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.File;

/**
 *
 * @author user
 */
public class BitmapReader {
    public static boolean isLowDynamic(File file)
    {        
        return isLowDynamic(file.toURI().toString());
    }
    public static boolean isLowDynamic(String uri)
    {        
        return uri.toLowerCase().endsWith(".jpg") || uri.toLowerCase().endsWith(".png");
    }
    
    public static boolean isHighDynamic(File file)
    {
        return isHighDynamic(file.toURI().toString());
    }
    
    public static boolean isHighDynamic(String uri)
    {
        return uri.toLowerCase().endsWith(".hdr");
    }
}
