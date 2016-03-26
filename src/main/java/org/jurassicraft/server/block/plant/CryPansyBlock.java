package org.jurassicraft.server.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import org.jurassicraft.server.creativetab.TabHandler;

public class CryPansyBlock extends BlockBush
{
    public CryPansyBlock()
    {
        super();
        this.setCreativeTab(TabHandler.INSTANCE.plants);

        this.setStepSound(Block.soundTypeGrass);
    }
}
