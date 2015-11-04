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
public class Normal3f extends Vector3<Normal3f>
{
    public Normal3f() {
        super();
    }

    public Normal3f(float x, float y, float z) {
        super(x,y,z);
    }

    public Normal3f(Vector3 v) {
        super(v);
    }
    
    public void set(Vector3 v)
    {
        x = v.x; y = v.y; z = v.z;
    }
}
