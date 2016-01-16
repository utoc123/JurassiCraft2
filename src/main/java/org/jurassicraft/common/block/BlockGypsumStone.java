package org.jurassicraft.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import org.jurassicraft.common.creativetab.JCCreativeTabs;
import org.jurassicraft.common.item.JCItemRegistry;

import java.util.Random;

public class BlockGypsumStone extends Block
{
    public BlockGypsumStone()
    {
        super(Material.rock);
        this.setUnlocalizedName("gypsum_stone");
        this.setCreativeTab(JCCreativeTabs.blocks);
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return JCItemRegistry.gypsum_powder;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return random.nextInt(5) + 4;
    }
}
