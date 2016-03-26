package org.jurassicraft.server.plant;

import net.minecraft.block.Block;
import org.jurassicraft.server.block.BlockHandler;

/**
 * Copyright 2016 Timeless Modding Mod
 */
public class WildOnionPlant extends Plant
{
    @Override
    public EnumPlantType getPlantType()
    {
        return EnumPlantType.CROP;
    }

    @Override
    public String getName()
    {
        return "Wild Onion";
    }

    @Override
    public Block getBlock()
    {
        return BlockHandler.INSTANCE.wild_onion;
    }
}
