/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.image;

import core.math.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;

/**
 *
 * @author user
 */
public class Bitmap {
    private int[] pixels = null;
    private int width = 0;
    private int height = 0;
    private boolean isHDR = false;
    
    public Bitmap(String filename) throws IOException 
    {
        // regular image, load using Java api
        BufferedImage bi = ImageIO.read(new File(filename));
        width = bi.getWidth();
        height = bi.getHeight();
        isHDR = false;
        pixels = new int[width * height];
        for (int y = 0, index = 0; y < height; y++) {
            for (int x = 0; x < width; x++, index++) {
                int rgb = bi.getRGB(x, height - 1 - y);
                pixels[index] = rgb;
            }
        }
    }
    
    public Bitmap(int w, int h, boolean isHDR) {
        width = w;
        height = h;
        this.isHDR = isHDR;
        pixels = new int[w * h];
    }
    
    public void setPixel(int x, int y, Color c) {
        if ((x >= 0) && (x < width) && (y >= 0) && (y < height))
            pixels[(y * width) + x] = isHDR ? c.toRGBE() : c.toRGB();
    }

    public Color getPixel(int x, int y) {
        if ((x >= 0) && (x < width) && (y >= 0) && (y < height))
            return isHDR ? new Color().setRGBE(pixels[(y * width) + x]) : new Color(pixels[(y * width) + x]);
        return Color.BLACK;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    private void savePNG(String filename) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                bi.setRGB(x, height - 1 - y, isHDR ? getPixel(x, y).toRGB() : pixels[(y * width) + x]);
        try 
        {
            ImageIO.write(bi, "png", new File(filename));
        } 
        catch (IOException e) 
        {
            System.err.println("Error in saving image");
        }
    }
    
    public void save(String filename) 
    {
        if (filename.endsWith(".png"))
            savePNG(filename);        
    }
    
    public ImageView getImageView()
    {
        return null;
    }
    
    public Image getImageFx()
    {
        return null;
    }
}
