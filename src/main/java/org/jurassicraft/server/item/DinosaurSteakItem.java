package org.jurassicraft.server.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.creativetab.JCCreativeTabs;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.JCEntityRegistry;
import org.jurassicraft.server.lang.AdvLang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DinosaurSteakItem extends ItemFood
{
    public DinosaurSteakItem()
    {
        super(8, 0.8F, true);

        this.setHasSubtypes(true);

        this.setCreativeTab(JCCreativeTabs.foods);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        Dinosaur dinosaur = this.getDinosaur(stack);

        return new AdvLang("item.dinosaur_steak.name").withProperty("dino", "entity.jurassicraft." + dinosaur.getName().replace(" ", "_").toLowerCase() + ".name").build();
    }

    public Dinosaur getDinosaur(ItemStack stack)
    {
        Dinosaur dinosaur = JCEntityRegistry.getDinosaurById(stack.getItemDamage());

        if (dinosaur == null)
        {
            dinosaur = JCEntityRegistry.achillobator;
        }

        return dinosaur;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subtypes)
    {
        List<Dinosaur> dinosaurs = new ArrayList<Dinosaur>(JCEntityRegistry.getDinosaurs());

        Map<Dinosaur, Integer> ids = new HashMap<Dinosaur, Integer>();

        for (Dinosaur dino : dinosaurs)
        {
            ids.put(dino, JCEntityRegistry.getDinosaurId(dino));
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
