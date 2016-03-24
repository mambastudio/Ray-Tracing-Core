/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.math;

import core.coordinates.Point2f;
import core.coordinates.Vector3f;
import static core.math.Utility.acosf;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 *
 * @author user
 */
public class Utility {
    public static final float PI_F = (float)Math.PI;
    public static final float HALF_PI_F = (float)PI_F/2f;
    public static final float PI_F_TWO = PI_F * PI_F;
    public static final float TWO_PI_F = 2 * PI_F;
    public static final float INV_PI_F = 1.f/PI_F;
    public static final float INV_TWO_PI_F = 1.f/ (2 * PI_F);
    public static final float EPS_COSINE = 1e-6f;
    public static final float EPS_PHONG = 1e-3f;
    public static final float EPS_RAY  =  0.000001f;
    
    public static Vector3f sphericalDirectionY(float theta, float phi)
    {
        float x = (float) (cos(phi) * sin(theta));
        float y = (float) sin(phi);
        float z = (float) (-cos(phi) * cos(theta));
              
        return new Vector3f(x, y, z);
    }
    
    public static Vector3f sphericalDirection(float theta, float phi)
    {
        float x = (float) (cos(phi) * sin(theta));
        float y = (float) (sin(phi) * sin(theta));
        float z = (float) cos(theta);
        
        return new Vector3f(x, y, z);
    }
    
    public static float sphericalTheta(Vector3f v)
    {
        return acosf(clamp(v.z, -1, 1));
    }
    
    public static float sphericalPhi(Vector3f v)
    {
        float p = atan2f(v.y, v.x);
        return (p < 0.f) ? p + 2.f * PI_F : p;
    }
    
    public static float toDegrees(double radians)
    {
        return (float)Math.toDegrees(radians);
    }
    
    public static float toRadians(double degrees)
    {
        return (float)Math.toRadians(degrees);
    }
    
    public static float acosf(float a)
    {
        return (float)Math.acos(a);
    }
    
    public static float asinf(float a)
    {
        return (float)Math.asin(a);
    }
    
    public static float tanf(float a)
    {
        return (float)Math.tan(a);
    }
    
    public static float atanf(float a)
    {
        return (float)Math.atan(a);
    }
    
    public static float atan2f(float a, float b)
    {
        return (float)Math.atan2(a, b);
    }
    
    public static float expf(float a)
    {
        return (float)Math.exp(a);
    }
    
    public static float check(float value)
    {
        if(Float.isInfinite(value)||Float.isNaN(value))
            return 0f;
        else
            return value;
    }
    
    public static float clamp(float x, float min, float max)
    {
        if (x > max)
            return max;
        if (x > min)
            return x;
        return min;
    }
    
    public static int clamp(int x, int min, int max)
    {
        if (x > max)
            return max;
        if (x > min)
            return x;
        return min;
    }

    public static int max(int a, int b, int c) 
    {
        if (a < b)
            a = b;
        if (a < c)
            a = c;
        return a;
    }

    public static float max(float a, float b, float c) 
    {
        if (a < b)
            a = b;
        if (a < c)
            a = c;
        return a;
    }
    
    
    public static boolean isBad(float value)
    {
        if(Float.isInfinite(value))
            return true;
        else return Float.isNaN(value);
    }
    
    public static void debugValue(float value)
    {
        if(Float.isInfinite(value))
            System.out.println("Infinite value detected");
        else if(Float.isNaN(value))
            System.out.println("Nan value detected");
    }
    
    public static void debugArray(float... array)
    {
        for(float value : array)
        {
            debugValue(value);
        }
    }
    
    public static void debugArray(float[][] array)
    {
        for(int i = 0; i<array[i].length; i++)
            debugArray(array[i]);
    }
    
    
    public static float sqr(float value)
    {
        return value * value;
    }
    
    public static float sqrt(float value)
    {
        return (float)Math.sqrt(value);
    }
    
    public static float fresnelDielectric(float cosIncident,
                                            float ior)
    {
        if(ior < 0)
            return 1.f;
        
        float etaIncOverEtaTrans;
        
        if(cosIncident < 0.f)
        {
            cosIncident = -cosIncident;
            etaIncOverEtaTrans = ior;
        }
        else
        {
            etaIncOverEtaTrans = 1.f / ior;
        }
        
        float sinTrans2 = sqr(etaIncOverEtaTrans * etaIncOverEtaTrans) * (1.f - sqr(cosIncident));
        float cosTrans = (float) Math.sqrt(Math.max(0.f, 1.f - sinTrans2));

        float term1 = etaIncOverEtaTrans * cosTrans;
        float rParallel =
            (cosIncident - term1) / (cosIncident + term1);

        float term2 = etaIncOverEtaTrans * cosIncident;
        float rPerpendicular =
            (term2 - cosTrans) / (term2 + cosTrans);

        return 0.5f * (sqr(rParallel) + sqr(rPerpendicular));
    }
    
    public static float fresnelSchlick(float n1, float n2, float cosThetaI)
    {
        if(n2 < 0) return 1f;
        
        float r0 = (n1 - n2)/(n1 + n2);
        r0 *= r0;
        float cosX = cosThetaI;
        
        if(n1 > n2)
        {
            float n = n1/n2;
            float sinT2 = n * n * (1 - cosX*cosX);
            
            if(sinT2 > 1f) return 1f; //TIR
            
            cosX = sqrt(1f - sinT2);
        }
        
        float x = 1f - cosX;
        return r0 + (1f - r0) * x * x * x * x * x;
    }
    
    public static Vector3f sampleCosineHemisphereW(float r1, float r2, FloatValue pdfW)
    {
        float phi = 2 * PI_F * r1;
        float theta = acosf(sqrtf(r2));
        
        Vector3f ret = sphericalDirection(theta, phi);
        
        if(pdfW != null)        
            pdfW.value = ret.z * INV_PI_F;
       
        return ret;
    }
    
    public static float cosHemispherePdfW(
        Vector3f  aNormal,
        Vector3f  aDirection)
    {
        return Math.max(0.f, Vector3f.dot(aNormal, aDirection)) * INV_PI_F;
    }
        
    public static Vector3f samplePowerCosHemisphereW(float r1, float r2, float power, FloatValue pdfW)
    {
        float a = 1f/(1+power);
        
        float phi = 2 * PI_F * r1;
        float theta = acosf(powf(r2, a));
        
        Vector3f ret = sphericalDirection(theta, phi);
        
        if(pdfW != null)
            pdfW.value = (power + 1.f) * powf(ret.z, power) * (0.5f * INV_PI_F);
        
        return ret;
    }
    
    public static float pdfPowerCosHemisphereW(Vector3f n, Vector3f w, float power)
    {
        float cosTheta = Math.max(0f, Vector3f.dot(n, w));
        return (float) ((power + 1.f) * Math.pow(cosTheta, power) * (0.5f * INV_PI_F));
    }
    
    public static float pdfPowerCosHemisphereW(float cosThetaFactor, float power)
    {      
        float cosTheta = Math.max(0f, cosThetaFactor);
        return (float) ((power + 1.f) * Math.pow(cosTheta, power) * (0.5f * INV_PI_F));
    }
    
    public static Vector3f sampleUniformSphereW(Point2f sample, FloatValue pdfW)
    {
        float term1 = 2.f * PI_F * sample.x;
        float term2 = (float) (2.f * Math.sqrt(sample.y - sample.y * sample.y));

        Vector3f ret = new Vector3f(
            (float)Math.cos(term1) * term2,
            (float)Math.sin(term1) * term2,
            1.f - 2.f * sample.y);

        if(pdfW != null)
        {
            //*oPdfSA = 1.f / (4.f * PI_F);
            pdfW.value = INV_PI_F * 0.25f;
        }

        return ret;
    }
    
    public static float concentricDiscPdfA()
    {
        return INV_PI_F;
    }
    
    public static float uniformSpherePdfW()
    {
        //return (1.f / (4.f * PI_F));
        return INV_PI_F * 0.25f;
    }
    
    public static Point2f sampleConcentricDisc(float r1, float r2)
    {
        float phi, r;

        float a = 2*r1 - 1;   /* (a,b) is now on [-1,1]^2 */
        float b = 2*r2 - 1;

        if(a > -b)      /* region 1 or 2 */
        {
            if(a > b)   /* region 1, also |a| > |b| */
            {
                r = a;
                phi = (PI_F/4.f) * (b/a);
            }
            else        /* region 2, also |b| > |a| */
            {
                r = b;
                phi = (PI_F/4.f) * (2.f - (a/b));
            }
        }
        else            /* region 3 or 4 */
        {
            if(a < b)   /* region 3, also |a| >= |b|, a != 0 */
            {
                r = -a;
                phi = (PI_F/4.f) * (4.f + (b/a));
            }
            else        /* region 4, |b| >= |a|, but a==0 and b==0 could occur. */
            {
                r = -b;

                if (b != 0)
                    phi = (PI_F/4.f) * (6.f - (a/b));
                else
                    phi = 0;
            }
        }

        Point2f res = new Point2f();
        res.x = (float) (r * Math.cos(phi));
        res.y = (float) (r * Math.sin(phi));
        return res;
    }
    
    public static Vector3f sampleUniformConeW(float r1, float r2, float thetaMax, FloatValue pdfW)
    {
        float phi = 2 * PI_F * r1;
        float theta = acosf(1 - r2 * (1 - cosf(thetaMax)));
        
        if(pdfW != null)
            pdfW.value = pdfUniformConePdfW(thetaMax);
        
        return sphericalDirection(theta, phi);
    }
    
    public static float pdfUniformConePdfW(float thetaMax)
    {
        return 1f/(2 * PI_F *(1 - cosf(thetaMax)));
    }
        
    // reflect vector through (0,0,1)
    public static Vector3f reflectLocal(Vector3f w)
    {
        return new Vector3f(-w.x, -w.y, w.z);
    }
    
    public static float cosTheta(Vector3f w)
    {
        return w.z;
    }
    
    public static float absCosTheta(Vector3f w)
    {
        return Math.abs(cosTheta(w));
    }
    
    public static float sinTheta2(Vector3f w)
    {
        return Math.max(0f, 1.f - cosTheta(w) * cosTheta(w));
    }
    
    public static float sinTheta(Vector3f w)
    {
        return (float) Math.sqrt(sinTheta2(w));
    }
    
    public static float cosPhi(Vector3f w) 
    {
        float sintheta = sinTheta(w);
        if (sintheta == 0.f) return 1.f;
        return clamp(w.x / sintheta, -1.f, 1.f);
    }
    
    float sinPhi(Vector3f w) 
    {
        float sintheta = sinTheta(w);
        if (sintheta == 0.f) return 0.f;
        return clamp(w.y / sintheta, -1.f, 1.f);
    }
    
    public static boolean quadratic(float A, float B, float C, float[] t)
    {
        // Find Quadratic discriminant
        float discrim = B * B - 4.f * A * C;
        if (discrim <= 0.) {
            return false;
        }
        float rootDiscrim = (float) sqrt(discrim);

        // Compute Quadratic _t_ values
        float q;
        if (B < 0) {
            q = -.5f * (B - rootDiscrim);
        } else {
            q = -.5f * (B + rootDiscrim);
        }
        t[0] = q / A;
        t[1] = C / q;
        if (t[0] > t[1]) {
            float swap = t[0];
            t[0] = t[1];
            t[1] = swap;
        }
        return true;
    }
    
    public static float radians(float deg) {
        return ((float) PI / 180.f) * deg;
    }

    public static float degrees(float rad) {
        return (180.f / (float) PI) * rad;
    }
    
    public static float lerp(float t, float v1, float v2) {
        return (1f - t) * v1 + t * v2;
    }
    
    public static float sqrtf(float f)
    {
        return (float)Math.sqrt(f);
    }
    
    public static float cosf(float f)
    {
        return (float)Math.cos(f);
    }
    
    public static float sinf(float f)
    {
        return (float)Math.sin(f);
    }
    
    
    public static float powf(float a, float b)
    {
        return (float)Math.pow(a, b);
    }
}
