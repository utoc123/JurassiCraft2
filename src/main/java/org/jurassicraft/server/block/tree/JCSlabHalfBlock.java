package org.jurassicraft.server.block.tree;

import net.minecraft.block.state.IBlockState;
import org.jurassicraft.server.creativetab.JCCreativeTabs;

public class JCSlabHalfBlock extends JCSlabBlock
{
    public JCSlabHalfBlock(TreeType type, IBlockState state)
    {
        super(state);
        this.setUnlocalizedName(type.name().toLowerCase() + "_slab");
        this.setCreativeTab(JCCreativeTabs.plants);
    }

    public boolean isDouble()
    {
        return false;
    }
}
