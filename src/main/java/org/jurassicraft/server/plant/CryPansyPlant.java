package org.jurassicraft.server.plant;

import net.minecraft.block.Block;
import org.jurassicraft.server.block.BlockHandler;

public class CryPansyPlant extends Plant
{
    @Override
    public PlantType getPlantType()
    {
        return PlantType.FLOWER;
    }

    @Override
    public String getName()
    {
        return "Cry Pansy";
    }

    @Override
    public Block getBlock()
    {
        return BlockHandler.INSTANCE.CRY_PANSY;
    }
}
