package org.jurassicraft.server.block.fence;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jurassicraft.server.block.entity.ElectricFencePoleBlockEntity;
import org.jurassicraft.server.tab.TabHandler;

public class ElectricFencePoleBlock extends BlockContainer {
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool EAST = PropertyBool.create("east");

    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.3425, 0.0, 0.3425, 0.6575, 1.0, 0.6575);

    public ElectricFencePoleBlock() {
        super(Material.IRON);
        this.setHardness(3.0F);
        this.setCreativeTab(TabHandler.BLOCKS);
        this.setSoundType(SoundType.METAL);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDS;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
        return this.getBoundingBox(state, world, pos);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
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
    public TileEntity createNewTileEntity(World world, int meta) {
        return new ElectricFencePoleBlockEntity();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, NORTH, SOUTH, WEST, EAST);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        BlockPos northPos = pos.offset(EnumFacing.NORTH);
        BlockPos southPos = pos.offset(EnumFacing.SOUTH);
        BlockPos eastPos = pos.offset(EnumFacing.EAST);
        BlockPos westPos = pos.offset(EnumFacing.WEST);
        IBlockState northBlock = world.getBlockState(northPos);
        IBlockState southBlock = world.getBlockState(southPos);
        IBlockState westBlock = world.getBlockState(westPos);
        IBlockState eastBlock = world.getBlockState(eastPos);
        boolean north = this.canConnect(world, northPos, EnumFacing.NORTH, northBlock);
        boolean south = this.canConnect(world, southPos, EnumFacing.SOUTH, southBlock);
        boolean west = this.canConnect(world, westPos, EnumFacing.WEST, westBlock);
        boolean east = this.canConnect(world, eastPos, EnumFacing.EAST, eastBlock);
        return state.withProperty(NORTH, north).withProperty(SOUTH, south).withProperty(WEST, west).withProperty(EAST, east);
    }

    protected boolean canConnect(IBlockAccess world, BlockPos pos, EnumFacing direction, IBlockState state) {
        if (state.getBlock() instanceof ElectricFenceWireBlock || state.getBlock() instanceof ElectricFencePoleBlock) {
            return true;
        } else {
            IBlockState down = world.getBlockState(pos.down());
            if (down.getBlock() instanceof ElectricFenceWireBlock && down.getActualState(world, pos).getValue(ElectricFenceWireBlock.UP_DIRECTION).getOpposite() == direction) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }
}
