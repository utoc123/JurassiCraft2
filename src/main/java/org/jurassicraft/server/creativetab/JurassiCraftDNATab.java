package org.jurassicraft.server.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.JCEntityRegistry;
import org.jurassicraft.server.item.JCItemRegistry;

public class JurassiCraftDNATab extends CreativeTabs
{
    private ItemStack[] stacks = null;

    public JurassiCraftDNATab(String label)
    {
        super(label);
    }

    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack()
    {
        if (stacks == null)
        {
            int dinosaurs = JCEntityRegistry.getRegisteredDinosaurs().size();
            this.stacks = new ItemStack[dinosaurs * 3];

            int id = 0;
            int i = 0;

            for (Dinosaur dino : JCEntityRegistry.getDinosaurs())
            {
                if (dino.shouldRegister())
                {
                    stacks[i] = new ItemStack(JCItemRegistry.dna, 1, id);
                    stacks[i + dinosaurs] = new ItemStack(JCItemRegistry.soft_tissue, 1, id);
                    stacks[i + (dinosaurs * 2)] = new ItemStack(JCItemRegistry.syringe, 1, id);

                    i++;
                }

                id++;
            }
        }

        return stacks[(int) ((JurassiCraft.timerTicks / 20) % stacks.length)];
    }

    @Override
    public Item getTabIconItem()
    {
        return JCItemRegistry.dna;
    }
}
