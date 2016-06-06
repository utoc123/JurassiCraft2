package org.jurassicraft.server.plant;

import net.minecraft.block.Block;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.tree.TreeType;

public class CalamitesPlant extends Plant
{
    @Override
    public PlantType getPlantType()
    {
        return PlantType.TREE;
    }

    @Override
    public String getName()
    {
        return "Calamites";
    }

    @Override
    public Block getBlock()
    {
        return BlockHandler.INSTANCE.ANCIENT_SAPLINGS.get(TreeType.CALAMITES);
    }
}
