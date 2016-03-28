package org.jurassicraft.server.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.creativetab.TabHandler;

public class SmallRoyalFernBlock extends BlockBush
{
    public SmallRoyalFernBlock()
    {
        super();
        this.setCreativeTab(TabHandler.INSTANCE.plants);

        float f = 0.4F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);

        this.setStepSound(Block.soundTypeGrass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Block.EnumOffsetType getOffsetType()
    {
        return EnumOffsetType.XZ;
    }
}
