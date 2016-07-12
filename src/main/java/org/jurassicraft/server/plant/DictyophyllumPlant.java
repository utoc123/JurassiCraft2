package org.jurassicraft.server.plant;

import net.minecraft.block.Block;
import org.jurassicraft.server.block.BlockHandler;

public class DictyophyllumPlant extends Plant
{
    @Override
    public String getName()
    {
        return "Dictyophyllum";
    }

    @Override
    public Block getBlock()
    {
        return BlockHandler.DICTYOPHYLLUM;
    }

    @Override
    public int getHealAmount()
    {
        return 2000;
    }
}
