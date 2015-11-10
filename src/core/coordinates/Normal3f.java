/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.coordinates;

/**
 *
 * @author user
 */
public class Normal3f extends Vector3f
{
    public Normal3f() {
        super();
    }

    public Normal3f(float x, float y, float z) {
        super(x,y,z);
    }

    public Normal3f(Vector3f v) {
        super(v);
    }
    
    @Override
    public Normal3f normalize()
    {
        return (Normal3f) super.normalize();
    }
    
    @Override
    public Normal3f neg()
    {
        return (Normal3f) super.neg();
    }
        
    @Override
    public Normal3f clone()
    {
        return new Normal3f(x, y, z);
    }
}
