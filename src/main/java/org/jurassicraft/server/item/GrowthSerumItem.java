package org.jurassicraft.server.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.jurassicraft.server.creativetab.TabHandler;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class GrowthSerumItem extends Item
{
    public GrowthSerumItem()
    {
        super();
        this.setCreativeTab(TabHandler.INSTANCE.items);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand)
    {
        if (target instanceof DinosaurEntity)
        {
            DinosaurEntity dinosaur = (DinosaurEntity) target;

            if (!dinosaur.isCarcass())
            {
                dinosaur.increaseGrowthSpeed();

                stack.stackSize--;

                if (!player.capabilities.isCreativeMode)
                {
                    player.inventory.addItemStackToInventory(new ItemStack(ItemHandler.INSTANCE.empty_syringe));
                }

                return true;
            }
        }

        return false;
    }
}
