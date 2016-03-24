package org.jurassicraft.server.item;

import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
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
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.JCEntityRegistry;
import org.jurassicraft.server.entity.egg.DinosaurEggEntity;
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
        if (world.isRemote)
        {
            return EnumActionResult.SUCCESS;
        }
        else if (!player.canPlayerEdit(pos.offset(side), side, stack))
        {
            return EnumActionResult.PASS;
        }
        else
        {
            IBlockState state = world.getBlockState(pos);

            pos = pos.offset(side);
            double yOffset = 0.0D;

            if (side == EnumFacing.UP && state.getBlock() instanceof BlockFence)
            {
                yOffset = 0.5D;
            }

            DinosaurEggEntity egg = spawnEgg(world, player, stack, (double) pos.getX() + 0.5D, (double) pos.getY() + yOffset, (double) pos.getZ() + 0.5D);

            if (egg != null)
            {
                if (!player.capabilities.isCreativeMode)
                {
                    --stack.stackSize;
                }

                world.spawnEntityInWorld(egg);
            }

            return EnumActionResult.SUCCESS;
        }
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
                DinosaurEggEntity egg = new DinosaurEggEntity(world, dinosaur.getDinosaur().isMarineAnimal(), dinosaur);
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
