package org.jurassicraft.server.storagedisc;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.jurassicraft.server.genetics.PlantDNA;

import java.util.List;

public class PlantDNAStorageType implements StorageType {
    private PlantDNA dna;

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        this.dna.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.dna = PlantDNA.readFromNBT(nbt);
    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip) {
        this.dna.addInformation(stack, tooltip);
    }
}
