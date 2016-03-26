package org.jurassicraft.server.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.creativetab.JCCreativeTabs;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.JCEntityRegistry;
import org.jurassicraft.server.entity.item.DinosaurEggEntity;
import org.jurassicraft.server.lang.AdvLang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemDinosaurEgg extends DNAContainerItem
{
    public ItemDinosaurEgg()
    {
        super();

        this.setCreativeTab(JCCreativeTabs.eggs);
        this.setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        String dinoName = getDinosaur(stack).getName().toLowerCase().replaceAll(" ", "_");

        return new AdvLang("item.dino_egg.name").withProperty("dino", "entity.jurassicraft." + dinoName + ".name").build();
    }

    public Dinosaur getDinosaur(ItemStack stack)
    {
        return JCEntityRegistry.getDinosaurById(stack.getMetadata());
    }

    @Override
    public int getContainerId(ItemStack stack)
    {
        return JCEntityRegistry.getDinosaurId(getDinosaur(stack));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subtypes)
    {
        List<Dinosaur> dinosaurs = new ArrayList<Dinosaur>(JCEntityRegistry.getDinosaurs());

        Map<Dinosaur, Integer> ids = new HashMap<Dinosaur, Integer>();

        for (Dinosaur dino : dinosaurs)
        {
            ids.put(dino, JCEntityRegistry.getDinosaurId(dino));
        }

        Collections.sort(dinosaurs);

        for (Dinosaur dino : dinosaurs)
        {
            if (dino.shouldRegister() && !dino.isMammal())
            {
                subtypes.add(new ItemStack(item, 1, ids.get(dino)));
            }
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        pos = pos.offset(side);

        if (player.canPlayerEdit(pos, side, stack) && !world.isRemote)
        {
            DinosaurEggEntity egg = spawnEgg(world, player, stack, pos.getX(), pos.getY(), pos.getZ());
            egg.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            world.spawnEntityInWorld(egg);

            if (!player.capabilities.isCreativeMode)
            {
                stack.stackSize--;
            }

            return true;
        }

        return false;
    }

    public DinosaurEggEntity spawnEgg(World world, EntityPlayer player, ItemStack stack, double x, double y, double z)
    {
        Dinosaur dinoInEgg = getDinosaur(stack);

        if (dinoInEgg != null)
        {
            Class<? extends DinosaurEntity> dinoClass = dinoInEgg.getDinosaurClass();

            try
            {
                DinosaurEntity dinosaur = dinoClass.getConstructor(World.class).newInstance(player.worldObj);
                DinosaurEggEntity egg = new DinosaurEggEntity(world, dinosaur);
                egg.setPosition(x, y, z);
                return egg;
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }
}