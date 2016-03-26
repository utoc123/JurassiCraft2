package org.jurassicraft.server.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.server.creativetab.TabHandler;
import org.jurassicraft.server.entity.item.JurassiCraftSignEntity;

public class JurassiCraftSignItem extends Item
{
    public JurassiCraftSignItem()
    {
        this.setCreativeTab(TabHandler.INSTANCE.items);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (side != EnumFacing.DOWN && side != EnumFacing.UP)
        {
            BlockPos offset = pos.offset(side);

            if (player.canPlayerEdit(offset, side, stack))
            {
                JurassiCraftSignEntity sign = new JurassiCraftSignEntity(world, offset, side);

                if (sign.onValidSurface())
                {
                    if (!world.isRemote)
                    {
                        world.spawnEntityInWorld(sign);
                    }

                    --stack.stackSize;
                }

                return EnumActionResult.SUCCESS;
            }
        }

        return EnumActionResult.PASS;
    }
}
