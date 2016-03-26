package org.jurassicraft.server.world.islanublar;

import net.ilexiconn.llibrary.common.world.gen.ChunkProviderHeightmap;
import net.ilexiconn.llibrary.common.world.gen.WorldChunkManagerHeightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;

public class IslaNublarWorldType extends WorldType
{
    public IslaNublarGeneration generator;

    public IslaNublarWorldType()
    {
        super("isla_nublar");
        this.generator = new IslaNublarGeneration();
    }

    @Override
    public net.minecraft.world.biome.WorldChunkManager getChunkManager(World world)
    {
        return new WorldChunkManagerHeightmap(world, generator);
    }

    @Override
    public net.minecraft.world.chunk.IChunkProvider getChunkGenerator(World world, String generatorOptions)
    {
        return new ChunkProviderHeightmap(world, world.getSeed(), generator);
    }
}
