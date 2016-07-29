package org.jurassicraft.server.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.item.ItemHandler;

import java.util.List;

public class JurassiCraftFoodTab extends CreativeTabs {
    private int[] metas;

    public JurassiCraftFoodTab(String label) {
        super(label);

        List<Dinosaur> registeredDinosaurs = EntityHandler.getRegisteredDinosaurs();
        this.metas = new int[registeredDinosaurs.size()];

        int i = 0;

        for (Dinosaur dino : registeredDinosaurs) {
            if (dino.shouldRegister()) {
                this.metas[i] = EntityHandler.getDinosaurId(dino);

                i++;
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack() {
        return new ItemStack(this.getTabIconItem(), 1, this.metas[((int) ((JurassiCraft.timerTicks / 20) % this.metas.length))]);
    }

    @Override
    public Item getTabIconItem() {
        return ItemHandler.DINOSAUR_MEAT;
    }
}
