package org.jurassicraft.server.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.jurassicraft.server.achievements.AchievementHandler;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.EncasedFossilBlock;
import org.jurassicraft.server.block.FossilBlock;
import org.jurassicraft.server.creativetab.TabHandler;

public class PlasterAndBandageItem extends Item
{
    public PlasterAndBandageItem()
    {
        super();

        this.setCreativeTab(TabHandler.INSTANCE.items);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            return true;
        }
        else if (!player.canPlayerEdit(pos.offset(side), side, stack))
        {
            return false;
        }
        else
        {
            IBlockState state = world.getBlockState(pos);

            Block block = state.getBlock();

            if (block instanceof FossilBlock)
            {
                int id = BlockHandler.INSTANCE.getDinosaurId((FossilBlock) block, block.getMetaFromState(state));

                world.setBlockState(pos, BlockHandler.INSTANCE.getEncasedFossil(id).getDefaultState().withProperty(EncasedFossilBlock.VARIANT, BlockHandler.INSTANCE.getMetadata(id)));

                if (!player.capabilities.isCreativeMode)
                {
                    stack.stackSize--;
                }

                player.addStat(AchievementHandler.INSTANCE.fossils, 1);

                return true;
            }
        }

        return false;
    }
}
