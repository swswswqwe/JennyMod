package com.schnurritv.sexmod.util.Handlers;

import net.minecraftforge.fml.common.network.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraft.util.math.*;
import com.schnurritv.sexmod.girls.*;
import com.schnurritv.sexmod.gui.*;
import java.util.*;

public class GuiHandler implements IGuiHandler
{
    public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
        if (ID == 0) {
            return new ContainerUI();
        }
        return null;
    }
    
    public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
        if (ID == 0) {
            final ArrayList<GirlEntity> girls = GirlEntity.getGirlsByPos(new BlockPos(x, y, z));
            for (final GirlEntity girl : girls) {
                if (girl.animationParameters.get(GirlEntity.AnimationParameters.ISDOINGACTION)) {
                    return null;
                }
            }
            return new MenuUI(player.inventory, x, y, z);
        }
        return null;
    }
}
