package org.jurassicraft.server.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.Explosion;
import org.jurassicraft.server.creativetab.TabHandler;
import org.jurassicraft.server.item.ItemHandler;

import java.util.Random;

public class PlantFossilBlock extends Block
{
    public PlantFossilBlock()
    {
        super(Material.rock);
        this.setHardness(2.0F);
        this.setResistance(8.0F);
        this.setStepSound(soundTypeStone);
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
        double v = rand.nextDouble();

        if (v < 0.35)
        {
            return ItemHandler.INSTANCE.plant_fossil;
        }
        else if (v < 0.60)
        {
            return Items.coal;
        }
        else if (v < 0.75)
        {
            return ItemHandler.INSTANCE.twig_fossil;
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
