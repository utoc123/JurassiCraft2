package org.jurassicraft.server.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.creativetab.JCCreativeTabs;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.JCEntityRegistry;
import org.jurassicraft.server.entity.item.DinosaurEggEntity;
import org.jurassicraft.server.lang.AdvLang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DinosaurEggItem extends DNAContainerItem
{
    public DinosaurEggItem()
    {
        super();

        this.setCreativeTab(JCCreativeTabs.eggs);
        this.setHasSubtypes(true);
    }

    public String getItemStackDisplayName(ItemStack stack)
    {
        String dinoName = getDinosaur(stack).getName().toLowerCase().replaceAll(" ", "_");

        return new AdvLang("item.dino_egg.name").withProperty("dino", "entity.jurassicraft." + dinoName + ".name").build();
    }

    public Dinosaur getDinosaur(ItemStack stack)
    {
        return JCEntityRegistry.getDinosaurById(stack.getMetadata());
    }

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
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        pos = pos.offset(side);

        if (side == EnumFacing.EAST || side == EnumFacing.WEST)
        {
            hitX = 1.0F - hitX;
        }
        else if (side == EnumFacing.NORTH || side == EnumFacing.SOUTH)
        {
            hitZ = 1.0F - hitZ;
        }

        if (player.canPlayerEdit(pos, side, stack) && !world.isRemote)
        {
            DinosaurEggEntity egg = new DinosaurEggEntity(world, getDinosaur(stack), getDNAQuality(player, stack), getGeneticCode(player, stack).toString());
            egg.setPosition(pos.getX() + hitX, pos.getY(), pos.getZ() + hitZ);
            egg.rotationYaw = player.rotationYaw;
            world.spawnEntityInWorld(egg);

            if (!player.capabilities.isCreativeMode)
            {
                stack.stackSize--;
            }

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }
}
