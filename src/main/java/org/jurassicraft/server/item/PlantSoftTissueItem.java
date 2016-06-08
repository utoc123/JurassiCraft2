package org.jurassicraft.server.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.api.SequencableItem;
import org.jurassicraft.server.genetics.PlantDNA;
import org.jurassicraft.server.lang.LangHelper;
import org.jurassicraft.server.plant.Plant;
import org.jurassicraft.server.plant.PlantHandler;
import org.jurassicraft.server.tab.TabHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlantSoftTissueItem extends Item implements SequencableItem
{
    public PlantSoftTissueItem()
    {
        this.setHasSubtypes(true);
        this.setCreativeTab(TabHandler.INSTANCE.PLANTS);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        String plantName = getPlant(stack).getName().toLowerCase().replaceAll(" ", "_");

        return new LangHelper("item.plant_soft_tissue.name").withProperty("plant", "plants." + plantName + ".name").build();
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
        List<Plant> plants = new ArrayList<>(PlantHandler.INSTANCE.getPlants());

        Map<Plant, Integer> ids = new HashMap<>();

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

    @Override
    public boolean isSequencable(ItemStack stack)
    {
        return true;
    }

    @Override
    public ItemStack getSequenceOutput(ItemStack stack, Random random)
    {
        NBTTagCompound nbt = stack.getTagCompound();

        if (nbt == null)
        {
            nbt = new NBTTagCompound();
            PlantDNA dna = new PlantDNA(stack.getItemDamage(), SequencableItem.randomQuality(random));
            dna.writeToNBT(nbt);
        }

        ItemStack output = new ItemStack(ItemHandler.INSTANCE.STORAGE_DISC, 1, stack.getItemDamage());
        output.setTagCompound(nbt);

        return output;
    }
}
