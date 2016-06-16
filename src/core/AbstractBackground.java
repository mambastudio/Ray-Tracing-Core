/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.math.Transform;

/**
 *
 * @author user
 */
public abstract class AbstractBackground extends AbstractLight
{
    public AbstractBackground()
    {
        super(new Transform());
    }
}
