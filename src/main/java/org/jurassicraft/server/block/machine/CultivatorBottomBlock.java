package org.jurassicraft.server.block.machine;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.creativetab.TabHandler;
import org.jurassicraft.server.tileentity.CultivatorTile;

public class CultivatorBottomBlock extends CultivatorBlock
{
    public CultivatorBottomBlock()
    {
        super("bottom");
        this.setCreativeTab(TabHandler.INSTANCE.blocks);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return super.canPlaceBlockAt(worldIn, pos) && worldIn.isAirBlock(pos.up());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        if (stack.hasDisplayName())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof CultivatorTile)
            {
                ((CultivatorTile) tileentity).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            return true;
        }
        else if (!player.isSneaking())
        {
            TileEntity tileEntity = world.getTileEntity(pos);

            if (tileEntity instanceof CultivatorTile)
            {
                CultivatorTile cultivator = (CultivatorTile) tileEntity;

                if (cultivator.isUseableByPlayer(player))
                {
                    player.openGui(JurassiCraft.INSTANCE, 10, world, pos.getX(), pos.getY(), pos.getZ());
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        BlockPos topBlock = pos.add(0, 1, 0);

        Block block = worldIn.getBlockState(topBlock).getBlock();

        if (block == Blocks.air)
        {
            worldIn.setBlockState(topBlock, BlockHandler.INSTANCE.cultivate_top.getDefaultState().withProperty(COLOR, state.getValue(COLOR)));
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.setBlockState(pos.add(0, 1, 0), Blocks.air.getDefaultState());
        dropItems(worldIn, pos);

        super.breakBlock(worldIn, pos, state);
    }
}
