package com.schnurritv.sexmod.girls.ellie;

import com.schnurritv.sexmod.girls.*;
import net.minecraft.util.*;

public class EllieModel extends GirlModel
{
    ResourceLocation[] models;
    
    public EllieModel() {
        this.models = new ResourceLocation[] { new ResourceLocation("sexmod", "geo/ellie/nude.geo.json"), new ResourceLocation("sexmod", "geo/ellie/dressed.geo.json") };
    }
    
    @Override
    public ResourceLocation getModel(final int whichOne) {
        if (whichOne > this.models.length) {
            System.out.println("Ellie doesn't have an outfit Nr." + whichOne + " so im just making her nude lol");
            return this.models[0];
        }
        return this.models[whichOne];
    }
    
    @Override
    public ResourceLocation getSkin() {
        return new ResourceLocation("sexmod", "textures/entity/ellie/ellie.png");
    }
    
    @Override
    public ResourceLocation getAnimationFile() {
        return new ResourceLocation("sexmod", "animations/ellie/ellie.animation.json");
    }
    
    @Override
    public String getSkinName() {
        return "ellieskin";
    }
}
