/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import org.rt.thread.TimerExecution;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class Test {
    public static void main(String... args)
    {
        AtomicReference<CustomString> atomicString = new AtomicReference<>(new CustomString("Wewe"));
        CustomString string = atomicString.get();
        string.set("aljdfal");
        System.out.println(atomicString);
        System.out.println(string);
    }
    
    static class CustomString
    {
        String string;
        
        CustomString(String string)
        {
            this.string = string;
        }
        
        public void set(String string)
        {
            this.string = string;
        }
        
        @Override
        public String toString()
        {
            return string;
        }
    }
}
