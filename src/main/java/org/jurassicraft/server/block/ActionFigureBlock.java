package org.jurassicraft.server.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.block.entity.ActionFigureBlockEntity;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.item.ItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ActionFigureBlock extends OrientedBlock {
    public ActionFigureBlock() {
        super(Material.WOOD);
        this.setSoundType(SoundType.WOOD);
        this.setTickRandomly(true);
        this.setHardness(0.0F);
        this.setResistance(0.0F);
    }

//    @Override
//    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos) {
//        return this.getBounds(blockAccess, pos);
//    }
//
//    @Override
//    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
//        return this.getBounds(world, pos).offset(pos);
//    }

    private AxisAlignedBB getBounds(IBlockAccess world, BlockPos pos) {
        TileEntity entity = world.getTileEntity(pos);
        if (entity instanceof ActionFigureBlockEntity) {
            Dinosaur dinosaur = EntityHandler.getDinosaurById(((ActionFigureBlockEntity) entity).dinosaur);
            if (dinosaur != null) {
                if(!((ActionFigureBlockEntity)entity).getEntity().isSkeleton()){
                    float width = dinosaur.getAdultSizeX() * 0.2F / 2.0F;
                    float height = dinosaur.getAdultSizeY() * 0.2F;
                    return new AxisAlignedBB(0.5 - width, 0, 0.5 - width, width + 0.5, height, width + 0.5);
                }
                float width = dinosaur.getAdultSizeX();
                float height = dinosaur.getAdultSizeY();
                return new AxisAlignedBB(0.5 - width, 0, 0.5 - width, width + 0.5, height, width + 0.5);
            }
        }
        return new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return super.canPlaceBlockAt(world, pos) && this.canBlockStay(world, pos);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block) {
        super.neighborChanged(state, world, pos, block);
        this.checkAndDropBlock(world, pos, world.getBlockState(pos));
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        this.checkAndDropBlock(world, pos, state);
    }

    protected void checkAndDropBlock(World world, BlockPos pos, IBlockState state) {
        if (!this.canBlockStay(world, pos)) {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

    public boolean canBlockStay(World world, BlockPos pos) {
        return world.getBlockState(pos.down()).isOpaqueCube();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ItemHandler.ACTION_FIGURE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return getItemFromTile(getTile(world, pos));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new ActionFigureBlockEntity();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    protected ActionFigureBlockEntity getTile(IBlockAccess world, BlockPos pos) {
        return (ActionFigureBlockEntity) world.getTileEntity(pos);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!player.capabilities.isCreativeMode) {
            this.dropBlockAsItem(world, pos, state, 0);
        }

        super.onBlockHarvested(world, pos, state, player);
    }
    public ItemStack getItemFromTile(ActionFigureBlockEntity tile){
        return ItemHandler.ACTION_FIGURE.establishNBT(new ItemStack(ItemHandler.ACTION_FIGURE, 1, tile.dinosaur), tile.isMale?1:2, tile.isSkeleton); 
    }
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> drops = new ArrayList<>(1);

        ActionFigureBlockEntity tile = this.getTile(world, pos);

        if (tile != null) {
            drops.add(getItemFromTile(tile));
        }

        return drops;
    }
}
