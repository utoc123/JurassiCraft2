package org.jurassicraft.server.plant;

import net.minecraft.block.Block;
import org.jurassicraft.server.block.BlockHandler;

public class BennettitaleanCycadeoideaPlant extends Plant
{
    @Override
    public PlantType getPlantType()
    {
        return PlantType.FERN;
    }

    @Override
    public String getName()
    {
        return "Bennettitalean Cycadeoidea";
    }

    @Override
    public Block getBlock()
    {
        return BlockHandler.INSTANCE.CYCADEOIDEA;
    }
}
