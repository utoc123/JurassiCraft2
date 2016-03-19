package org.jurassicraft.server.block.plant;

import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import org.jurassicraft.server.creativetab.JCCreativeTabs;

public class CryPansyBlock extends BlockBush
{
    public CryPansyBlock()
    {
        super();
        this.setCreativeTab(JCCreativeTabs.plants);
        this.setStepSound(SoundType.PLANT);
    }
}
