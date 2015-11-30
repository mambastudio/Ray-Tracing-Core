/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 *
 * @author user
 */
public class ScheduledExecutor 
{
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
}
