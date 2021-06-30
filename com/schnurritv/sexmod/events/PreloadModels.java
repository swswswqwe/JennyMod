package com.schnurritv.sexmod.events;

import net.minecraftforge.event.entity.living.*;
import net.minecraft.entity.player.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.world.*;

public class PreloadModels
{
    public boolean didIt;
    
    public PreloadModels() {
        this.didIt = false;
    }
    
    @SubscribeEvent
    public void PenisSauce(final LivingEvent.LivingUpdateEvent event) {
        if (!this.didIt && event.getEntity() instanceof EntityPlayer) {
            this.SpawnPreloders((EntityPlayer)event.getEntity(), event.getEntity().world);
            this.didIt = true;
        }
    }
    
    private void SpawnPreloders(final EntityPlayer player, final World world) {
    }
}
