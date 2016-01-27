package org.jurassicraft.server.plant;

import net.minecraft.block.Block;
import org.jurassicraft.server.block.JCBlockRegistry;

public class SmallCycadPlant extends Plant
{
    @Override
    public EnumPlantType getPlantType()
    {
        return EnumPlantType.FERN;
    }

    @Override
    public String getName()
    {
        return "Small Cycad";
    }

    @Override
    public Block getBlock()
    {
        return JCBlockRegistry.small_cycad;
    }
}
