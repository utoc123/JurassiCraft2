package org.jurassicraft.common.item.vehicules;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jurassicraft.common.creativetab.JCCreativeTabs;
import org.jurassicraft.common.vehicles.helicopter.modules.HelicopterModule;

import java.util.List;

import static com.google.common.base.Preconditions.*;

public class ItemHeliModule extends Item
{

    private final Class<? extends HelicopterModule> module;
    private final String moduleID;

    public ItemHeliModule(String helicopterModuleID)
    {
        moduleID = helicopterModuleID;
        setUnlocalizedName("helicopter_module_"+helicopterModuleID);
        module = checkNotNull(HelicopterModule.registry.get(helicopterModuleID), "Invalid module id "+helicopterModuleID);
        setCreativeTab(JCCreativeTabs.items);
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
