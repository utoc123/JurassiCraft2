package org.jurassicraft.server.plant;

import net.minecraft.block.Block;
import org.jurassicraft.server.block.BlockHandler;

public class BennettitaleanCycadeoideaPlant extends Plant
{
    @Override
    public EnumPlantType getPlantType()
    {
        return EnumPlantType.FERN;
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
