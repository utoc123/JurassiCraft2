package org.jurassicraft.server.storagedisc;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.jurassicraft.server.genetics.PlantDNA;

import java.util.List;

public class PlantDNAStorageType implements IStorageType
{
    private PlantDNA dna;

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        dna.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        dna = PlantDNA.readFromNBT(nbt);
    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip)
    {
        dna.addInformation(stack, tooltip);
    }
}
