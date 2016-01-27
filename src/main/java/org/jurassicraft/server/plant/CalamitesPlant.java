package org.jurassicraft.server.plant;

import net.minecraft.block.Block;
import org.jurassicraft.server.block.JCBlockRegistry;

public class CalamitesPlant extends Plant
{
    @Override
    public EnumPlantType getPlantType()
    {
        return EnumPlantType.TREE;
    }

    @Override
    public String getName()
    {
        return "Calamites";
    }

    @Override
    public Block getBlock()
    {
        return JCBlockRegistry.saplings[1];
    }
}
