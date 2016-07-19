package org.jurassicraft.server.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.EntityHandler;
import org.jurassicraft.server.lang.LangHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HatchedEggItem extends DNAContainerItem
{
    public HatchedEggItem()
    {
        super();
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        String dinoName = getDinosaur(stack).getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

        return new LangHelper("item.hatched_egg.name").withProperty("dino", "entity.jurassicraft." + dinoName + ".name").build();
    }

    public Dinosaur getDinosaur(ItemStack stack)
    {
        return EntityHandler.getDinosaurById(stack.getMetadata());
    }

    public boolean getGender(EntityPlayer player, ItemStack stack)
    {
        NBTTagCompound nbt = stack.getTagCompound();

        boolean gender = player.worldObj.rand.nextBoolean();

        if (nbt == null)
        {
            nbt = new NBTTagCompound();
        }

        if (nbt.hasKey("Gender"))
        {
            gender = nbt.getBoolean("Gender");
        }
        else
        {
            nbt.setBoolean("Gender", gender);
        }

        stack.setTagCompound(nbt);

        return gender;
    }

    @Override
    public int getContainerId(ItemStack stack)
    {
        return EntityHandler.getDinosaurId(getDinosaur(stack));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subtypes)
    {
        List<Dinosaur> dinosaurs = new ArrayList<>(EntityHandler.getDinosaurs());

        Map<Dinosaur, Integer> ids = new HashMap<>();

        for (Dinosaur dino : dinosaurs)
        {
            ids.put(dino, EntityHandler.getDinosaurId(dino));
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

        if (player.canPlayerEdit(pos, side, stack))
        {
            if (!world.isRemote)
            {
                Dinosaur dinosaur = getDinosaur(stack);

                try
                {
                    DinosaurEntity entity = dinosaur.getDinosaurClass().getDeclaredConstructor(World.class).newInstance(world);

                    entity.setPosition(pos.getX() + hitX, pos.getY(), pos.getZ() + hitZ);
                    entity.setAge(0);
                    entity.setGenetics(getGeneticCode(player, stack));
                    entity.setDNAQuality(getDNAQuality(player, stack));
                    entity.setMale(getGender(player, stack));

                    if (!player.isSneaking())
                    {
                        entity.setOwner(player);
                    }

                    world.spawnEntityInWorld(entity);

                    if (!player.capabilities.isCreativeMode)
                    {
                        stack.stackSize--;
                    }
                }
                catch (ReflectiveOperationException e)
                {
                    e.printStackTrace();
                }
            }

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }
}
