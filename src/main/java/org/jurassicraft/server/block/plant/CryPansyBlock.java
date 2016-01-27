package org.jurassicraft.server.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import org.jurassicraft.server.creativetab.JCCreativeTabs;

public class CryPansyBlock extends BlockBush
{
    public CryPansyBlock()
    {
        super();
        this.setCreativeTab(JCCreativeTabs.plants);

        this.setStepSound(Block.soundTypeGrass);
    }
}
