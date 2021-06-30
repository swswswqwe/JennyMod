package com.schnurritv.sexmod.util.Handlers;

import com.schnurritv.sexmod.girls.jenny.*;
import net.minecraft.entity.*;
import com.schnurritv.sexmod.girls.ellie.*;
import net.minecraft.util.*;
import com.schnurritv.sexmod.*;
import net.minecraftforge.fml.common.registry.*;

public class EntityInit
{
    public static void registerEntities() {
        registerEntity("jenny", (Class<? extends Entity>)JennyEntity.class, 177013, 50, 3286592, 12655237);
        registerEntity("ellie", (Class<? extends Entity>)EllieEntity.class, 228922, 50, 1447446, 9961472);
    }
    
    private static void registerEntity(final String name, final Class<? extends Entity> entity, final int id, final int range, final int color1, final int color2) {
        EntityRegistry.registerModEntity(new ResourceLocation("sexmod:" + name), (Class)entity, name, id, (Object)Main.instance, range, 1, true, color1, color2);
    }
}
