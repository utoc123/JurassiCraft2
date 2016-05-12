package org.jurassicraft.server.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.creativetab.TabHandler;

public class MossBlock extends Block
{
    public MossBlock()
    {
        super(Material.leaves);

        this.setHardness(0.2F);
        this.setResistance(0.0F);
        this.setTickRandomly(true);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        this.setCreativeTab(TabHandler.INSTANCE.plants);
        this.setStepSound(soundTypeGrass);
        this.setLightOpacity(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        return ColorizerGrass.getGrassColor(0.5D, 1.0D);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(IBlockState state)
    {
        return this.getBlockColor();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
    {
        return BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
    }

    @Override
    public boolean isFullCube()
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        IBlockState below = worldIn.getBlockState(pos.down());
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos) && (below != null && below.getBlock().isFullCube());
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        this.checkForDrop(world, pos, state);
    }

    private boolean checkForDrop(World world, BlockPos pos, IBlockState state)
    {
        if (!this.canBlockStay(world, pos))
        {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean canBlockStay(World world, BlockPos pos)
    {
        return !world.isAirBlock(pos.down());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return side == EnumFacing.UP || super.shouldSideBeRendered(world, pos, side);
    }
}
