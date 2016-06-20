package org.jurassicraft.server.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.lang.LangHelper;
import org.jurassicraft.server.tab.TabHandler;

import java.util.List;

public class AmberItem extends Item
{
    public AmberItem()
    {
        super();
        this.setCreativeTab(TabHandler.ITEMS);
        this.setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        return new LangHelper("item.amber.name").withProperty("stored", "amber." + (stack.getItemDamage() == 0 ? "mosquito" : "aphid") + ".name").build();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems)
    {
        subItems.add(new ItemStack(item, 1, 0));
        subItems.add(new ItemStack(item, 1, 1));
    }
}
