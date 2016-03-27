package org.jurassicraft.server.api;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.Random;

public interface IGrindableItem
{
    boolean isGrindable(ItemStack stack);

    ItemStack getGroundItem(ItemStack stack, Random random);

    static IGrindableItem getGrindableItem(ItemStack stack)
    {
        if (stack != null)
        {
            Item item = stack.getItem();

            if (item instanceof ItemBlock)
            {
                Block block = ((ItemBlock) item).getBlock();

                if (block instanceof IGrindableItem)
                {
                    return (IGrindableItem) block;
                }
            }
            else if (item instanceof IGrindableItem)
            {
                return (IGrindableItem) item;
            }
        }

        return null;
    }

    static boolean isGrindableItem(ItemStack stack)
    {
        return getGrindableItem(stack) != null;
    }
}
