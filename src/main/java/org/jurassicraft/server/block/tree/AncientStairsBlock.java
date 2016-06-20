package org.jurassicraft.server.block.tree;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import org.jurassicraft.server.tab.TabHandler;

public class AncientStairsBlock extends BlockStairs
{
    public AncientStairsBlock(TreeType type, IBlockState state)
    {
        super(state);
        this.setCreativeTab(TabHandler.PLANTS);
        this.setUnlocalizedName(type.name().toLowerCase().replaceAll(" ", "_") + "_stairs");
    }
}