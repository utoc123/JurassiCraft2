package net.timeless.jurassicraft.common.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.timeless.jurassicraft.common.creativetab.JCCreativeTabs;
import net.timeless.jurassicraft.common.dinosaur.Dinosaur;
import net.timeless.jurassicraft.common.entity.base.JCEntityRegistry;
import net.timeless.jurassicraft.common.lang.AdvLang;

public class ItemDinosaurMeat extends ItemDnaContainer
{
    public ItemDinosaurMeat()
    {
        this.setUnlocalizedName("dinosaur_meat");
        this.setHasSubtypes(true);

        this.setCreativeTab(JCCreativeTabs.foods);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        Dinosaur dinosaur = this.getDinosaur(stack);

        return new AdvLang("item.dinosaur_meat.name").withProperty("dino", "entity." + dinosaur.getName().replace(" ", "_").toLowerCase() + ".name").build();
    }

    public Dinosaur getDinosaur(ItemStack stack)
    {
        Dinosaur dinosaur = JCEntityRegistry.getDinosaurById(stack.getItemDamage());

        if (dinosaur == null)
            dinosaur = JCEntityRegistry.achillobator;

        return dinosaur;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List subtypes)
    {
        int i = 0;

        for (Dinosaur dino : JCEntityRegistry.getDinosaurs())
        {
            if (dino.shouldRegister())
                subtypes.add(new ItemStack(item, 1, i));

            i++;
        }
    }
}
