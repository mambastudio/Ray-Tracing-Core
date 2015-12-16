/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.image;

import core.color.Color;
import core.coordinates.Point2f;
import core.coordinates.Point2i;
import core.coordinates.Vector3f;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/**
 *
 * @author user
 */
public class HDR 
{
    private final String uri;
    private int width, height;
    private int [] pixels = null;
    
    public HDR(String uri)
    {
        //load file
        this.uri = uri;
        
        try {
            load(uri);
        } catch (IOException ex) {
            Logger.getLogger(HDR.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public final void load(String uri) throws IOException
    {
        if(!isHDRFile(uri)) return;
        
        // load radiance rgbe file
        FileInputStream f = new FileInputStream(uri);
        
        // parse header
        boolean parseWidth = false, parseHeight = false;
        
        int last = 0;
        while (width == 0 || height == 0 || last != '\n')
        {
            int n = f.read();
            switch (n)
            {
                case 'Y':
                    parseHeight = last == '-';
                    parseWidth = false;
                    break;
                case 'X':
                    parseHeight = false;
                    parseWidth = last == '+';
                    break;
                case ' ':
                    parseWidth &= width == 0;
                    parseHeight &= height == 0;
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    if (parseHeight)
                        height = 10 * height + (n - '0');
                    else if (parseWidth)
                        width = 10 * width + (n - '0');
                    break;
                default:
                    parseWidth = parseHeight = false;
                    break;
            }
            last = n;
        }
        
        // allocate image
        pixels = new int[width * height];
        if ((width < 8) || (width > 0x7fff))
        {
            // run length encoding is not allowed so read flat
            readFlatRGBE(f, pixels, 0, width * height);
            return;
        }
        int rasterPos = 0;
        int numScanlines = height;
        int[] scanlineBuffer = new int[4 * width];
        while (numScanlines > 0) 
        {
            int r = f.read();
            int g = f.read();
            int b = f.read();
            int e = f.read();
            if ((r != 2) || (g != 2) || ((b & 0x80) != 0)) 
            {
                // this file is not run length encoded
                pixels[rasterPos] = (r << 24) | (g << 16) | (b << 8) | e;
                readFlatRGBE(f, pixels, rasterPos + 1, width * numScanlines - 1);
                return;
            }

            if (((b << 8) | e) != width) 
            {
                System.out.println("Invalid scanline width");
                return;
            }
            int p = 0;
            // read each of the four channels for the scanline into
            // the buffer
            for (int i = 0; i < 4; i++) 
            {
                if (p % width != 0)
                    System.out.println("Unaligned access to scanline data");
                int end = (i + 1) * width;
                while (p < end) 
                {
                    int b0 = f.read();
                    int b1 = f.read();
                    if (b0 > 128) 
                    {
                        // a run of the same value
                        int count = b0 - 128;
                        if ((count == 0) || (count > (end - p))) 
                        {
                            System.out.println("Bad scanline data - invalid RLE run");
                            return;
                        }
                        while (count-- > 0)
                        {
                            scanlineBuffer[p] = b1;
                            p++;
                        }
                    } 
                    else
                    {
                        // a non-run
                        int count = b0;
                        if ((count == 0) || (count > (end - p)))
                        {
                            System.out.println("Bad scanline data - invalid count");
                            return;
                        }
                        scanlineBuffer[p] = b1;
                        p++;
                        if (--count > 0) 
                        {
                            for (int x = 0; x < count; x++)
                                scanlineBuffer[p + x] = f.read();
                            p += count;
                        }
                    }
                }
            }
            
            // now convert data from buffer into floats
            for (int i = 0; i < width; i++)
            {
                r = scanlineBuffer[i];
                g = scanlineBuffer[i + width];
                b = scanlineBuffer[i + 2 * width];
                e = scanlineBuffer[i + 3 * width];
                pixels[rasterPos] = (r << 24) | (g << 16) | (b << 8) | e;
                rasterPos++;
            }
            numScanlines--;
        }       
        
    }
    
    private void readFlatRGBE(FileInputStream f, int [] pixels, int rasterPos, int numPixels) throws IOException
    {
        while (numPixels-- > 0)
        {
            int r = f.read();
            int g = f.read();
            int b = f.read();
            int e = f.read();
            pixels[rasterPos] = (r << 24) | (g << 16) | (b << 8) | e;
            rasterPos++;
        }
    }
    
    public boolean isHDRFile(String uri)
    {
        return uri.toLowerCase().endsWith(".hdr");
    }
    
    public void setPixel(int x, int y, Color c)
    {
        if ((x >= 0) && (x < width) && (y >= 0) && (y < height))
            pixels[(y * width) + x] =  c.toRGBE();
    }

    public Color getColor(int x, int y)
    {
        if ((x >= 0) && (x < width) && (y >= 0) && (y < height))
            return new Color().setRGBE(pixels[(y * width) + x]);
        return Color.BLACK;
    }
    
    public Color getColor(Point2i uv)
    {
        return getColor(uv.x, uv.y);
    }

    public Color[] getArray()
    {
        Color color[] = new Color[width * height];

        for(int i = 0; i < pixels.length; i++)
            color[i] = new Color().setRGBE(pixels[i]);
        
        return color;
    }
    
    public float luminance(int u, int v)
    {
        return getColor(u, v).setRGBE(pixels[v*width + u]).luminance();
    }
    
    public Bitmap getBitmap()
    {
        Bitmap image = new Bitmap(width, height);
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                image.setRGB(x, y, getColor(x, y).toRGB());
        return image;
    }
    
    public Image getImage()
    {
        return getBitmap().getImage();
    }
    
    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
    
    public Color getColor(Vector3f d)
    {
        int u, v;
        
        // assume lon/lat format, here the y coordinate is up
        double phi = 0, theta = 0;
        phi = Math.acos(d.y);
        theta = Math.atan2(d.z, d.x);
        
        u = (int) ((0.5 - 0.5 * theta / Math.PI) * width);
        v = (int) ((phi / Math.PI) * height);
        
        return getColor(u, v);
    }
}
