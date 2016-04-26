package org.jurassicraft.server.item.bones;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.api.IGrindableItem;
import org.jurassicraft.server.api.IHybrid;
import org.jurassicraft.server.creativetab.TabHandler;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.EntityHandler;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.lang.AdvLang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FossilItem extends Item implements IGrindableItem
{
    private String type;
    private boolean includeHybrids;

    public static Map<String, List<Dinosaur>> fossilDinosaurs = new HashMap<String, List<Dinosaur>>();

    public FossilItem(String type, boolean includeHybrids)
    {
        this.type = type.toLowerCase().replaceAll(" ", "_");
        this.includeHybrids = includeHybrids;

        this.setHasSubtypes(true);

        this.setCreativeTab(TabHandler.INSTANCE.bones);
    }

    public static void init()
    {
        for (Dinosaur dinosaur : EntityHandler.INSTANCE.getDinosaurs())
        {
            String[] boneTypes = dinosaur.getBones();

            for (String boneType : boneTypes)
            {
                List<Dinosaur> dinosaursWithType = fossilDinosaurs.get(boneType);

                if (dinosaursWithType == null)
                {
                    dinosaursWithType = new ArrayList<>();
                }

                dinosaursWithType.add(dinosaur);

                fossilDinosaurs.put(boneType, dinosaursWithType);
            }
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        Dinosaur dinosaur = this.getDinosaur(stack);

        if (dinosaur != null)
        {
            return new AdvLang(getUnlocalizedName() + ".name").withProperty("dino", "entity.jurassicraft." + dinosaur.getName().replace(" ", "_").toLowerCase() + ".name").build();
        }

        return super.getItemStackDisplayName(stack);
    }

    public Dinosaur getDinosaur(ItemStack stack)
    {
        return EntityHandler.INSTANCE.getDinosaurById(stack.getItemDamage());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subtypes)
    {
        List<Dinosaur> dinosaurs = new ArrayList<>(EntityHandler.INSTANCE.getRegisteredDinosaurs());

        Map<Dinosaur, Integer> ids = new HashMap<>();

        for (Dinosaur dino : dinosaurs)
        {
            ids.put(dino, EntityHandler.INSTANCE.getDinosaurId(dino));
        }

        Collections.sort(dinosaurs);

        List<Dinosaur> dinosaursForType = fossilDinosaurs.get(type);

        for (Dinosaur dino : dinosaurs)
        {
            if (dino.shouldRegister() && dinosaursForType.contains(dino) && !(!includeHybrids && dino instanceof IHybrid))
            {
                subtypes.add(new ItemStack(item, 1, ids.get(dino)));
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> lore, boolean advanced)
    {
        NBTTagCompound nbt = stack.getTagCompound();

        if (nbt != null && nbt.hasKey("Genetics") && nbt.hasKey("DNAQuality"))
        {
            int quality = nbt.getInteger("DNAQuality");

            TextFormatting colour;

            if (quality > 75)
            {
                colour = TextFormatting.GREEN;
            }
            else if (quality > 50)
            {
                colour = TextFormatting.YELLOW;
            }
            else if (quality > 25)
            {
                colour = TextFormatting.GOLD;
            }
            else
            {
                colour = TextFormatting.RED;
            }

            lore.add(colour + new AdvLang("lore.dna_quality.name").withProperty("quality", quality + "").build());
            lore.add(TextFormatting.BLUE + new AdvLang("lore.genetic_code.name").withProperty("code", nbt.getString("Genetics")).build());
        }
    }

    @Override
    public boolean isGrindable(ItemStack stack)
    {
        return true;
    }

    @Override
    public ItemStack getGroundItem(ItemStack stack, Random random)
    {
        NBTTagCompound tag = stack.getTagCompound();

        int outputType = random.nextInt(6);

        if (outputType == 5 || stack.getUnlocalizedName().contains("fresh"))
        {
            ItemStack output = new ItemStack(ItemHandler.INSTANCE.soft_tissue, 1, stack.getItemDamage());
            output.setTagCompound(tag);
            return output;
        }
        else if (outputType < 3)
        {
            return new ItemStack(Items.dye, 1, 15);
        }

        return new ItemStack(Items.flint);
    }
}
