package org.jurassicraft.server.block.tree;

import net.minecraft.block.state.IBlockState;
import org.jurassicraft.server.creativetab.TabHandler;

public class JCSlabHalfBlock extends JCSlabBlock
{
    public JCSlabHalfBlock(TreeType type, IBlockState state)
    {
        super(type, state);
        this.setUnlocalizedName(type.name().toLowerCase() + "_slab");
        this.setCreativeTab(TabHandler.INSTANCE.plants);
    }

    @Override
    public boolean isDouble()
    {
        return false;
    }
}
