package org.jurassicraft.server.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.api.ISequencableItem;
import org.jurassicraft.server.creativetab.TabHandler;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.EntityHandler;
import org.jurassicraft.server.genetics.DinoDNA;
import org.jurassicraft.server.genetics.GeneticsHelper;
import org.jurassicraft.server.lang.AdvLang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SoftTissueItem extends Item implements ISequencableItem
{
    public SoftTissueItem()
    {
        this.setHasSubtypes(true);

        this.setCreativeTab(TabHandler.INSTANCE.dna);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        String dinoName = getDinosaur(stack).getName().toLowerCase().replaceAll(" ", "_");

        return new AdvLang("item.soft_tissue.name").withProperty("dino", "entity.jurassicraft." + dinoName + ".name").build();
    }

    public Dinosaur getDinosaur(ItemStack stack)
    {
        Dinosaur dinosaur = EntityHandler.INSTANCE.getDinosaurById(stack.getItemDamage());

        if (dinosaur == null)
        {
            dinosaur = EntityHandler.INSTANCE.achillobator;
        }

        return dinosaur;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subtypes)
    {
        List<Dinosaur> dinosaurs = new ArrayList<>(EntityHandler.INSTANCE.getDinosaurs());

        Map<Dinosaur, Integer> ids = new HashMap<>();

        for (Dinosaur dino : dinosaurs)
        {
            ids.put(dino, EntityHandler.INSTANCE.getDinosaurId(dino));
        }

        Collections.sort(dinosaurs);

        for (Dinosaur dino : dinosaurs)
        {
            if (dino.shouldRegister())
            {
                subtypes.add(new ItemStack(item, 1, ids.get(dino)));
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
            int quality = ISequencableItem.randomQuality(random);
            DinoDNA dna = new DinoDNA(EntityHandler.INSTANCE.getDinosaurById(stack.getItemDamage()), quality, GeneticsHelper.randomGenetics(random));
            dna.writeToNBT(nbt);
        }

        ItemStack output = new ItemStack(ItemHandler.INSTANCE.storage_disc, 1, stack.getItemDamage());
        output.setTagCompound(nbt);

        return output;
    }
}
