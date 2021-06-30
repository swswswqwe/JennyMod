package com.schnurritv.sexmod.util.interfaces;

import net.minecraft.world.*;
import net.minecraft.world.gen.structure.template.*;
import net.minecraftforge.fml.common.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;

public interface IStructure
{
    public static final WorldServer worldServer = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
    public static final PlacementSettings settings = new PlacementSettings().setChunk((ChunkPos)null).setIgnoreEntities(false).setIgnoreEntities(false).setMirror(Mirror.NONE).setRotation(Rotation.NONE);
}
