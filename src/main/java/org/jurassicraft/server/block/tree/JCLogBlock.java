package org.jurassicraft.server.block.tree;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.creativetab.JCCreativeTabs;

public class JCLogBlock extends BlockLog
{
    private boolean petrified;

    public JCLogBlock(TreeType treeType, boolean petrified)
    {
        this.setDefaultState(getBlockState().getBaseState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
        this.setHardness(2.0F);
        this.setResistance(0.5F);
        this.setStepSound(Block.soundTypeWood);
        this.setCreativeTab(JCCreativeTabs.plants);
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
    public Material getMaterial()
    {
        return petrified ? Material.rock : super.getMaterial();
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
    protected BlockState createBlockState()
    {
        return new BlockState(this, LOG_AXIS);
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
