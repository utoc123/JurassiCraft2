package org.jurassicraft.server.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.server.creativetab.JCCreativeTabs;
import org.jurassicraft.server.handler.JCGuiHandler;

public class PaddockSignItem extends Item
{
    public PaddockSignItem()
    {
        this.setCreativeTab(JCCreativeTabs.items);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (side != EnumFacing.DOWN && side != EnumFacing.UP)
        {
            BlockPos offset = pos.offset(side);

            if (player.worldObj.isRemote && player.canPlayerEdit(offset, side, stack))
            {
                JCGuiHandler.openSelectDino(player, offset, side, hand);
            }
        }

        return EnumActionResult.PASS;
    }
}
