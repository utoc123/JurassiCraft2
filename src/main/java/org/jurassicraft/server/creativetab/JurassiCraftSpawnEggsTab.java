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

import java.util.List;

public class JurassiCraftSpawnEggsTab extends CreativeTabs
{
    private int[] metas;

    public JurassiCraftSpawnEggsTab(String label)
    {
        super(label);

        List<Dinosaur> registeredDinosaurs = JCEntityRegistry.getRegisteredDinosaurs();
        this.metas = new int[registeredDinosaurs.size()];

        int i = 0;

        for (Dinosaur dino : registeredDinosaurs)
        {
            metas[i] = JCEntityRegistry.getDinosaurId(dino);

            i++;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack()
    {
        return new ItemStack(getTabIconItem(), 1, metas[((int) ((JurassiCraft.timerTicks / 20) % metas.length))]);
    }

    @Override
    public Item getTabIconItem()
    {
        return JCItemRegistry.spawn_egg;
    }
}
