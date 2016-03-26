package org.jurassicraft.server.block.plant;

import net.minecraft.item.Item;
import org.jurassicraft.server.item.JCItemRegistry;

/**
 * Copyright 2016 Timeless Modding Mod
 */
public class WildOnionBlock extends JCBlockCrops7
{
    public WildOnionBlock()
    {
        seedDropMin = 0;
        seedDropMax = 0;
        cropDropMin = 1;
        cropDropMax = 4;
    }

    @Override
    protected Item getSeed()
    {
        return JCItemRegistry.wild_onion;
    }

    @Override
    protected Item getCrop()
    {
        return JCItemRegistry.wild_onion;
    }

}
