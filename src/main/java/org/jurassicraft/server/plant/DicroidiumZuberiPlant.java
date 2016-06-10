package org.jurassicraft.server.plant;

import net.minecraft.block.Block;
import org.jurassicraft.server.block.BlockHandler;

public class DicroidiumZuberiPlant extends Plant
{
    @Override
    public PlantType getPlantType()
    {
        return PlantType.FERN;
    }

    @Override
    public String getName()
    {
        return "Dicroidium Zuberi";
    }

    @Override
    public Block getBlock()
    {
        return BlockHandler.INSTANCE.DICROIDIUM_ZUBERI;
    }
}
