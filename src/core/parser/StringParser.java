/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class StringParser {
    private BufferedReader bf;
    private String lineTokens[];
    private int index;
    
    public StringParser(String directory, String file)
    {
        Path path = FileSystems.getDefault().getPath(directory, file);
        try 
        {
            bf = Files.newBufferedReader(path, Charset.defaultCharset());
        } 
        catch (IOException ex)
        {
            Logger.getLogger(StringParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        lineTokens = new String[0];
        index = 0;
    }
    
    public StringParser(URI uri)
    {
        File file = new File(uri);
        Path path = file.toPath();
        
        try 
        {
            bf = Files.newBufferedReader(path, Charset.defaultCharset());
        } 
        catch (IOException ex)
        {
            Logger.getLogger(StringParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        lineTokens = new String[0];
        index = 0;
    }
    
    public void close()
    {
        try 
        {
            bf.close();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(StringParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void skipTokens(int value)
    {
        String string = null;
        
        for(int i = 0; i<value; i++)
        {
            string = getNextToken();
            if(string == null)
                return;
        }
    }
    
    public String getNextToken()
    {
        while(true)
        {
            String tok = fetchNextToken();
            if (tok == null)
                return null;
            else
                return tok;
        }
    }
    
    public boolean getNextBoolean()
    {
        return Boolean.valueOf(getNextToken());
    }

    public int getNextInt() 
    {
        return Integer.parseInt(getNextToken());
    }

    public float getNextFloat() 
    {
        return Float.parseFloat(getNextToken());
    }
            
    public boolean hasNext()
    {
        return peekNextToken() != null;
    }
    
    public String peekNextToken()
    {
        while(true)
        {
            String t = fetchNextToken();
            if (t == null)
                return null; // nothing left
            else
            {
                index--;
                return t;
            }
        }
    }
    
    public boolean peekNextTokenNumber()
    {
        String str = peekNextToken();
        return str.matches("-?\\d+(\\.\\d+)?");
    }
    
    private String fetchNextToken()
    {
        if (bf == null)
            return null;
        while (true) 
        {            
            if (index < lineTokens.length)
                return lineTokens[index++];
            else if (!getNextLine())
                return null;
        }
    }
    
    private boolean getNextLine()
    {
        String line = null;
        while(true)
        {
            try 
            {
                line = bf.readLine();                
            } 
            catch (IOException ex)
            {
                Logger.getLogger(StringParser.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (line == null)
                return false;
            
            if(line.isEmpty())
                continue;
            
            lineTokens = line.trim().split("\\s+");            
           
            index = 0;
            return true;
        }
    }
}
