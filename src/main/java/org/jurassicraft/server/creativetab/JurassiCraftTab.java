package org.jurassicraft.server.creativetab;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public abstract class JurassiCraftTab extends CreativeTabs
{
    public JurassiCraftTab(String label)
    {
        super(label);
    }

    public void setTab(Block... blocks)
    {
        for (Block block : blocks)
        {
            if (block != null)
            {
                block.setCreativeTab(this);
            }
        }
    }

    public void setTab(Item... items)
    {
        for (Item item : items)
        {
            if (item != null)
            {
                item.setCreativeTab(this);
            }
        }
    }
}
