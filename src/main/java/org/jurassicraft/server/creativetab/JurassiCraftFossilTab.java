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

import java.util.ArrayList;
import java.util.List;

public class JurassiCraftFossilTab extends CreativeTabs
{
    private int[] metas;

    public JurassiCraftFossilTab(String label)
    {
        super(label);

        List<Dinosaur> fossilDinosaurs = getFossilDinosaurs();
        this.metas = new int[fossilDinosaurs.size()];

        int i = 0;

        for (Dinosaur dino : fossilDinosaurs)
        {
            metas[i] = JCEntityRegistry.getDinosaurId(dino);

            i++;
        }
    }

    public List<Dinosaur> getFossilDinosaurs()
    {
        List<Dinosaur> fossilDinosaurs = new ArrayList<Dinosaur>();

        for (Dinosaur dino : JCItemRegistry.fossilDinosaurs.get("skull"))
        {
            if (dino.shouldRegister())
            {
                fossilDinosaurs.add(dino);
            }
        }

        return fossilDinosaurs;
    }

    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack()
    {
        return new ItemStack(getTabIconItem(), 1, metas[((int) ((JurassiCraft.timerTicks / 20) % metas.length))]);
    }

    @Override
    public Item getTabIconItem()
    {
        return JCItemRegistry.fossils.get("skull");
    }
}
