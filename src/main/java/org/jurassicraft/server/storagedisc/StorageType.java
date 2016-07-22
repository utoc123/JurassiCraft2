package org.jurassicraft.server.storagedisc;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public interface StorageType {
    void writeToNBT(NBTTagCompound nbt);

    void readFromNBT(NBTTagCompound nbt);

    void addInformation(ItemStack stack, List<String> tooltip);
}
