package org.jurassicraft.server.block.tree;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import org.jurassicraft.server.creativetab.JCCreativeTabs;

public class JCStairsBlock extends BlockStairs
{
    public JCStairsBlock(String name, IBlockState state)
    {
        super(state);
        this.setCreativeTab(JCCreativeTabs.plants);
        this.setUnlocalizedName(name.toLowerCase().replaceAll(" ", "_") + "_stairs");
    }
}