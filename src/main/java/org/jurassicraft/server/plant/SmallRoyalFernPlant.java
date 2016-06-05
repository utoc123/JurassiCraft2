package org.jurassicraft.server.plant;

import net.minecraft.block.Block;
import org.jurassicraft.server.block.BlockHandler;

public class SmallRoyalFernPlant extends Plant
{
    @Override
    public EnumPlantType getPlantType()
    {
        return EnumPlantType.FERN;
    }

    @Override
    public String getName()
    {
        return "Small Royal Fern";
    }

    @Override
    public Block getBlock()
    {
        return BlockHandler.INSTANCE.SMALL_ROYAL_FERN;
    }
}
