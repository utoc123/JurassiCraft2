package org.jurassicraft.server.api;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.Random;

public interface ICleanableItem
{
    boolean isCleanable(ItemStack stack);

    ItemStack getCleanedItem(ItemStack stack, Random random);

    static ICleanableItem getCleanableItem(ItemStack stack)
    {
        if (stack != null)
        {
            Item item = stack.getItem();

            if (item instanceof ItemBlock)
            {
                Block block = ((ItemBlock) item).getBlock();

                if (block instanceof ICleanableItem)
                {
                    return (ICleanableItem) block;
                }
            }
            else if (item instanceof ICleanableItem)
            {
                return (ICleanableItem) item;
            }
        }

        return null;
    }

    static boolean isCleanableItem(ItemStack stack)
    {
        return getCleanableItem(stack) != null;
    }
}
