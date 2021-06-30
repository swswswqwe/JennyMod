package com.schnurritv.sexmod.events;

import net.minecraftforge.event.entity.living.*;
import com.schnurritv.sexmod.girls.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class RemoveEntityFromList
{
    @SubscribeEvent
    public void RemoveSophiFromList(final LivingDeathEvent event) {
        if (event.getEntity() instanceof GirlEntity) {
            final GirlEntity girl = (GirlEntity)event.getEntity();
            GirlEntity.girlEntities.remove(girl);
        }
    }
}
