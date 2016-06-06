package org.jurassicraft.server.plant;

import net.minecraft.block.Block;
import org.jurassicraft.server.block.BlockHandler;

public class SmallChainFernPlant extends Plant
{
    @Override
    public PlantType getPlantType()
    {
        return PlantType.FERN;
    }

    @Override
    public String getName()
    {
        return "Small Chain Fern";
    }

    @Override
    public Block getBlock()
    {
        return BlockHandler.INSTANCE.SMALL_CHAIN_FERN;
    }
}
