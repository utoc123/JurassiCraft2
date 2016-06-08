package org.jurassicraft.server.block.tree;

import net.minecraft.block.BlockLog;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.jurassicraft.server.api.GrindableItem;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.plant.PlantHandler;
import org.jurassicraft.server.tab.TabHandler;

import java.util.Random;

public class AncientLogBlock extends BlockLog implements GrindableItem
{
    private boolean petrified;
    private TreeType type;

    public AncientLogBlock(TreeType treeType, boolean petrified)
    {
        this.setDefaultState(getBlockState().getBaseState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
        this.setHardness(2.0F);
        this.setResistance(0.5F);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(TabHandler.INSTANCE.PLANTS);
        this.petrified = petrified;
        this.type = treeType;

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

    public TreeType getType()
    {
        return type;
    }

    public boolean isPetrified()
    {
        return petrified;
    }

    @Override
    public Material getMaterial(IBlockState state)
    {
        return petrified ? Material.ROCK : super.getMaterial(state);
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

    @Override
    public boolean isGrindable(ItemStack stack)
    {
        return isPetrified();
    }

    @Override
    public ItemStack getGroundItem(ItemStack stack, Random random)
    {
        NBTTagCompound tag = stack.getTagCompound();

        int outputType = random.nextInt(6);

        if (outputType == 5)
        {
            ItemStack output = new ItemStack(ItemHandler.INSTANCE.PLANT_SOFT_TISSUE, 1, PlantHandler.INSTANCE.getPlantId(type.getPlant()));
            output.setTagCompound(tag);
            return output;
        }
        else if (outputType < 3)
        {
            return new ItemStack(Items.DYE, 1, 15);
        }

        return new ItemStack(Items.FLINT);
    }
}
