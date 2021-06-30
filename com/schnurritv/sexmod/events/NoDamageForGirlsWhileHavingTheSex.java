package com.schnurritv.sexmod.events;

import net.minecraftforge.event.entity.living.*;
import com.schnurritv.sexmod.girls.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class NoDamageForGirlsWhileHavingTheSex
{
    @SubscribeEvent
    public void NoDamageForGirlsWhileHavingTheSex(final LivingAttackEvent event) {
        if (event.getEntity() instanceof GirlEntity) {}
    }
}
