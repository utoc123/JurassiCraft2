package org.jurassicraft.server.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.creativetab.TabHandler;

import java.util.Random;

public class DoublePlantBlock extends BlockBush
{
    public static final PropertyEnum HALF = PropertyEnum.create("half", DoublePlantBlock.EnumBlockHalf.class);

    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.1F, 0.0F, 0.1F, 0.9F, 1.0F, 0.9F);

    public DoublePlantBlock(Material material)
    {
        super(material);
        this.setHardness(0.0F);
        this.setStepSound(SoundType.PLANT);
        this.setCreativeTab(TabHandler.INSTANCE.plants);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BOUNDS;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos)
    {
        return super.canPlaceBlockAt(world, pos) && world.isAirBlock(pos.up());
    }

    /**
     * Whether this Block can be replaced directly by other blocks (true for e.g. tall grass)
     */
    @Override
    public boolean isReplaceable(IBlockAccess world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);

        return state.getBlock() != this;
    }

    @Override
    protected void checkAndDropBlock(World world, BlockPos pos, IBlockState state)
    {
        if (!this.canBlockStay(world, pos, state))
        {
            boolean upperPart = state.getValue(HALF) == DoublePlantBlock.EnumBlockHalf.UPPER;
            BlockPos top = upperPart ? pos : pos.up();
            BlockPos bottom = upperPart ? pos.down() : pos;
            Block topBlock = upperPart ? this : world.getBlockState(top).getBlock();
            Block bottomBlock = upperPart ? world.getBlockState(bottom).getBlock() : this;

            if (!upperPart)
            {
                this.dropBlockAsItem(world, pos, state, 0);
            }

            if (topBlock == this)
            {
                world.setBlockState(top, Blocks.air.getDefaultState(), 3);
            }

            if (bottomBlock == this)
            {
                world.setBlockState(bottom, Blocks.air.getDefaultState(), 3);
            }
        }
    }

    @Override
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getBlock() != this)
        {
            return super.canBlockStay(worldIn, pos, state); // Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
        }
        if (state.getValue(HALF) == DoublePlantBlock.EnumBlockHalf.UPPER)
        {
            return worldIn.getBlockState(pos.down()).getBlock() == this;
        }
        else
        {
            IBlockState iblockstate1 = worldIn.getBlockState(pos.up());
            return iblockstate1.getBlock() == this && super.canBlockStay(worldIn, pos, iblockstate1);
        }
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        if (state.getValue(HALF) == DoublePlantBlock.EnumBlockHalf.UPPER)
        {
            return null;
        }
        else
        {
            return super.getItemDropped(state, rand, fortune);
        }
    }

    /**
     * Get the damage value that this Block should drop
     */
    @Override
    public int damageDropped(IBlockState state)
    {
        return 0;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, this.getDefaultState().withProperty(HALF, EnumBlockHalf.LOWER), 2);
        worldIn.setBlockState(pos.up(), this.getDefaultState().withProperty(HALF, DoublePlantBlock.EnumBlockHalf.UPPER), 2);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (state.getValue(HALF) == DoublePlantBlock.EnumBlockHalf.UPPER)
        {
            if (worldIn.getBlockState(pos.down()).getBlock() == this)
            {
                if (!player.capabilities.isCreativeMode)
                {
                    IBlockState iblockstate1 = worldIn.getBlockState(pos.down());

                    if (iblockstate1.getBlock() == this)
                    {
                        worldIn.destroyBlock(pos.down(), true);
                    }
                }
                else
                {
                    worldIn.setBlockToAir(pos.down());
                }
            }
        }
        else if (player.capabilities.isCreativeMode && worldIn.getBlockState(pos.up()).getBlock() == this)
        {
            worldIn.setBlockState(pos.up(), Blocks.air.getDefaultState(), 2);
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(HALF, DoublePlantBlock.EnumBlockHalf.values()[meta]);
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the metadata, such as fence connections.
     */
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((DoublePlantBlock.EnumBlockHalf) state.getValue(HALF)).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, HALF);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        if (state.getBlock() == this && state.getValue(HALF) == EnumBlockHalf.LOWER && world.getBlockState(pos.up()).getBlock() == this)
        {
            world.setBlockToAir(pos.up());
        }

        return world.setBlockToAir(pos);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Block.EnumOffsetType getOffsetType()
    {
        return EnumOffsetType.XZ;
    }

    enum EnumBlockHalf implements IStringSerializable
    {
        UPPER, LOWER;

        @Override
        public String toString()
        {
            return this.getName();
        }

        @Override
        public String getName()
        {
            return this == UPPER ? "upper" : "lower";
        }
    }
}
