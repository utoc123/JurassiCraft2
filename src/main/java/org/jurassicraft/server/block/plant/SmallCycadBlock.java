package org.jurassicraft.server.block.plant;

import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.jurassicraft.server.creativetab.TabHandler;

public class SmallCycadBlock extends BlockBush
{
    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.1F, 0.0F, 0.1F, 0.9F, 0.8F, 0.9F);

    public SmallCycadBlock()
    {
        super();
        this.setCreativeTab(TabHandler.INSTANCE.plants);
        this.setStepSound(SoundType.PLANT);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
    {
        return BOUNDS;
    }
}
