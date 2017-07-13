package org.jurassicraft.server.block.machine;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import javax.annotation.Nullable;

import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.OrientedBlock;
import org.jurassicraft.server.container.SkeletonAssemblyContainer;
import org.jurassicraft.server.proxy.ServerProxy;
import org.jurassicraft.server.tab.TabHandler;
import org.jurassicraft.server.util.LangHelper;

public class SkeletonAssemblyBlock extends Block {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public SkeletonAssemblyBlock() {
        super(Material.ANVIL);
        this.setUnlocalizedName("skeleton_assembly");
        this.setHardness(2.0F);
        this.setLightOpacity(0);
        this.setSoundType(SoundType.METAL);
        this.setCreativeTab(TabHandler.BLOCKS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        } else {
            playerIn.openGui(JurassiCraft.INSTANCE, ServerProxy.GUI_SKELETON_ASSEMBLER, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
    }
    
    public BlockPos getSister(BlockPos pos, IBlockState state){
        
        EnumFacing facing = state.getValue(FACING);
        switch (facing) {
            case NORTH:
                pos=pos.add(-1,0,0);
                break;
            case EAST:
                pos=pos.add(0,0,-1);
                break;
            case SOUTH:
                pos=pos.add(1,0,0);
                break;
            case WEST:
                pos=pos.add(0,0,1);
                break;
            default:
                break;
                
        }
        return pos;
    }
    
    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
    {
        pos = getSister(pos,state);
        worldIn.setBlockToAir(pos);
    }
    
    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn)
    {
        this.onBlockDestroyedByPlayer(worldIn, pos, worldIn.getBlockState(pos));
    }
    public static class InterfaceCraftingTable implements IInteractionObject {
        private final World world;
        private final BlockPos position;

        public InterfaceCraftingTable(World worldIn, BlockPos pos) {
            this.world = worldIn;
            this.position = pos;
        }

        /**
         * Get the name of this object. For players this returns their username
         */
        public String getName() {
            return null;
        }

        /**
         * Returns true if this thing is named
         */
        public boolean hasCustomName() {
            return false;
        }

        /**
         * Get the formatted ChatComponent that will be used for the sender's
         * username in chat
         */
        public ITextComponent getDisplayName() {
            return new TextComponentString(new LangHelper(BlockHandler.SKELETON_ASSEMBLY.getUnlocalizedName() + ".name").build());
        }

        public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
            return new SkeletonAssemblyContainer(playerInventory, this.world, this.position);
        }

        public String getGuiID() {
            return JurassiCraft.MODID + ":skeleton_assembly";
        }
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
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
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.setDefaultFacing(worldIn, pos, state);
        state = worldIn.getBlockState(pos);
        pos = this.getSister(pos, state);
        worldIn.setBlockState(pos, BlockHandler.SKELETON_ASSEMBLY_DUMMY.blockState.getBaseState().withProperty(FACING, state.getValue(FACING)));
    }

    private void setDefaultFacing(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote) {
            IBlockState blockNorth = world.getBlockState(pos.north());
            IBlockState blockSouth = world.getBlockState(pos.south());
            IBlockState blockWest = world.getBlockState(pos.west());
            IBlockState blockEast = world.getBlockState(pos.east());
            EnumFacing facing = state.getValue(FACING);

            if (facing == EnumFacing.NORTH && blockNorth.isFullBlock() && !blockSouth.isFullBlock()) {
                facing = EnumFacing.SOUTH;
            } else if (facing == EnumFacing.SOUTH && blockSouth.isFullBlock() && !blockNorth.isFullBlock()) {
                facing = EnumFacing.NORTH;
            } else if (facing == EnumFacing.WEST && blockWest.isFullBlock() && !blockEast.isFullBlock()) {
                facing = EnumFacing.EAST;
            } else if (facing == EnumFacing.EAST && blockEast.isFullBlock() && !blockWest.isFullBlock()) {
                facing = EnumFacing.WEST;
            }

            world.setBlockState(pos, state.withProperty(FACING, facing), 2);
        }
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getFront(meta);

        if (facing.getAxis() == EnumFacing.Axis.Y) {
            facing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    public static class DummyBlock extends SkeletonAssemblyBlock {
        public DummyBlock() {
            super();
            this.setUnlocalizedName("skeleton_assembly_dummy");
            this.setCreativeTab(null);
        }
        
        @Override
        public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {}
        
        @Override
        public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
            if (worldIn.isRemote) {
                return true;
            } else {
                pos = getSister(pos,state);
                super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
                return true;
            }
        }
        @Override
        public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
        {
            pos = getSister(pos,state);
            super.onBlockDestroyedByPlayer(worldIn, pos, state);
            worldIn.destroyBlock(pos, !player.capabilities.isCreativeMode);
            return;
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public boolean shouldSideBeRendered(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
            return true;
        }
        
        @Override
        public EnumBlockRenderType getRenderType(IBlockState state) {
            return EnumBlockRenderType.INVISIBLE;
        }
        
        @Override
        public BlockPos getSister(BlockPos pos, IBlockState state){
            
            EnumFacing facing = state.getValue(FACING);
            switch (facing) {
                case NORTH:
                    pos=pos.add(1,0,0);
                    break;
                case EAST:
                    pos=pos.add(0,0,1);
                    break;
                case SOUTH:
                    pos=pos.add(-1,0,0);
                    break;
                case WEST:
                    pos=pos.add(0,0,-1);
                    break;
                default:
                    break;
                    
            }
            return pos;
        }
        @Override
        public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
            return null;
        }
    }
}
