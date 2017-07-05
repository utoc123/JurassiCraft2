package org.jurassicraft.server.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRail;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.vehicle.FordExplorerEntity;
import org.jurassicraft.server.tab.TabHandler;

import java.util.Random;

public class TourRailBlock extends BlockRail {
    public static final PropertyEnum<EnumRailDirection> SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class);

    private boolean powered;

    public TourRailBlock(boolean powered) {
        super();
        this.powered = powered;
        if (!this.isPowered()) {
            this.setCreativeTab(TabHandler.BLOCKS);
        } else {
            this.setCreativeTab(null);
        }
        this.setUnlocalizedName("tour_rail");
        this.setHarvestLevel("pickaxe", 1);
        this.setHardness(1.0F);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
        if (!world.isRemote) {
            world.notifyNeighborsOfStateChange(pos, this);
            world.notifyNeighborsOfStateChange(pos.down(), this);
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        if (!world.isRemote) {
            state.neighborChanged(world, pos, this);
        }
    }

    protected boolean findSignal(World world, BlockPos pos, IBlockState state, EnumFacing direction, int distance) {
        if (distance >= 8) {
            return false;
        } else {
            BlockRailBase.EnumRailDirection shape = state.getValue(SHAPE);

            BlockPos newPos = pos.offset(direction);

            if (shape.isAscending()) {
                boolean ascendingEast = shape == EnumRailDirection.ASCENDING_EAST && direction == EnumFacing.EAST;
                boolean ascendingWest = shape == EnumRailDirection.ASCENDING_WEST && direction == EnumFacing.WEST;
                boolean ascendingNorth = shape == EnumRailDirection.ASCENDING_NORTH && direction == EnumFacing.NORTH;
                boolean ascendingSouth = shape == EnumRailDirection.ASCENDING_SOUTH && direction == EnumFacing.SOUTH;
                if (ascendingEast || ascendingWest || ascendingNorth || ascendingSouth) {
                    newPos = newPos.up();
                }
            }

            return this.isSameRailWithPower(world, newPos, distance, direction, shape) || !shape.isAscending() && this.isSameRailWithPower(world, newPos.down(), distance, direction, shape);
        }
    }

    protected boolean isSameRailWithPower(World world, BlockPos pos, int distance, EnumFacing direction, BlockRailBase.EnumRailDirection shape) {
        IBlockState state = world.getBlockState(pos);

        if (!(state.getBlock() instanceof TourRailBlock)) {
            return false;
        } else {
            BlockRailBase.EnumRailDirection checkedShape = state.getValue(SHAPE);
            boolean hadPower = ((TourRailBlock) state.getBlock()).isPowered();
            boolean isX = checkedShape != EnumRailDirection.EAST_WEST && checkedShape != EnumRailDirection.ASCENDING_EAST && checkedShape != EnumRailDirection.ASCENDING_WEST;
            boolean isZ = checkedShape != EnumRailDirection.NORTH_SOUTH && checkedShape != EnumRailDirection.ASCENDING_NORTH && checkedShape != EnumRailDirection.ASCENDING_SOUTH;
            boolean canMove = (shape != EnumRailDirection.EAST_WEST || isZ) && (shape != EnumRailDirection.NORTH_SOUTH || isX);
            return canMove && hadPower && world.isBlockPowered(pos) || (this.findSignal(world, pos, state, direction, distance + 1) || (!shape.isAscending() && this.findSignal(world, pos, state, direction.rotateY(), distance + 1) || this.findSignal(world, pos, state, direction.rotateYCCW(), distance + 1)));
        }
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return this.isPowered();
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        return 2;
    }

    @Override
    protected void updateState(IBlockState state, World world, BlockPos pos, Block block) {
        super.updateState(state, world, pos, block);
        state = world.getBlockState(pos);

        boolean currentlyPowered = world.isBlockPowered(pos);

        if (!currentlyPowered) {
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                if (this.findSignal(world, pos, state, facing, 0)) {
                    currentlyPowered = true;
                    break;
                }
            }
        }

        if (currentlyPowered != this.isPowered()) {
            EnumRailDirection shape = state.getValue(SHAPE);
            state = (currentlyPowered ? BlockHandler.TOUR_RAIL_POWERED : BlockHandler.TOUR_RAIL).getDefaultState();
            world.setBlockState(pos, state.withProperty(SHAPE, shape), 3);
            world.notifyNeighborsOfStateChange(pos.down(), this);

            if (state.getValue(SHAPE).isAscending()) {
                world.notifyNeighborsOfStateChange(pos.up(), this);
            }
        }
    }

    @Override
    public IProperty<EnumRailDirection> getShapeProperty() {
        return SHAPE;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rotation) {
        EnumRailDirection shape = state.getValue(SHAPE);
        switch (rotation) {
            case CLOCKWISE_180:
                switch (shape) {
                    case ASCENDING_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
                    case NORTH_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
                    case NORTH_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
                }

            case COUNTERCLOCKWISE_90:
                switch (shape) {
                    case ASCENDING_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);
                    case ASCENDING_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);
                    case ASCENDING_NORTH:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);
                    case ASCENDING_SOUTH:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);
                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
                    case NORTH_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
                    case NORTH_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
                    case NORTH_SOUTH:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST);
                    case EAST_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);
                }

            case CLOCKWISE_90:
                switch (shape) {
                    case ASCENDING_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);
                    case ASCENDING_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);
                    case ASCENDING_NORTH:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);
                    case ASCENDING_SOUTH:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);
                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
                    case NORTH_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
                    case NORTH_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
                    case NORTH_SOUTH:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST);
                    case EAST_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);
                }

            default:
                return state;
        }
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        BlockRailBase.EnumRailDirection shape = state.getValue(SHAPE);

        switch (mirrorIn) {
            case LEFT_RIGHT:
                switch (shape) {
                    case ASCENDING_NORTH:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
                    case NORTH_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
                    case NORTH_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
                    default:
                        return super.withMirror(state, mirrorIn);
                }

            case FRONT_BACK:
                switch (shape) {
                    case ASCENDING_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                    case ASCENDING_SOUTH:
                    default:
                        break;
                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
                    case NORTH_WEST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
                    case NORTH_EAST:
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
                }
        }

        return super.withMirror(state, mirrorIn);
    }

    @Override
    public void onMinecartPass(World world, EntityMinecart cart, BlockPos pos) {
        super.onMinecartPass(world, cart, pos);

        if (cart instanceof FordExplorerEntity) {
            FordExplorerEntity vehicle = (FordExplorerEntity) cart;

            IBlockState state = world.getBlockState(pos);

            double speed = 0.1;
            double startSpeed = speed * 0.02;
            double moveSpeed = speed * 0.1;

            double movement = Math.sqrt(cart.motionX * cart.motionX + cart.motionZ * cart.motionZ);

            if (this.isPowered() && speed > 0) {
                EnumRailDirection shape = state.getValue(this.getShapeProperty());

                if (movement > startSpeed / 2.0) {
                    cart.motionX += cart.motionX / movement * moveSpeed;
                    cart.motionZ += cart.motionZ / movement * moveSpeed;
                } else if (shape == BlockRailBase.EnumRailDirection.EAST_WEST) {
                    if (world.getBlockState(pos.west()).isNormalCube()) {
                        cart.motionX = startSpeed;
                    } else if (world.getBlockState(pos.east()).isNormalCube()) {
                        cart.motionX = -startSpeed;
                    }
                } else if (shape == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
                    if (world.getBlockState(pos.north()).isNormalCube()) {
                        cart.motionZ = startSpeed;
                    } else if (world.getBlockState(pos.south()).isNormalCube()) {
                        cart.motionZ = -startSpeed;
                    }
                }
            } else {
                cart.motionY = 0.0;
                if (movement < 0.03) {
                    cart.motionX = 0.0;
                    cart.motionZ = 0.0;
                } else {
                    cart.motionX *= 0.5;
                    cart.motionZ *= 0.5;
                }
            }
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, SHAPE);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(BlockHandler.TOUR_RAIL);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return this.getItem(world, pos, state);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(BlockHandler.TOUR_RAIL);
    }

    public boolean isPowered() {
        return this.powered;
    }
}
