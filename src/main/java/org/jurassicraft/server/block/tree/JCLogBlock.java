package org.jurassicraft.server.block.tree;

import net.minecraft.block.BlockLog;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.creativetab.TabHandler;

public class JCLogBlock extends BlockLog
{
    private boolean petrified;

    public JCLogBlock(TreeType treeType, boolean petrified)
    {
        this.setDefaultState(getBlockState().getBaseState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
        this.setHardness(2.0F);
        this.setResistance(0.5F);
        this.setStepSound(SoundType.WOOD);
        this.setCreativeTab(TabHandler.INSTANCE.plants);
        this.petrified = petrified;

        String name = treeType.name().toLowerCase() + "_log";

        if (petrified)
        {
            name += "_petrified";
            this.setHarvestLevel("pickaxe", 2);
            this.setHardness(4.0F);
            this.setResistance(4.0F);
        }

        this.setUnlocalizedName(name);
    }

    public boolean isPetrified()
    {
        return petrified;
    }

    @Override
    public Material getMaterial(IBlockState state)
    {
        return petrified ? Material.rock : super.getMaterial(state);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(LOG_AXIS, EnumAxis.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(LOG_AXIS).ordinal();
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
}
