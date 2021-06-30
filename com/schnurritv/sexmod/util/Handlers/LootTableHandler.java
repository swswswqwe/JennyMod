package com.schnurritv.sexmod.util.Handlers;

import net.minecraft.util.*;
import net.minecraft.world.storage.loot.*;

public class LootTableHandler
{
    public static final ResourceLocation GIRL;
    
    static {
        GIRL = LootTableList.register(new ResourceLocation("sexmod", "girl"));
    }
}
