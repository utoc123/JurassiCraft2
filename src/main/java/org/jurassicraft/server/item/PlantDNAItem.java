package org.jurassicraft.server.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.creativetab.TabHandler;
import org.jurassicraft.server.lang.AdvLang;
import org.jurassicraft.server.plant.Plant;
import org.jurassicraft.server.plant.PlantHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlantDNAItem extends Item
{
    public PlantDNAItem()
    {
        super();
        this.setCreativeTab(TabHandler.INSTANCE.plants);
        this.setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        String plantName = getPlant(stack).getName().toLowerCase().replaceAll(" ", "_");

        return new AdvLang("item.plant_dna.name").withProperty("plant", "plants." + plantName + ".name").build();
    }

    public Plant getPlant(ItemStack stack)
    {
        Plant plant = PlantHandler.INSTANCE.getPlantById(stack.getItemDamage());

        if (plant == null)
        {
            plant = PlantHandler.INSTANCE.small_royal_fern;
        }

        return plant;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subtypes)
    {
        List<Plant> plants = new ArrayList<Plant>(PlantHandler.INSTANCE.getPlants());

        Map<Plant, Integer> ids = new HashMap<Plant, Integer>();

        for (Plant plant : plants)
        {
            ids.put(plant, PlantHandler.INSTANCE.getPlantId(plant));
        }

        Collections.sort(plants);

        for (Plant plant : plants)
        {
            if (plant.shouldRegister())
            {
                subtypes.add(new ItemStack(item, 1, ids.get(plant)));
            }
        }
    }

    public int getDNAQuality(EntityPlayer player, ItemStack stack)
    {
        int quality = player.capabilities.isCreativeMode ? 100 : 0;

        NBTTagCompound nbt = stack.getTagCompound();

        if (nbt == null)
        {
            nbt = new NBTTagCompound();
        }

        if (nbt.hasKey("DNAQuality"))
        {
            quality = nbt.getInteger("DNAQuality");
        }
        else
        {
            nbt.setInteger("DNAQuality", quality);
        }

        stack.setTagCompound(nbt);

        return quality;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> lore, boolean advanced)
    {
        int quality = getDNAQuality(player, stack);

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

        lore.add(formatting + new AdvLang("lore.dna_quality.name").withProperty("quality", quality + "").build());
    }
}
