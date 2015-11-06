/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.bsdf.Diffuse;
import core.coordinates.Normal3f;
import core.coordinates.Vector3f;
import core.math.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author user
 */
public class Material {
    private final ArrayList<AbstractBSDF> bsdfList = new ArrayList<>();
    private String name = null;
    private final Random random = new Random();
    
    private Color emission = new Color();
    private float power = 10f;
    
    public Material()
    {
        
    }
    
    public Material(AbstractBSDF... bsdfs)
    {
        bsdfList.addAll(Arrays.asList(bsdfs));
    }
    
    public boolean isEmitter()
    {
        return !emission.isBlack();
    }
    
    public void setEmission(Color emission)
    {
        this.emission = emission;
    }
    
    public Color getEmission()
    {
        return emission;
    }
    
    public void setPower(float power)
    {
        this.power = power;
    }
    
    public float getPower()
    {
        return power;
    }
        
    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }
    
    private AbstractBSDF getRandomBsdf()
    {
        return bsdfList.get(random.nextInt(bsdfList.size()));
    }
    
    public AbstractBSDF getBSDF(Normal3f worldNormal, Vector3f worldWi)
    {
        AbstractBSDF bsdf = getRandomBsdf();
        bsdf.setUp(worldNormal, worldWi);
        return bsdf;
    }
    
    @Override
    public String toString()
    {
        return getName();
    }
    
    public void addBsdf(AbstractBSDF bsdf)
    {
        bsdfList.add(bsdf);
    }
    
    public void removeBSDF(AbstractBSDF bsdf)
    {
        if(bsdfList.contains(bsdf)) bsdfList.remove(bsdf);
    }
    
    public void clearList()
    {
        bsdfList.removeAll(bsdfList);
    }
    
    public ArrayList<AbstractBSDF> getList()
    {
        return bsdfList;
    }
    
    public static Material createLambert(Color color)
    {
        return new Material(new Diffuse(color));
    }
    
    public static Material createEmission()
    {
        Material material = new Material();
        material.setEmission(Color.WHITE);
        return material;
    }
}
