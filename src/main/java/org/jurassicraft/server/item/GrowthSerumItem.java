package org.jurassicraft.server.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.creativetab.JCCreativeTabs;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class GrowthSerumItem extends Item
{
    public GrowthSerumItem()
    {
        super();
        this.setCreativeTab(JCCreativeTabs.items);
        this.setUnlocalizedName("growth_serum");
    }

    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target)
    {
        if (target instanceof DinosaurEntity)
        {
            DinosaurEntity dinosaur = (DinosaurEntity) target;

            if (!dinosaur.isCarcass())
            {
                dinosaur.increaseGrowthSpeed();
                // dinosaur.setAge(dinosaur.getDinosaurAge() + 750);

                stack.stackSize--;

                if (!player.capabilities.isCreativeMode)
                {
                    player.inventory.addItemStackToInventory(new ItemStack(JCItemRegistry.empty_syringe));
                }

                return true;
            }
        }

        return false;
    }
}
