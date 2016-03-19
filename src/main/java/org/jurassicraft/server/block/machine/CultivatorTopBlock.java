package org.jurassicraft.server.block.machine;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.jurassicraft.server.block.JCBlockRegistry;

public class CultivatorTopBlock extends CultivatorBlock
{
    public CultivatorTopBlock()
    {
        super("top");
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult result, World world, BlockPos pos, EntityPlayer player)
    {
        Item item = Item.getItemFromBlock(JCBlockRegistry.cultivate_bottom);

        if (item == null)
        {
            return null;
        }

        Block block = item instanceof ItemBlock ? getBlockFromItem(item) : this;
        return new ItemStack(item, 1, block.getMetaFromState(world.getBlockState(pos)));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        BlockPos add = pos.add(0, -1, 0);
        IBlockState blockState = world.getBlockState(add);

        return blockState.getBlock().onBlockActivated(world, add, blockState, player, hand, stack, side, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        BlockPos bottomBlock = pos.add(0, -1, 0);

        if (world.getBlockState(bottomBlock).getBlock() != JCBlockRegistry.cultivate_bottom)
        {
            world.setBlockState(bottomBlock, JCBlockRegistry.cultivate_bottom.getDefaultState().withProperty(COLOR, state.getValue(COLOR)));
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        world.setBlockState(pos.add(0, -1, 0), Blocks.air.getDefaultState());
        super.breakBlock(world, pos, state);
    }
}
