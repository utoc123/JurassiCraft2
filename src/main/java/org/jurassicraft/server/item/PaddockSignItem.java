package org.jurassicraft.server.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.jurassicraft.server.creativetab.TabHandler;
import org.jurassicraft.server.handler.JCGuiHandler;

public class PaddockSignItem extends Item
{
    public PaddockSignItem()
    {
        this.setCreativeTab(TabHandler.INSTANCE.items);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
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
            BlockPos placePos = pos.offset(side);

            if (!player.canPlayerEdit(placePos, side, stack))
            {
                return false;
            }
            else
            {
                if (player.worldObj.isRemote)
                {
                    JCGuiHandler.openSelectDino(player, placePos, side);
                }

//                int dinosaur = getDinosaur(stack);
//
//                if (dinosaur != -1)
//                {
//                    PaddockSignEntity paddockSign = new PaddockSignEntity(world, placePos, side, dinosaur);
//
//                    if (paddockSign.onValidSurface())
//                    {
//                        if (!world.isRemote)
//                        {
//                            world.spawnEntityInWorld(paddockSign);
//                        }
//
//                        --stack.stackSize;
//
//                        return true;
//                    }
//                }
            }
        }

        return false;
    }
}
