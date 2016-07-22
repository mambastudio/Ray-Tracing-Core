/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import core.thread.TimerExecution;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class Test {
    public static void main(String... args)
    {
        TimerExecution execution = new TimerExecution(0, 5, 5, true, TimeUnit.SECONDS);
        execution.execute(new Update());
        
    }
    
    static class Update implements Runnable
    {
        int i = 0;
        @Override
        public void run() {
            i=i+5;
            System.out.println("i: " +i); 
            
        }
        
    }
}
