package org.jurassicraft.server.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
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
        this.setUnlocalizedName("gracilaria");
    }

    //  ___ _
    // |_ _| |_ ___ _ __ ___
    //  | || __/ _ \ '_ ` _ \
    //  | || ||  __/ | | | | |
    // |___|\__\___|_| |_| |_|

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        // NOTE:  Pos is the block we are placing ON

        // Based on ItemSeeds.
        if (side != EnumFacing.UP || !player.canPlayerEdit(pos.offset(side), side, stack))
        {
            return EnumActionResult.PASS;
        }
        else if (seaweedBlock.canPlaceBlockAt(world, pos.up()))
        {
            world.setBlockState(pos.up(), this.seaweedBlock.getDefaultState());
            --stack.stackSize;
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
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
