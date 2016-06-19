package org.jurassicraft.server.plant;

import net.minecraft.block.Block;
import org.jurassicraft.server.block.BlockHandler;

public class SmallCycadPlant extends Plant
{
    @Override
    public String getName()
    {
        return "Small Cycad";
    }

    @Override
    public Block getBlock()
    {
        return BlockHandler.INSTANCE.SMALL_CYCAD;
    }
}
