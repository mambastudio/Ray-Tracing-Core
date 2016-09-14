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
package org.rt.core.image.writer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import org.rt.core.AbstractBitmapWriter;
import org.rt.core.color.Color;
import org.rt.core.image.formats.BitmapBGRA;

/**
 *
 * @author user
 */
public class JPGBitmapWriter implements AbstractBitmapWriter
{
    String filename;
    
    @Override
    public void openFile(String filename) {
        this.filename = filename;
    }

    @Override
    public void writeFile(int x, int y, int w, int h, Color[] color, float[] alpha) {
        File file = new File(filename);   
        
        BitmapBGRA bitmap = new BitmapBGRA(w, h);
        bitmap.writeColor(color, alpha, x, y, w, h);
        
        Image image = bitmap.getImage();
        
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        try 
        {
            ImageIO.write(bImage, "png", file);
        } 
        catch (IOException io) 
        {
            throw new RuntimeException(io);
        }        
    }
}
