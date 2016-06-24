package org.jurassicraft.server.item.vehicles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.entity.vehicle.modules.HelicopterModule;
import org.jurassicraft.server.tab.TabHandler;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class HelicopterModuleItem extends Item
{
    private final Class<? extends HelicopterModule> module;
    private final String moduleID;

    public HelicopterModuleItem(String helicopterModuleID)
    {
        moduleID = helicopterModuleID;
        module = checkNotNull(HelicopterModule.registry.get(helicopterModuleID), "Invalid module id " + helicopterModuleID);
        setCreativeTab(TabHandler.ITEMS);
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
