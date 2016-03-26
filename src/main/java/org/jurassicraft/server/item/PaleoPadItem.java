package org.jurassicraft.server.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.jurassicraft.server.creativetab.TabHandler;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.handler.JCGuiHandler;

public class PaleoPadItem extends Item
{
    public PaleoPadItem()
    {
        super();
        this.setMaxStackSize(1);
        this.setCreativeTab(TabHandler.INSTANCE.items);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        JCGuiHandler.openPaleoPad(player);

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand)
    {
        if (target instanceof DinosaurEntity)
        {
            DinosaurEntity dino = (DinosaurEntity) target;

            JCGuiHandler.openViewDinosaur(dino);

            return true;
        }

        return false;
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and update it's contents.
     */
    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
    {
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entity;

            setString(stack, "LastOwner", player.getUniqueID().toString());
        }
    }

    public void setString(ItemStack stack, String key, String value)
    {
        NBTTagCompound nbt = getNBT(stack);

        nbt.setString(key, value);

        stack.setTagCompound(nbt);
    }

    private NBTTagCompound getNBT(ItemStack stack)
    {
        NBTTagCompound nbt = stack.getTagCompound();

        if (nbt == null)
        {
            nbt = new NBTTagCompound();
        }

        return nbt;
    }
}
