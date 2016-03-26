package org.jurassicraft.server.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import org.jurassicraft.server.creativetab.JCCreativeTabs;
import org.jurassicraft.server.item.JCItemRegistry;

import java.util.Random;

public class GypsumStoneBlock extends Block
{
    public GypsumStoneBlock()
    {
        super(Material.rock);
        this.setCreativeTab(JCCreativeTabs.blocks);
        this.setHardness(1.5F);
        this.setResistance(1.5F);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return JCItemRegistry.gypsum_powder;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random random)
    {
        return random.nextInt(5) + 4;
    }
}
