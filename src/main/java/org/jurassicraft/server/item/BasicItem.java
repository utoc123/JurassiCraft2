package org.jurassicraft.server.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BasicItem extends Item
{
    public BasicItem(String name, CreativeTabs tab)
    {
        super();
        this.setUnlocalizedName(name.replaceAll(" ", "_").toLowerCase());
        this.setCreativeTab(tab);
    }
}
