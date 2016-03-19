package org.jurassicraft.server.block.tree;

import net.minecraft.block.BlockLog;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.creativetab.JCCreativeTabs;

public class JCLogBlock extends BlockLog
{
    public JCLogBlock(String treeName)
    {
        this.setDefaultState(this.blockState.getBaseState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
        this.setHardness(2.0F);
        this.setResistance(0.5F);
        this.setStepSound(SoundType.WOOD);
        this.setUnlocalizedName(treeName + "_log");

        this.setCreativeTab(JCCreativeTabs.plants);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return true;
    }

    public boolean isFullCube(IBlockState state)
    {
        return true;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState state = this.getDefaultState();

        switch (meta & 12)
        {
            case 0:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
                break;
            case 4:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
                break;
            case 8:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
                break;
            default:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
        }

        return state;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;

        switch (JCLogBlock.SwitchEnumAxis.AXIS_LOOKUP[state.getValue(LOG_AXIS).ordinal()])
        {
            case 1:
                i = 4;
                break;
            case 2:
                i = 8;
                break;
            case 3:
                i = 12;
        }

        return i;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, LOG_AXIS);
    }

    @Override
    protected ItemStack createStackedBlock(IBlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(this), 1, 0);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return 0;
    }

    static final class SwitchEnumAxis
    {
        static final int[] AXIS_LOOKUP = new int[BlockLog.EnumAxis.values().length];

        static
        {
            AXIS_LOOKUP[BlockLog.EnumAxis.X.ordinal()] = 1;
            AXIS_LOOKUP[BlockLog.EnumAxis.Z.ordinal()] = 2;
            AXIS_LOOKUP[BlockLog.EnumAxis.NONE.ordinal()] = 3;
        }
    }
}
