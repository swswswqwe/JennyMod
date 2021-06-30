package com.schnurritv.sexmod.world.gen;

import net.minecraftforge.fml.common.*;
import com.schnurritv.sexmod.world.gen.generators.*;
import net.minecraft.world.biome.*;
import java.util.*;
import net.minecraft.world.gen.*;
import net.minecraft.world.chunk.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.block.*;
import com.google.common.collect.*;
import net.minecraft.init.*;

public class WorldGenCustomStructures implements IWorldGenerator
{
    public static final WorldGenStructure SEXHOUSE;
    public static final Set<Biome> SEXHOUSE_BIOMELIST;
    
    public void generate(final Random random, final int chunkX, final int chunkZ, final World world, final IChunkGenerator chunkGenerator, final IChunkProvider chunkProvider) {
        switch (world.provider.getDimension()) {
            case 0: {
                this.generateStructure(WorldGenCustomStructures.SEXHOUSE, world, random, chunkX, chunkZ, 25, WorldGenCustomStructures.SEXHOUSE_BIOMELIST);
            }
        }
    }
    
    private void generateStructure(final WorldGenerator generator, final World world, final Random random, final int chunkX, final int chunkZ, final int chance, final Set<Biome> BiomeList) {
        final int x = chunkX * 16 + random.nextInt(15);
        final int z = chunkZ * 16 + random.nextInt(15);
        final int y = calculateGenerationHeight(world, x, z);
        final BlockPos pos = new BlockPos(x, y, z);
        final Biome biome = world.provider.getBiomeForCoords(pos);
        if (world.getWorldType() != WorldType.FLAT && BiomeList.contains(biome) && random.nextInt(chance) == 0) {
            boolean canSpawnHere = true;
            if (world.getBlockState(new BlockPos(x, y + 1, z)) != Blocks.WATER.getDefaultState()) {
                for (int i = 0; i <= 16; ++i) {
                    for (int i2 = 0; i2 <= 16; ++i2) {
                        final boolean isAir = world.isAirBlock(new BlockPos(x + i, y, z + i2));
                        canSpawnHere = (canSpawnHere && !isAir);
                    }
                }
            }
            else {
                canSpawnHere = false;
            }
            if (canSpawnHere) {
                generator.generate(world, random, pos);
            }
        }
    }
    
    private static int calculateGenerationHeight(final World world, final int x, final int z) {
        final Set<Block> topBlocks = (Set<Block>)Sets.newHashSet((Object[])new Block[] { (Block)Blocks.GRASS, (Block)Blocks.SAND, Blocks.RED_SANDSTONE });
        int y = world.getHeight();
        Block block;
        for (boolean foundGround = false; !foundGround && y-- >= 0; foundGround = topBlocks.contains(block)) {
            block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
        }
        return y;
    }
    
    static {
        SEXHOUSE = new WorldGenStructure("sexhousechurch");
        SEXHOUSE_BIOMELIST = Sets.newHashSet((Object[])new Biome[] { Biomes.FOREST_HILLS, Biomes.EXTREME_HILLS, Biomes.ROOFED_FOREST, Biomes.DESERT, Biomes.PLAINS, Biomes.SAVANNA, Biomes.FOREST, Biomes.BIRCH_FOREST, Biomes.MESA, Biomes.SWAMPLAND, Biomes.TAIGA });
    }
}
