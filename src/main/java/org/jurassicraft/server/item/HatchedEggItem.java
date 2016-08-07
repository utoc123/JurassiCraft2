package org.jurassicraft.server.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.util.LangHelper;

import java.util.Locale;

public class HatchedEggItem extends DNAContainerItem {
    public HatchedEggItem() {
        super();
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String dinoName = this.getDinosaur(stack).getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

        return new LangHelper("item.hatched_egg.name").withProperty("dino", "entity.jurassicraft." + dinoName + ".name").build();
    }

    public Dinosaur getDinosaur(ItemStack stack) {
        return EntityHandler.getDinosaurById(stack.getMetadata());
    }

    public boolean getGender(EntityPlayer player, ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound();

        boolean gender = player.worldObj.rand.nextBoolean();

        if (nbt == null) {
            nbt = new NBTTagCompound();
        }

        if (nbt.hasKey("Gender")) {
            gender = nbt.getBoolean("Gender");
        } else {
            nbt.setBoolean("Gender", gender);
        }

        stack.setTagCompound(nbt);

        return gender;
    }

    @Override
    public int getContainerId(ItemStack stack) {
        return EntityHandler.getDinosaurId(this.getDinosaur(stack));
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        pos = pos.offset(side);

        if (side == EnumFacing.EAST || side == EnumFacing.WEST) {
            hitX = 1.0F - hitX;
        } else if (side == EnumFacing.NORTH || side == EnumFacing.SOUTH) {
            hitZ = 1.0F - hitZ;
        }

        if (player.canPlayerEdit(pos, side, stack)) {
            if (!world.isRemote) {
                Dinosaur dinosaur = this.getDinosaur(stack);

                try {
                    DinosaurEntity entity = dinosaur.getDinosaurClass().getDeclaredConstructor(World.class).newInstance(world);

                    entity.setPosition(pos.getX() + hitX, pos.getY(), pos.getZ() + hitZ);
                    entity.setAge(0);
                    entity.setGenetics(this.getGeneticCode(player, stack));
                    entity.setDNAQuality(this.getDNAQuality(player, stack));
                    entity.setMale(this.getGender(player, stack));

                    if (!player.isSneaking()) {
                        entity.setOwner(player);
                    }

                    world.spawnEntityInWorld(entity);

                    if (!player.capabilities.isCreativeMode) {
                        stack.stackSize--;
                    }
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }
            }

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }
}
