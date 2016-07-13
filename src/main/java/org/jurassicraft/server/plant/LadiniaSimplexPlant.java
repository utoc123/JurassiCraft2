package org.jurassicraft.server.plant;

import net.minecraft.block.Block;
import org.jurassicraft.server.block.BlockHandler;

public class LadiniaSimplexPlant extends Plant
{
    @Override
    public String getName()
    {
        return "Ladinia Simplex";
    }

    @Override
    public Block getBlock()
    {
        return BlockHandler.LADINIA_SIMPLEX;
    }

    @Override
    public int getHealAmount()
    {
        return 2000;
    }
}
