package org.jurassicraft.server.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.EntityHandler;
import org.jurassicraft.server.lang.LangHelper;
import org.jurassicraft.server.tab.TabHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SyringeItem extends DNAContainerItem
{
    public SyringeItem()
    {
        super();

        this.setCreativeTab(TabHandler.DNA);
        this.setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        String dinoName = getDinosaur(stack).getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

        return new LangHelper("item.syringe.name").withProperty("dino", "entity.jurassicraft." + dinoName + ".name").build();
    }

    public Dinosaur getDinosaur(ItemStack stack)
    {
        Dinosaur dinosaur = EntityHandler.getDinosaurById(stack.getItemDamage());

        if (dinosaur == null)
        {
            dinosaur = EntityHandler.ACHILLOBATOR;
        }

        return dinosaur;
    }

    @Override
    public int getContainerId(ItemStack stack)
    {
        return EntityHandler.getDinosaurId(getDinosaur(stack));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subtypes)
    {
        List<Dinosaur> dinosaurs = new ArrayList<>(EntityHandler.getDinosaurs());

        Map<Dinosaur, Integer> ids = new HashMap<>();

        for (Dinosaur dino : dinosaurs)
        {
            ids.put(dino, EntityHandler.getDinosaurId(dino));
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
}
