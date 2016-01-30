package org.jurassicraft.server.item.bones;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.creativetab.JCCreativeTabs;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.JCEntityRegistry;
import org.jurassicraft.server.item.JCItemRegistry;
import org.jurassicraft.server.lang.AdvLang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FossilItem extends Item
{
    private String type;
    private String fossilType;

    public FossilItem(String type, String fossilType)
    {
        this.type = type.toLowerCase().replaceAll(" ", "_");
        this.fossilType = fossilType.toLowerCase().replaceAll(" ", "_");

        this.setUnlocalizedName("fossil_" + this.type);
        this.setHasSubtypes(true);

        this.setCreativeTab(JCCreativeTabs.bones);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        Dinosaur dinosaur = this.getDinosaur(stack);

        if (dinosaur != null)
        {
            return new AdvLang("item." + type + ".name").withProperty("dino", "entity.jurassicraft." + dinosaur.getName().replace(" ", "_").toLowerCase() + ".name").build();
        }

        return super.getItemStackDisplayName(stack);
    }

    public Dinosaur getDinosaur(ItemStack stack)
    {
        return JCEntityRegistry.getDinosaurById(stack.getItemDamage());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subtypes)
    {
        List<Dinosaur> dinosaurs = new ArrayList<Dinosaur>(JCEntityRegistry.getRegisteredDinosaurs());

        Map<Dinosaur, Integer> ids = new HashMap<Dinosaur, Integer>();

        for (Dinosaur dino : dinosaurs)
        {
            ids.put(dino, JCEntityRegistry.getDinosaurId(dino));
        }

        Collections.sort(dinosaurs);

        List<Dinosaur> dinosaursForType = JCItemRegistry.fossilDinosaurs.get(fossilType);

        for (Dinosaur dino : dinosaurs)
        {
            if (dino.shouldRegister() && dinosaursForType.contains(dino))
            {
                subtypes.add(new ItemStack(item, 1, ids.get(dino)));
            }
        }
    }
}
