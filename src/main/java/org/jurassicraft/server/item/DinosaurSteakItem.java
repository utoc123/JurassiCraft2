package org.jurassicraft.server.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.EntityHandler;
import org.jurassicraft.server.lang.LangHelper;
import org.jurassicraft.server.tab.TabHandler;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class DinosaurSteakItem extends ItemFood {
    public DinosaurSteakItem() {
        super(8, 0.8F, true);

        this.setHasSubtypes(true);

        this.setCreativeTab(TabHandler.FOODS);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Dinosaur dinosaur = this.getDinosaur(stack);

        return new LangHelper("item.dinosaur_steak.name").withProperty("dino", "entity.jurassicraft." + dinosaur.getName().replace(" ", "_").toLowerCase(Locale.ENGLISH) + ".name").build();
    }

    public Dinosaur getDinosaur(ItemStack stack) {
        Dinosaur dinosaur = EntityHandler.getDinosaurById(stack.getItemDamage());

        if (dinosaur == null) {
            dinosaur = EntityHandler.VELOCIRAPTOR;
        }

        return dinosaur;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subtypes) {
        List<Dinosaur> dinosaurs = new LinkedList<>(EntityHandler.getDinosaurs().values());

        Collections.sort(dinosaurs);

        for (Dinosaur dinosaur : dinosaurs) {
            if (dinosaur.shouldRegister()) {
                subtypes.add(new ItemStack(item, 1, EntityHandler.getDinosaurId(dinosaur)));
            }
        }
    }
}
