package org.jurassicraft.server.item;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.jurassicraft.server.api.GrindableItem;
import org.jurassicraft.server.block.tree.TreeType;
import org.jurassicraft.server.plant.PlantHandler;
import org.jurassicraft.server.tab.TabHandler;

import java.util.Random;

public class TwigFossilItem extends Item implements GrindableItem
{
    public TwigFossilItem()
    {
        super();
        this.setCreativeTab(TabHandler.INSTANCE.plants);
    }

    @Override
    public boolean isGrindable(ItemStack stack)
    {
        return true;
    }

    @Override
    public ItemStack getGroundItem(ItemStack stack, Random random)
    {
        NBTTagCompound tag = stack.getTagCompound();

        int outputType = random.nextInt(4);

        if (outputType == 3)
        {
            ItemStack output = new ItemStack(ItemHandler.INSTANCE.PLANT_SOFT_TISSUE, 1, PlantHandler.INSTANCE.getPlantId(TreeType.values()[random.nextInt(TreeType.values().length)].getPlant()));
            output.setTagCompound(tag);
            return output;
        }
        else if (outputType < 2)
        {
            return new ItemStack(Items.DYE, 1, 15);
        }

        return new ItemStack(Items.FLINT);
    }
}
