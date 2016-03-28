package org.jurassicraft.server.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.Explosion;
import org.jurassicraft.server.api.ICleanableItem;
import org.jurassicraft.server.creativetab.TabHandler;
import org.jurassicraft.server.item.ItemHandler;

import java.util.Random;

public class PlantFossilBlock extends Block implements ICleanableItem
{
    public PlantFossilBlock()
    {
        super(Material.rock);
        this.setHardness(2.0F);
        this.setResistance(8.0F);
        this.setStepSound(SoundType.STONE);
        this.setCreativeTab(TabHandler.INSTANCE.fossils);
        this.setHarvestLevel("pickaxe", 0);
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosion)
    {
        return false;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return getRandomOutput(rand, 1.0);
    }

    @Override
    public boolean isCleanable(ItemStack stack)
    {
        return true;
    }

    @Override
    public ItemStack getCleanedItem(ItemStack stack, Random random)
    {
        return new ItemStack(getRandomOutput(random, 2.0));
    }

    private Item getRandomOutput(Random rand, double luck)
    {
        double v = rand.nextDouble() / luck;

        if (v < 0.35)
        {
            return ItemHandler.INSTANCE.plant_fossil;
        }
        else if (v < 0.50)
        {
            return ItemHandler.INSTANCE.twig_fossil;
        }
        else if (v < 0.75)
        {
            return Items.coal;
        }
        else if (v < 0.85)
        {
            return Items.flint;
        }
        else
        {
            return Item.getItemFromBlock(Blocks.cobblestone);
        }
    }
}
