package com.schnurritv.sexmod.util.Handlers;

import com.schnurritv.sexmod.world.gen.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.fml.common.*;
import com.schnurritv.sexmod.*;
import net.minecraftforge.fml.common.network.*;

public class RegistryHandler
{
    public static void preInitRegistries() {
        GameRegistry.registerWorldGenerator((IWorldGenerator)new WorldGenCustomStructures(), 0);
        EntityInit.registerEntities();
        RenderHandler.registerEntityRenders();
        EventHandler.registerEvents();
    }
    
    public static void initRegistries() {
        SoundsHandler.registerSounds();
        NetworkRegistry.INSTANCE.registerGuiHandler((Object)Main.instance, (IGuiHandler)new GuiHandler());
        PacketHandler.registerMessages();
    }
}
