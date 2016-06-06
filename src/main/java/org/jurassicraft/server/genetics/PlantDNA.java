package org.jurassicraft.server.genetics;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import org.jurassicraft.server.lang.LangHelper;
import org.jurassicraft.server.plant.PlantHandler;

import java.util.List;

public class PlantDNA
{
    private int plant;
    private int quality;

    public PlantDNA(int plant, int quality)
    {
        this.plant = plant;
        this.quality = quality;
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("DNAQuality", quality);
        nbt.setInteger("Plant", plant);
        nbt.setString("StorageId", "PlantDNA");
    }

    public static PlantDNA fromStack(ItemStack stack)
    {
        return readFromNBT(stack.getTagCompound());
    }

    public static PlantDNA readFromNBT(NBTTagCompound nbt)
    {
        return new PlantDNA(nbt.getInteger("Plant"), nbt.getInteger("DNAQuality"));
    }

    public int getDNAQuality()
    {
        return quality;
    }

    public int getPlant()
    {
        return plant;
    }

    public void addInformation(ItemStack stack, List<String> tooltip)
    {
        tooltip.add(TextFormatting.DARK_AQUA + new LangHelper("lore.plant.name").withProperty("plant", "plants." + PlantHandler.INSTANCE.getPlantById(plant).getName().toLowerCase().replaceAll(" ", "_") + ".name").build());

        TextFormatting formatting;

        if (quality > 75)
        {
            formatting = TextFormatting.GREEN;
        }
        else if (quality > 50)
        {
            formatting = TextFormatting.YELLOW;
        }
        else if (quality > 25)
        {
            formatting = TextFormatting.GOLD;
        }
        else
        {
            formatting = TextFormatting.RED;
        }

        tooltip.add(formatting + new LangHelper("lore.dna_quality.name").withProperty("quality", quality + "").build());
    }
}
