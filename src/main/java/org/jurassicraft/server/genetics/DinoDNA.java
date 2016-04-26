package org.jurassicraft.server.genetics;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.EntityHandler;
import org.jurassicraft.server.lang.AdvLang;

import java.util.List;

public class DinoDNA
{
    private int quality;
    private String genetics;
    private Dinosaur dinosaur;

    public DinoDNA(Dinosaur dinosaur, int quality, String genetics)
    {
        this.quality = quality;
        this.genetics = genetics;
        this.dinosaur = dinosaur;
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("DNAQuality", quality);
        nbt.setString("Genetics", genetics);
        nbt.setString("StorageId", "DinoDNA");
        nbt.setInteger("Dinosaur", EntityHandler.INSTANCE.getDinosaurId(dinosaur));
    }

    public static DinoDNA fromStack(ItemStack stack)
    {
        return readFromNBT(stack.getTagCompound());
    }

    public static DinoDNA readFromNBT(NBTTagCompound nbt)
    {
        return new DinoDNA(EntityHandler.INSTANCE.getDinosaurById(nbt.getInteger("Dinosaur")), nbt.getInteger("DNAQuality"), nbt.getString("Genetics"));
    }

    public int getDNAQuality()
    {
        return quality;
    }

    public String getGenetics()
    {
        return genetics;
    }

    public void addInformation(ItemStack stack, List<String> tooltip)
    {
        tooltip.add(TextFormatting.DARK_AQUA + new AdvLang("lore.dinosaur.name").withProperty("dino", "entity.jurassicraft." + dinosaur.getName().toLowerCase() + ".name").build());

        TextFormatting colour;

        if (quality > 75)
        {
            colour = TextFormatting.GREEN;
        }
        else if (quality > 50)
        {
            colour = TextFormatting.YELLOW;
        }
        else if (quality > 25)
        {
            colour = TextFormatting.GOLD;
        }
        else
        {
            colour = TextFormatting.RED;
        }

        tooltip.add(colour + new AdvLang("lore.dna_quality.name").withProperty("quality", quality + "").build());
        tooltip.add(TextFormatting.BLUE + new AdvLang("lore.genetic_code.name").withProperty("code", genetics).build());
    }

    public Dinosaur getDinosaur()
    {
        return dinosaur;
    }
}
