package org.jurassicraft.server.block.tree;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import org.jurassicraft.server.creativetab.JCCreativeTabs;

public class JCStairsBlock extends BlockStairs
{
    public JCStairsBlock(TreeType type, IBlockState state)
    {
        super(state);
        this.setCreativeTab(JCCreativeTabs.plants);
        this.setUnlocalizedName(type.name().toLowerCase().replaceAll(" ", "_") + "_stairs");
    }
}