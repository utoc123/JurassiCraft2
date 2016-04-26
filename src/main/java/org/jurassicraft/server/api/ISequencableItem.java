package org.jurassicraft.server.api;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.Random;

public interface ISequencableItem
{
    boolean isSequencable(ItemStack stack);

    ItemStack getSequenceOutput(ItemStack stack, Random random);

    static ISequencableItem getSequencableItem(ItemStack stack)
    {
        if (stack != null)
        {
            Item item = stack.getItem();

            if (item instanceof ItemBlock)
            {
                Block block = ((ItemBlock) item).getBlock();

                if (block instanceof ISequencableItem)
                {
                    return (ISequencableItem) block;
                }
            }
            else if (item instanceof ISequencableItem)
            {
                return (ISequencableItem) item;
            }
        }

        return null;
    }

    static boolean isSequencableItem(ItemStack stack)
    {
        return getSequencableItem(stack) != null;
    }

    static int randomQuality(Random rand)
    {
        return (rand.nextInt(20) + 1) * 5;
    }
}
