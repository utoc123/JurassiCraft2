package org.jurassicraft.server.block.plant;

import net.minecraft.item.Item;
import org.jurassicraft.server.item.ItemHandler;

/**
 * Copyright 2016 Timeless Modding Mod
 */
public class AjuginuculaSmithiiBlock extends JCBlockCrops8
{
    public AjuginuculaSmithiiBlock()
    {
        seedDropMin = 1;
        seedDropMax = 4;
        cropDropMin = 2;
        cropDropMax = 5;
    }

    @Override
    protected Item getSeed()
    {
        return ItemHandler.INSTANCE.ajuginucula_smithii_seeds;
    }

    @Override
    protected Item getCrop()
    {
        return ItemHandler.INSTANCE.ajuginucula_smithii_leaves;
    }
}
