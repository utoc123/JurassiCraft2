package org.jurassicraft.server.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.EntityHandler;
import org.jurassicraft.server.item.ItemHandler;

public class JurassiCraftDNATab extends CreativeTabs
{
    private ItemStack[] stacks = null;

    public JurassiCraftDNATab(String label)
    {
        super(label);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack()
    {
        if (stacks == null)
        {
            int dinosaurs = EntityHandler.INSTANCE.getRegisteredDinosaurs().size();
            this.stacks = new ItemStack[dinosaurs * 3];

            int i = 0;

            for (Dinosaur dino : EntityHandler.INSTANCE.getDinosaurs())
            {
                if (dino.shouldRegister())
                {
                    int id = EntityHandler.INSTANCE.getDinosaurId(dino);

                    stacks[i] = new ItemStack(ItemHandler.INSTANCE.DNA, 1, id);
                    stacks[i + dinosaurs] = new ItemStack(ItemHandler.INSTANCE.SOFT_TISSUE, 1, id);
                    stacks[i + (dinosaurs * 2)] = new ItemStack(ItemHandler.INSTANCE.SYRINGE, 1, id);

                    i++;
                }
            }
        }

        return stacks[(int) ((JurassiCraft.timerTicks / 20) % stacks.length)];
    }

    @Override
    public Item getTabIconItem()
    {
        return ItemHandler.INSTANCE.DNA;
    }
}
