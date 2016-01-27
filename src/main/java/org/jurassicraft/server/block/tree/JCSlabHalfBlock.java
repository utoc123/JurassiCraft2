package org.jurassicraft.server.block.tree;

import net.minecraft.block.state.IBlockState;
import org.jurassicraft.server.creativetab.JCCreativeTabs;

public class JCSlabHalfBlock extends JCSlabBlock
{
    public JCSlabHalfBlock(String name, IBlockState state)
    {
        super(state);
        this.setUnlocalizedName(name + "_slab");
        this.setCreativeTab(JCCreativeTabs.plants);
    }

    public boolean isDouble()
    {
        return false;
    }
}
