package org.jurassicraft.server.plant;

import net.minecraft.block.Block;
import org.jurassicraft.server.block.JCBlockRegistry;

public class DicksoniaPlant extends Plant
{
    @Override
    public EnumPlantType getPlantType()
    {
        return EnumPlantType.FERN;
    }

    @Override
    public String getName()
    {
        return "Dicksonia";
    }

    @Override
    public Block getBlock()
    {
        return JCBlockRegistry.dicksonia;
    }
}
