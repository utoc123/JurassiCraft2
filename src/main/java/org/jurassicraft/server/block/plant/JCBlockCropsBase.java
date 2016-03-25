package org.jurassicraft.server.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A configurable rework of the Minecraft BlockCrops.
 */
public abstract class JCBlockCropsBase extends BlockBush implements IGrowable
{
    protected int seedDropMin = 0;
    protected int seedDropMax = 3;
    protected int cropDropMin = 1;
    protected int cropDropMax = 1;

    protected JCBlockCropsBase()
    {
        this.setDefaultState(this.blockState.getBaseState().withProperty(getAgeProperty(), Integer.valueOf(0)));
        this.setTickRandomly(true);
        float f = 0.5F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);

        // NOTE: No tab because the seeds are placed not the plant.
        this.setCreativeTab((CreativeTabs) null);
        this.setHardness(0.0F);
        this.setStepSound(soundTypeGrass);
        this.disableStats();
    }

    abstract protected PropertyInteger getAgeProperty();

    abstract protected int getMaxAge();

    // NOTE:  This is called on parent object construction.
    abstract protected BlockState createBlockState();

    abstract protected Item getSeed();

    abstract protected Item getCrop();

    //============================================

    /**
     * is the block grass, dirt or farmland
     */
    protected boolean canPlaceBlockOn(Block ground)
    {
        return ground == Blocks.farmland;
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(worldIn, pos, state, rand);

        if (worldIn.getLightFromNeighbors(pos.up()) >= 9)
        {
            int i = ((Integer) state.getValue(getAgeProperty())).intValue();

            if (i < this.getMaxAge())
            {
                float f = getGrowthChance(this, worldIn, pos);

                if (rand.nextInt((int) (25.0F / f) + 1) == 0)
                {
                    worldIn.setBlockState(pos, state.withProperty(getAgeProperty(), Integer.valueOf(i + 1)), 2);
                }
            }
        }
    }

    public void grow(World worldIn, BlockPos pos, IBlockState state)
    {
        // TODO:  Pull out these two numbers.
        int i = ((Integer) state.getValue(getAgeProperty())).intValue() + MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);

        if (i > this.getMaxAge())
        {
            i = this.getMaxAge();
        }

        worldIn.setBlockState(pos, state.withProperty(getAgeProperty(), Integer.valueOf(i)), 2);
    }

    protected static float getGrowthChance(Block blockIn, World worldIn, BlockPos pos)
    {
        float f = 1.0F;
        BlockPos blockpos = pos.down();

        for (int i = -1; i <= 1; ++i)
        {
            for (int j = -1; j <= 1; ++j)
            {
                float f1 = 0.0F;
                IBlockState iblockstate = worldIn.getBlockState(blockpos.add(i, 0, j));

                if (iblockstate.getBlock().canSustainPlant(worldIn, blockpos.add(i, 0, j),
                        net.minecraft.util.EnumFacing.UP, (net.minecraftforge.common.IPlantable) blockIn))
                {
                    f1 = 1.0F;

                    if (iblockstate.getBlock().isFertile(worldIn, blockpos.add(i, 0, j)))
                    {
                        f1 = 3.0F;
                    }
                }

                if (i != 0 || j != 0)
                {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        BlockPos blockpos1 = pos.north();
        BlockPos blockpos2 = pos.south();
        BlockPos blockpos3 = pos.west();
        BlockPos blockpos4 = pos.east();
        boolean flag = blockIn == worldIn.getBlockState(blockpos3).getBlock() || blockIn == worldIn.getBlockState(blockpos4).getBlock();
        boolean flag1 = blockIn == worldIn.getBlockState(blockpos1).getBlock() || blockIn == worldIn.getBlockState(blockpos2).getBlock();

        if (flag && flag1)
        {
            f /= 2.0F;
        }
        else
        {
            boolean flag2 = blockIn == worldIn.getBlockState(blockpos3.north()).getBlock() ||
                    blockIn == worldIn.getBlockState(blockpos4.north()).getBlock() ||
                    blockIn == worldIn.getBlockState(blockpos4.south()).getBlock() ||
                    blockIn == worldIn.getBlockState(blockpos3.south()).getBlock();

            if (flag2)
            {
                f /= 2.0F;
            }
        }

        return f;
    }

    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        return (worldIn.getLight(pos) >= 8 || worldIn.canSeeSky(pos)) &&
                worldIn.getBlockState(pos.down()).getBlock().canSustainPlant(worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
    }

    /**
     * Whether this IGrowable can grow
     */
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return ((Integer) state.getValue(getAgeProperty())).intValue() < this.getMaxAge();
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, BlockPos pos)
    {
        return this.getSeed();
    }

    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        this.grow(worldIn, pos, state);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(getAgeProperty(), Integer.valueOf(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer) state.getValue(getAgeProperty())).intValue();
    }


    //==============

    /**
     * Spawns this Block's drops into the World as EntityItems.
     */
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return state.getValue(getAgeProperty()) == this.getMaxAge() ? this.getCrop() : this.getSeed();
    }


    @Override
    public java.util.List<ItemStack> getDrops(net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        List<ItemStack> drops = new ArrayList<ItemStack>();

        int age = state.getValue(getAgeProperty());
        Random rand = world instanceof World ? ((World) world).rand : new Random();

        if (age < this.getMaxAge())
        {
            drops.add(new ItemStack(getSeed()));
        }
        else
        {
            // Drop range of leaves and range of seeds
            if (seedDropMin > 0 && seedDropMax > 0)
                drops.add(new ItemStack(getSeed(), MathHelper.getRandomIntegerInRange(rand, seedDropMin, seedDropMax)));
            if (cropDropMin > 0 && cropDropMax > 0)
                drops.add(new ItemStack(getCrop(), MathHelper.getRandomIntegerInRange(rand, cropDropMin, cropDropMax)));
        }

        return drops;
    }

}
