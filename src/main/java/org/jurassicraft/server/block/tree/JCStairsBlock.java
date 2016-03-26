package org.jurassicraft.server.block.tree;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import org.jurassicraft.server.creativetab.TabHandler;

public class JCStairsBlock extends BlockStairs
{
    public JCStairsBlock(TreeType type, IBlockState state)
    {
        super(state);
        this.setCreativeTab(TabHandler.INSTANCE.plants);
        this.setUnlocalizedName(type.name().toLowerCase().replaceAll(" ", "_") + "_stairs");
    }
}