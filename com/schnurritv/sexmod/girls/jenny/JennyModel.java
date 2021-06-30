package com.schnurritv.sexmod.girls.jenny;

import com.schnurritv.sexmod.girls.*;
import net.minecraft.util.*;

public class JennyModel extends GirlModel
{
    ResourceLocation[] models;
    
    public JennyModel() {
        this.models = new ResourceLocation[] { new ResourceLocation("sexmod", "geo/jenny/jennynude.geo.json"), new ResourceLocation("sexmod", "geo/jenny/jennydressed.geo.json") };
    }
    
    @Override
    public ResourceLocation getSkin() {
        return new ResourceLocation("sexmod", "textures/entity/jenny/jenny.png");
    }
    
    @Override
    public ResourceLocation getAnimationFile() {
        return new ResourceLocation("sexmod", "animations/jenny/jenny.animation.json");
    }
    
    @Override
    public String getSkinName() {
        return "jennyskin";
    }
    
    @Override
    public ResourceLocation getModel(final int whichOne) {
        if (whichOne > this.models.length) {
            System.out.println("Jenny doesn't have an outfit Nr." + whichOne + " so im just making her nude lol");
            return this.models[0];
        }
        return this.models[whichOne];
    }
}
