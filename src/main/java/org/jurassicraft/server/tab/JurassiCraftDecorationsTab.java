package org.jurassicraft.server.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.item.DisplayBlockItem;
import org.jurassicraft.server.item.ItemHandler;

import java.util.List;

public class JurassiCraftDecorationsTab extends CreativeTabs {
    private int[] metas;

    public JurassiCraftDecorationsTab(String label) {
        super(label);

        List<Dinosaur> registeredDinosaurs = EntityHandler.getRegisteredDinosaurs();
        this.metas = new int[registeredDinosaurs.size()];

        for (int i = 0; i < registeredDinosaurs.size(); i++) {
            this.metas[i] = DisplayBlockItem.getMetadata(EntityHandler.getDinosaurId(registeredDinosaurs.get(i)), 0, false);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack() {
        return new ItemStack(this.getTabIconItem(), 1, this.metas[((int) ((JurassiCraft.timerTicks / 20) % this.metas.length))]);
    }

    @Override
    public Item getTabIconItem() {
        return ItemHandler.DISPLAY_BLOCK;
    }
}
