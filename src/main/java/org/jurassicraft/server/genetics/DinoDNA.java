package org.jurassicraft.server.genetics;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.EntityHandler;
import org.jurassicraft.server.lang.LangHelper;

import java.util.List;
import java.util.Locale;

public class/* Bingo! */ DinoDNA
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

    public static DinoDNA fromStack(ItemStack stack)
    {
        return readFromNBT(stack.getTagCompound());
    }

    public static DinoDNA readFromNBT(NBTTagCompound nbt)
    {
        return new DinoDNA(EntityHandler.getDinosaurById(nbt.getInteger("Dinosaur")), nbt.getInteger("DNAQuality"), nbt.getString("Genetics"));
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("DNAQuality", quality);
        nbt.setString("Genetics", genetics);
        nbt.setString("StorageId", "DinoDNA");
        nbt.setInteger("Dinosaur", EntityHandler.getDinosaurId(dinosaur));
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
        tooltip.add(TextFormatting.DARK_AQUA + new LangHelper("lore.dinosaur.name").withProperty("dino", "entity.jurassicraft." + dinosaur.getName().toLowerCase(Locale.ENGLISH) + ".name").build());

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

        tooltip.add(colour + new LangHelper("lore.dna_quality.name").withProperty("quality", quality + "").build());
        tooltip.add(TextFormatting.BLUE + new LangHelper("lore.genetic_code.name").withProperty("code", genetics).build());
    }

    public Dinosaur getDinosaur()
    {
        return dinosaur;
    }
}
