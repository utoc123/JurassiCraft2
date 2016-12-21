package org.jurassicraft.server.tab;

import java.util.List;

import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.item.ItemHandler;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class JurassiCraftDecorationsTab extends CreativeTabs {
    private int[] metas;

    public JurassiCraftDecorationsTab(String label) {
        super(label);

        List<Dinosaur> registeredDinosaurs = EntityHandler.getRegisteredDinosaurs();
        this.metas = new int[registeredDinosaurs.size()];

        int i = 0;

        for (Dinosaur dino : registeredDinosaurs) {
            this.metas[i] = EntityHandler.getDinosaurId(dino);

            i++;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack() {
        return new ItemStack(ItemHandler.ACTION_FIGURE);
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ItemHandler.ACTION_FIGURE);
    }
}
