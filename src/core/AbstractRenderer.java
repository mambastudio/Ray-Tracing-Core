/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.thread.Threading;

/**
 *
 * @author user
 */
public abstract class AbstractRenderer extends Threading
{
    protected AbstractScene scene;
    
    public AbstractRenderer(AbstractScene scene)
    {
        this.scene = scene;
    }
    public abstract void runIteration(int aIteration);
}
