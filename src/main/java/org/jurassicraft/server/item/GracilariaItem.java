package org.jurassicraft.server.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

/**
 * Copyright 2016 Andrew O. Mellinger
 */
public class GracilariaItem extends Item implements IPlantable
{
    private Block seaweedBlock;

    public GracilariaItem(Block crops)
    {
        this.seaweedBlock = crops;
        setUnlocalizedName("gracilaria");
    }

    //  ___ _
    // |_ _| |_ ___ _ __ ___
    //  | || __/ _ \ '_ ` _ \
    //  | || ||  __/ | | | | |
    // |___|\__\___|_| |_| |_|

    /**
     * Called when a Block is right-clicked with this Item
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn,
            BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        // NOTE:  Pos is the block we are placing ON

        // Based on ItemSeeds.
        if (side != EnumFacing.UP)
        {
            return false;
        }
        else if (!playerIn.canPlayerEdit(pos.offset(side), side, stack))
        {
            return false;
        }
        else if (seaweedBlock.canPlaceBlockAt(worldIn, pos.up()))
        {
            worldIn.setBlockState(pos.up(), this.seaweedBlock.getDefaultState());
            --stack.stackSize;
            return true;
        }
        else
        {
            return false;
        }
    }

    //  ___ ____  _             _        _     _
    // |_ _|  _ \| | __ _ _ __ | |_ __ _| |__ | | ___
    //  | || |_) | |/ _` | '_ \| __/ _` | '_ \| |/ _ \
    //  | ||  __/| | (_| | | | | || (_| | |_) | |  __/
    // |___|_|   |_|\__,_|_| |_|\__\__,_|_.__/|_|\___|

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
    {
        return EnumPlantType.Crop;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos)
    {
        return null;
    }
}
