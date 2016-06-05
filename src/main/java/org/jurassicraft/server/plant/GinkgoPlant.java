package org.jurassicraft.server.plant;

import net.minecraft.block.Block;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.tree.TreeType;

public class GinkgoPlant extends Plant
{
    @Override
    public EnumPlantType getPlantType()
    {
        return EnumPlantType.TREE;
    }

    @Override
    public String getName()
    {
        return "Ginkgo";
    }

    @Override
    public Block getBlock()
    {
        return BlockHandler.INSTANCE.ANCIENT_SAPLINGS.get(TreeType.GINKGO);
    }
}
