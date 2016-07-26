/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.image.reader;

import org.rt.core.image.HDR;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author user
 */
public class HDRBitmapReader 
{
    public static final HDR load(File file)
    {
        return load(file.toURI());
    }
    
    public static final HDR load(URI uri)
    {
        if(!isHDRFile(uri.toString())) return null;
        
        // load radiance rgbe file
        FileInputStream f = null;
        try
        {
            f = new FileInputStream(new File(uri));
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(HDRBitmapReader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        // parse header
        boolean parseWidth = false, parseHeight = false;
        
        int width = 0, height = 0;
        
        int last = 0;
        while (width == 0 || height == 0 || last != '\n')
        {
            int n = read(f);
                        
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
        int[] pixels = new int[width * height];
        if ((width < 8) || (width > 0x7fff))
        {
            // run length encoding is not allowed so read flat
            readFlatRGBE(f, pixels, 0, width * height);
            return new HDR(width, height, pixels);
        }
        int rasterPos = 0;
        int numScanlines = height;
        int[] scanlineBuffer = new int[4 * width];
        while (numScanlines > 0) 
        {
            int r = read(f);
            int g = read(f);
            int b = read(f);
            int e = read(f);
            if ((r != 2) || (g != 2) || ((b & 0x80) != 0)) 
            {
                // this file is not run length encoded
                pixels[rasterPos] = (r << 24) | (g << 16) | (b << 8) | e;
                readFlatRGBE(f, pixels, rasterPos + 1, width * numScanlines - 1);
                return new HDR(width, height, pixels);
            }

            if (((b << 8) | e) != width) 
            {
                System.out.println("Invalid scanline width");
                return new HDR(width, height, pixels);
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
                    int b0 = read(f);
                    int b1 = read(f);
                    if (b0 > 128) 
                    {
                        // a run of the same value
                        int count = b0 - 128;
                        if ((count == 0) || (count > (end - p))) 
                        {
                            System.out.println("Bad scanline data - invalid RLE run");
                            return new HDR(width, height, pixels);
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
                            return new HDR(width, height, pixels);
                        }
                        scanlineBuffer[p] = b1;
                        p++;
                        if (--count > 0) 
                        {
                            for (int x = 0; x < count; x++)
                                scanlineBuffer[p + x] = read(f);
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
        try {
            f.close();
        } catch (IOException ex) {
            Logger.getLogger(HDRBitmapReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new HDR(width, height, pixels);
    }
    
    public static final HDR load(String uri)
    {
        if(!isHDRFile(uri)) return null;
        
        // load radiance rgbe file
        FileInputStream f = null;
        try
        {
            f = new FileInputStream(uri);
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(HDRBitmapReader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        // parse header
        boolean parseWidth = false, parseHeight = false;
        
        int width = 0, height = 0;
        
        int last = 0;
        while (width == 0 || height == 0 || last != '\n')
        {
            int n = read(f);
                        
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
        int[] pixels = new int[width * height];
        if ((width < 8) || (width > 0x7fff))
        {
            // run length encoding is not allowed so read flat
            readFlatRGBE(f, pixels, 0, width * height);
            return new HDR(width, height, pixels);
        }
        int rasterPos = 0;
        int numScanlines = height;
        int[] scanlineBuffer = new int[4 * width];
        while (numScanlines > 0) 
        {
            int r = read(f);
            int g = read(f);
            int b = read(f);
            int e = read(f);
            if ((r != 2) || (g != 2) || ((b & 0x80) != 0)) 
            {
                // this file is not run length encoded
                pixels[rasterPos] = (r << 24) | (g << 16) | (b << 8) | e;
                readFlatRGBE(f, pixels, rasterPos + 1, width * numScanlines - 1);
                return new HDR(width, height, pixels);
            }

            if (((b << 8) | e) != width) 
            {
                System.out.println("Invalid scanline width");
                return new HDR(width, height, pixels);
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
                    int b0 = read(f);
                    int b1 = read(f);
                    if (b0 > 128) 
                    {
                        // a run of the same value
                        int count = b0 - 128;
                        if ((count == 0) || (count > (end - p))) 
                        {
                            System.out.println("Bad scanline data - invalid RLE run");
                            return new HDR(width, height, pixels);
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
                            return new HDR(width, height, pixels);
                        }
                        scanlineBuffer[p] = b1;
                        p++;
                        if (--count > 0) 
                        {
                            for (int x = 0; x < count; x++)
                                scanlineBuffer[p + x] = read(f);
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
        
        return new HDR(width, height, pixels);
    }
    
    public static int read(FileInputStream f)
    {
        try {
            return f.read();
        } catch (IOException ex) {
            Logger.getLogger(HDRBitmapReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    private static void readFlatRGBE(FileInputStream f, int [] pixels, int rasterPos, int numPixels)
    {
        while (numPixels-- > 0)
        {
            int r = read(f);
            int g = read(f);
            int b = read(f);
            int e = read(f);
            pixels[rasterPos] = (r << 24) | (g << 16) | (b << 8) | e;
            rasterPos++;
        }
    }
    
    public static boolean isHDRFile(String uri)
    {
        return uri.toLowerCase().endsWith(".hdr");
    }
}
