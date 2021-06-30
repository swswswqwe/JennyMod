package com.schnurritv.sexmod.util.Handlers;

import net.minecraftforge.common.*;
import com.schnurritv.sexmod.events.*;
import com.schnurritv.sexmod.gui.*;

public class EventHandler
{
    public static void registerEvents() {
        MinecraftForge.EVENT_BUS.register((Object)new NoDamageForGirlsWhileHavingTheSex());
        MinecraftForge.EVENT_BUS.register((Object)new RemoveEntityFromList());
        MinecraftForge.EVENT_BUS.register((Object)new HandlePlayerMovement());
        MinecraftForge.EVENT_BUS.register((Object)new PreloadModels());
        MinecraftForge.EVENT_BUS.register((Object)new SetFOVForSex());
        MinecraftForge.EVENT_BUS.register((Object)new SexUI());
        MinecraftForge.EVENT_BUS.register((Object)new BlackScreenUI());
    }
}
