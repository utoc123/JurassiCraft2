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
import org.jurassicraft.server.api.CleanableItem;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.tab.TabHandler;

import java.util.Random;

public class PlantFossilBlock extends Block implements CleanableItem
{
    public PlantFossilBlock()
    {
        super(Material.ROCK);
        this.setHardness(2.0F);
        this.setResistance(8.0F);
        this.setSoundType(SoundType.STONE);
        this.setCreativeTab(TabHandler.FOSSILS);
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
        double chance = rand.nextDouble() / luck;

        if (chance < 0.35)
        {
            return ItemHandler.PLANT_FOSSIL;
        }
        else if (chance < 0.50)
        {
            return ItemHandler.TWIG_FOSSIL;
        }
        else if (chance < 0.75)
        {
            return Items.COAL;
        }
        else if (chance < 0.85)
        {
            return Items.FLINT;
        }
        else
        {
            return Item.getItemFromBlock(Blocks.COBBLESTONE);
        }
    }
}
