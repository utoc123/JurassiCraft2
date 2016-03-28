package org.jurassicraft.server.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.creativetab.TabHandler;

public class CryPansyBlock extends BlockBush
{
    public CryPansyBlock()
    {
        super();
        this.setCreativeTab(TabHandler.INSTANCE.plants);

        this.setStepSound(Block.soundTypeGrass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Block.EnumOffsetType getOffsetType()
    {
        return EnumOffsetType.XZ;
    }
}
