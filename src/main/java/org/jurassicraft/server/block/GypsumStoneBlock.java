package org.jurassicraft.server.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import org.jurassicraft.server.creativetab.TabHandler;
import org.jurassicraft.server.item.ItemHandler;

import java.util.Random;

public class GypsumStoneBlock extends Block
{
    public GypsumStoneBlock()
    {
        super(Material.rock);
        this.setCreativeTab(TabHandler.INSTANCE.blocks);
        this.setHardness(1.5F);
        this.setResistance(1.5F);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return ItemHandler.INSTANCE.gypsum_powder;
    }

    @Override
    public int quantityDropped(Random random)
    {
        return random.nextInt(5) + 4;
    }
}
