/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.math;

/**
 *
 * @author user
 */
public class Distribution1D {
    float [] func, cdf;
    float funcInt;
    int count;
    
    public Distribution1D(float [] f, int n)
    {
        count = n;
        func = new float[n];
        System.arraycopy(f, 0, func, 0, n);
        cdf = new float[n + 1];
        
        //Compute integral of step function at xi
        cdf[0] = 0;
        for(int i = 1; i < count+1; ++i)
            cdf[i] = cdf[i-1] + func[i-1] / n;
        
        //Transform step function integral into CDF
        funcInt = cdf[count];
        for (int i = 1; i < n+1; ++i)
            cdf[i] /= funcInt;
    }
}
