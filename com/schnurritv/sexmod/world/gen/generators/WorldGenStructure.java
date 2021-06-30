package com.schnurritv.sexmod.world.gen.generators;

import net.minecraft.world.gen.feature.*;
import com.schnurritv.sexmod.util.interfaces.*;
import net.minecraft.world.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;
import net.minecraft.server.*;
import net.minecraft.world.gen.structure.template.*;
import net.minecraft.block.state.*;
import java.util.*;

public class WorldGenStructure extends WorldGenerator implements IStructure
{
    public static String structureName;
    
    public WorldGenStructure(final String name) {
        WorldGenStructure.structureName = name;
    }
    
    public static void generateStructure(final World world, final BlockPos pos) {
        final MinecraftServer mcServer = world.getMinecraftServer();
        final TemplateManager manager = WorldGenStructure.worldServer.getStructureTemplateManager();
        final ResourceLocation location = new ResourceLocation("sexmod", WorldGenStructure.structureName);
        final Template template = manager.get(mcServer, location);
        if (template != null) {
            final IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
            template.addBlocksToWorld(world, pos, WorldGenStructure.settings);
        }
    }
    
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        generateStructure(worldIn, position);
        return false;
    }
}
