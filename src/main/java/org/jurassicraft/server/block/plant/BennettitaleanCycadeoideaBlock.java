package org.jurassicraft.server.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import org.jurassicraft.server.creativetab.TabHandler;

public class BennettitaleanCycadeoideaBlock extends BlockBush
{
    public BennettitaleanCycadeoideaBlock()
    {
        super();
        this.setCreativeTab(TabHandler.INSTANCE.plants);

        float f = 0.4F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);

        this.setStepSound(Block.soundTypeGrass);
    }
}
