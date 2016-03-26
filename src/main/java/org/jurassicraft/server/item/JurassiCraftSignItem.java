package org.jurassicraft.server.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (side == EnumFacing.DOWN)
        {
            return false;
        }
        else if (side == EnumFacing.UP)
        {
            return false;
        }
        else
        {
            BlockPos blockpos1 = pos.offset(side);

            if (!playerIn.canPlayerEdit(blockpos1, side, stack))
            {
                return false;
            }
            else
            {
                JurassiCraftSignEntity sign = new JurassiCraftSignEntity(worldIn, blockpos1, side);

                if (sign.onValidSurface())
                {
                    if (!worldIn.isRemote)
                    {
                        worldIn.spawnEntityInWorld(sign);
                    }

                    --stack.stackSize;
                }

                return true;
            }
        }
    }
}
