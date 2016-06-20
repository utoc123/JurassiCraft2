package org.jurassicraft.server.block.plant;

import net.minecraft.item.Item;
import org.jurassicraft.server.item.ItemHandler;

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
        return ItemHandler.WILD_ONION;
    }

    @Override
    protected Item getCrop()
    {
        return ItemHandler.WILD_ONION;
    }

}
