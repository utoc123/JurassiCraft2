package org.jurassicraft.server.item.vehicles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.jurassicraft.server.creativetab.TabHandler;
import org.jurassicraft.server.vehicles.helicopter.HelicopterBaseEntity;

import java.util.List;
import java.util.UUID;

public class HelicopterItem extends Item
{
    public HelicopterItem()
    {
        setCreativeTab(TabHandler.INSTANCE.items);
        setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        super.addInformation(stack, playerIn, tooltip, advanced);
        tooltip.add("Right click on a block to spawn the helicopter");
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        HelicopterBaseEntity helicopter = new HelicopterBaseEntity(worldIn, UUID.randomUUID());
        helicopter.setPosition(pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ);
        worldIn.spawnEntityInWorld(helicopter);
        return true;
    }
}
