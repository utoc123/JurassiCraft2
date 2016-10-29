package org.jurassicraft.server.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AncientCoralBlock extends AncientPlantBlock {
    public AncientCoralBlock() {
        super(Material.CORAL);
    }

    @Override
    protected boolean canPlace(IBlockState down, IBlockState here, IBlockState up) {
        return this.canSustainBush(down) && here.getBlock() == Blocks.WATER && up.getBlock() == Blocks.WATER;
    }

    @Override
    protected boolean canSustainBush(IBlockState ground) {
        Block block = ground.getBlock();
        return block == Blocks.SAND || block == Blocks.CLAY || block == Blocks.GRAVEL || block == Blocks.DIRT;
    }

    @Override
    protected boolean isNearWater(World world, BlockPos nextPos) {
        return true;
    }
}
