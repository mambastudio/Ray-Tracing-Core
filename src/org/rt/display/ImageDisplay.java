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
package org.rt.display;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.rt.core.AbstractBitmap;
import org.rt.core.AbstractDisplay;
import org.rt.core.color.Color;

/**
 *
 * @author user
 */
public class ImageDisplay extends StackPane implements AbstractDisplay
{
    ImageView imageView;
    AbstractBitmap bitmap;
    
    @Override
    public void imageBegin() 
    {
        imageView = new ImageView();
        getChildren().add(imageView);
    }

    @Override
    public void imageBegin(int width, int height) {
        imageView = new ImageView();
        getChildren().add(imageView);
    }

    @Override
    public void imageUpdate(float scale) {
        imagePaint();
    }

    @Override
    public void imagePaint() {
        if(bitmap != null)
            imageView.setImage(bitmap.getImage());
    }

    @Override
    public void imageFill(Color[] c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imageFill(float x, float y, Color c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void imageFill(AbstractBitmap bitmap) {
        this.bitmap = bitmap;
        imagePaint();
    }

    @Override
    public void imageClear() {
        imageView.setImage(null);
    }

    @Override
    public int getImageWidth() {
        if(bitmap != null)
            return bitmap.getWidth();
        else
            return 0;
    }

    @Override
    public int getImageHeight() {
        if(bitmap != null)
            return bitmap.getHeight();
        else
            return 0;
    }
    
}
