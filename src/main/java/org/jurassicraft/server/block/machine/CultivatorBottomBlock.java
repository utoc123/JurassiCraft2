package org.jurassicraft.server.block.machine;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.block.JCBlockRegistry;
import org.jurassicraft.server.creativetab.JCCreativeTabs;
import org.jurassicraft.server.tileentity.CultivatorTile;

public class CultivatorBottomBlock extends CultivatorBlock
{
    public CultivatorBottomBlock()
    {
        super("bottom");
        this.setCreativeTab(JCCreativeTabs.blocks);
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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            return true;
        }
        else if (!player.isSneaking())
        {
            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof CultivatorTile)
            {
                CultivatorTile cultivator = (CultivatorTile) tile;

                if (cultivator.isUseableByPlayer(player))
                {
                    player.openGui(JurassiCraft.instance, 10, world, pos.getX(), pos.getY(), pos.getZ());
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        BlockPos topBlock = pos.add(0, 1, 0);

        Block block = world.getBlockState(topBlock).getBlock();

        if (block == Blocks.air)
        {
            world.setBlockState(topBlock, JCBlockRegistry.cultivate_top.getDefaultState().withProperty(COLOR, state.getValue(COLOR)));
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.setBlockState(pos.add(0, 1, 0), Blocks.air.getDefaultState());
        super.breakBlock(worldIn, pos, state);
    }
}
