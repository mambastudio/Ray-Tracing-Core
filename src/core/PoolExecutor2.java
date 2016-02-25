/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.thread.KernelExecution;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class PoolExecutor2 
{
    protected static ArrayList<KernelExecution> progressiveRender = new ArrayList<>();
    
    //Useful for executing small processes in separate thread, once submitted
    //user has no control over it, hence care must be taken care of in terms
    //of executing resouce consuming and long tasks
    public static void executeThread(Runnable runnable)
    {
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
