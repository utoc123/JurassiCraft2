package org.jurassicraft.server.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.api.ISynthesizableItem;
import org.jurassicraft.server.creativetab.TabHandler;
import org.jurassicraft.server.storagedisc.IStorageType;
import org.jurassicraft.server.storagedisc.StorageTypeRegistry;

import java.util.List;
import java.util.Random;

public class StorageDiscItem extends Item implements ISynthesizableItem
{
    public StorageDiscItem()
    {
        super();
        this.setCreativeTab(TabHandler.INSTANCE.items);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        NBTTagCompound nbt = stack.getTagCompound();

        if (nbt != null)
        {
            String storageId = nbt.getString("StorageId");
            IStorageType type = StorageTypeRegistry.INSTANCE.getStorageType(storageId);

            if (type != null)
            {
                type.readFromNBT(nbt);
                type.addInformation(stack, tooltip);
            }
        }
        else
        {
            tooltip.add(TextFormatting.RED + I18n.translateToLocal("cage.empty.name"));
        }
    }

    @Override
    public boolean isSynthesizable(ItemStack stack)
    {
        NBTTagCompound tagCompound = stack.getTagCompound();
        return tagCompound != null && tagCompound.hasKey("DNAQuality") && tagCompound.getInteger("DNAQuality") == 100;
    }

    @Override
    public ItemStack getSynthesizedItem(ItemStack stack, Random random)
    {
        ItemStack output;

        if (!stack.getTagCompound().getString("StorageId").equalsIgnoreCase("PlantDNA"))
        {
            output = new ItemStack(ItemHandler.INSTANCE.dna, 1, stack.getItemDamage());
        }
        else
        {
            output = new ItemStack(ItemHandler.INSTANCE.plant_dna, 1, stack.getItemDamage());
        }

        output.setTagCompound(stack.getTagCompound());

        return output;
    }
}
