package com.schnurritv.sexmod;

import com.schnurritv.sexmod.proxy.*;
import net.minecraftforge.fml.common.*;
import software.bernie.geckolib3.*;
import com.schnurritv.sexmod.util.Handlers.*;
import net.minecraftforge.fml.common.event.*;
import com.schnurritv.sexmod.girls.*;

@Mod(modid = "sexmod", name = "Sex Mod", version = "1.0", acceptedMinecraftVersions = "[1.12.2]")
public class Main
{
    @Mod.Instance
    public static Main instance;
    @SidedProxy(clientSide = "com.schnurritv.sexmod.proxy.ClientProxy", serverSide = "com.schnurritv.sexmod.proxy.CommonProxy")
    public static CommonProxy proxy;
    
    public Main() {
        GeckoLib.initialize();
    }
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        RegistryHandler.preInitRegistries();
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        RegistryHandler.initRegistries();
    }
    
    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
    }
    
    @Mod.EventHandler
    public static void clearEntityList(final FMLServerStoppedEvent event) {
        GirlEntity.girlEntities.clear();
    }
}
