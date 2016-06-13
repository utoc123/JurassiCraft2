package org.jurassicraft.server.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.handler.GuiHandler;
import org.jurassicraft.server.tab.TabHandler;

public class FieldGuideItem extends Item
{
    public FieldGuideItem()
    {
        super();
        this.setCreativeTab(TabHandler.INSTANCE.ITEMS);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand)
    {
        if (target instanceof DinosaurEntity)
        {
            if (player.worldObj.isRemote)
            {
                GuiHandler.openFieldGuide((DinosaurEntity) target);
            }

            return true;
        }

        return false;
    }
}
