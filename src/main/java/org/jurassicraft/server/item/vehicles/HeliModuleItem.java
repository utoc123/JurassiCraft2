package org.jurassicraft.server.item.vehicles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.creativetab.TabHandler;
import org.jurassicraft.server.vehicles.helicopter.modules.HelicopterModule;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class HeliModuleItem extends Item
{
    private final Class<? extends HelicopterModule> module;
    private final String moduleID;

    public HeliModuleItem(String helicopterModuleID)
    {
        moduleID = helicopterModuleID;
        module = checkNotNull(HelicopterModule.registry.get(helicopterModuleID), "Invalid module id " + helicopterModuleID);
        setCreativeTab(TabHandler.INSTANCE.items);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        super.addInformation(stack, playerIn, tooltip, advanced);
        tooltip.add("Right click on a helicopter to attach this module");
    }

    public Class<? extends HelicopterModule> getModule()
    {
        return module;
    }

    public String getModuleID()
    {
        return moduleID;
    }
}
