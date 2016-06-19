package org.jurassicraft.server.plant;

import net.minecraft.block.Block;
import org.jurassicraft.server.block.BlockHandler;

public class ScalyTreeFernPlant extends Plant
{
    @Override
    public String getName()
    {
        return "Scaly Tree Fern";
    }

    @Override
    public Block getBlock()
    {
        return BlockHandler.INSTANCE.SCALY_TREE_FERN;
    }
}
