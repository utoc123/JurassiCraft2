package org.jurassicraft.server.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import org.jurassicraft.server.creativetab.JCCreativeTabs;

public class SmallRoyalFernBlock extends BlockBush
{
    public SmallRoyalFernBlock()
    {
        super();
        this.setCreativeTab(JCCreativeTabs.plants);

        float f = 0.4F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);

        this.setStepSound(Block.soundTypeGrass);
    }
}
