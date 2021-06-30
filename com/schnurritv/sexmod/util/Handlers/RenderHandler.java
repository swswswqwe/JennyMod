package com.schnurritv.sexmod.util.Handlers;

import net.minecraftforge.fml.client.registry.*;
import net.minecraft.client.renderer.entity.*;
import com.schnurritv.sexmod.girls.ellie.*;
import software.bernie.geckolib3.model.*;
import com.schnurritv.sexmod.girls.*;
import com.schnurritv.sexmod.girls.jenny.*;

public class RenderHandler
{
    public static void registerEntityRenders() {
        RenderingRegistry.registerEntityRenderingHandler((Class)JennyEntity.class, manager -> new GirlRenderer(manager, new JennyModel(), -0.15));
        RenderingRegistry.registerEntityRenderingHandler((Class)EllieEntity.class, manager -> new GirlRenderer(manager, new EllieModel(), 0.05));
    }
}
