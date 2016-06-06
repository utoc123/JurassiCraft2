package org.jurassicraft.server.plant;

import net.minecraft.block.Block;
import org.jurassicraft.server.block.BlockHandler;

public class DicksoniaPlant extends Plant
{
    @Override
    public PlantType getPlantType()
    {
        return PlantType.FERN;
    }

    @Override
    public String getName()
    {
        return "Dicksonia";
    }

    @Override
    public Block getBlock()
    {
        return BlockHandler.INSTANCE.DICKSONIA;
    }
}
