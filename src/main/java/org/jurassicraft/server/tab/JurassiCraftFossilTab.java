package org.jurassicraft.server.tab;

import java.util.ArrayList;
import java.util.List;

import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.item.bones.FossilItem;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class JurassiCraftFossilTab extends CreativeTabs {
    private int[] metas;

    public JurassiCraftFossilTab(String label) {
        super(label);

        List<Dinosaur> fossilDinosaurs = this.getFossilDinosaurs();
        this.metas = new int[fossilDinosaurs.size()];

        int i = 0;

        for (Dinosaur dino : fossilDinosaurs) {
            this.metas[i] = EntityHandler.getDinosaurId(dino);

            i++;
        }
    }

    public List<Dinosaur> getFossilDinosaurs() {
        List<Dinosaur> fossilDinosaurs = new ArrayList<>();

        for (Dinosaur dino : FossilItem.fossilDinosaurs.get("skull")) {
            if (dino.shouldRegister()) {
                fossilDinosaurs.add(dino);
            }
        }

        return fossilDinosaurs;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack() {
        return new ItemStack(ItemHandler.FOSSILS.get("skull"));
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ItemHandler.FOSSILS.get("skull"));
    }
}
