package org.jurassicraft.server.plant;

import net.minecraft.block.Block;
import org.jurassicraft.server.block.BlockHandler;

public class WildOnionPlant extends Plant
{
    @Override
    public PlantType getPlantType()
    {
        return PlantType.CROP;
    }

    @Override
    public String getName()
    {
        return "Wild Onion";
    }

    @Override
    public Block getBlock()
    {
        return BlockHandler.INSTANCE.WILD_ONION;
    }
}
