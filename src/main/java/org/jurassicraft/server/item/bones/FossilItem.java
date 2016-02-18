package org.jurassicraft.server.item.bones;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.api.IHybrid;
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
    private boolean includeHybrids;

    public static Map<String, List<Dinosaur>> fossilDinosaurs = new HashMap<String, List<Dinosaur>>();

    public FossilItem(String type, boolean includeHybrids)
    {
        this.type = type.toLowerCase().replaceAll(" ", "_");
        this.includeHybrids = includeHybrids;

        this.setHasSubtypes(true);

        this.setCreativeTab(JCCreativeTabs.bones);
    }

    public static void init()
    {
        for (Dinosaur dinosaur : JCEntityRegistry.getRegisteredDinosaurs())
        {
            String[] boneTypes = dinosaur.getBones();

            for (String boneType : boneTypes)
            {
                List<Dinosaur> dinosaursWithType = fossilDinosaurs.get(boneType);

                if (dinosaursWithType == null)
                {
                    dinosaursWithType = new ArrayList<Dinosaur>();
                }

                dinosaursWithType.add(dinosaur);

                fossilDinosaurs.put(boneType, dinosaursWithType);
            }
        }
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

        List<Dinosaur> dinosaursForType = fossilDinosaurs.get(type);

        for (Dinosaur dino : dinosaurs)
        {
            if (dino.shouldRegister() && dinosaursForType.contains(dino) && !(!includeHybrids && dino instanceof IHybrid))
            {
                subtypes.add(new ItemStack(item, 1, ids.get(dino)));
            }
        }
    }
}
