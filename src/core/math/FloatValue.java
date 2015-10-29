/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.math;

/**
 *
 * @author user
 */
public class FloatValue 
{
    public FloatValue(float value)
    {
        this.value = value;
    }
    
    public FloatValue()
    {
        value = 0;
    }
    
    public float value;
    
    public float getValue()
    {
        return value;
    }
    
    public void setValue(float value)
    {
        this.value = value;
    }
    
    @Override
    public String toString()
    {
        return "" +value;
    }
}
