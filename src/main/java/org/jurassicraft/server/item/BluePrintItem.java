package org.jurassicraft.server.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.jurassicraft.server.creativetab.JCCreativeTabs;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.JCEntityRegistry;
import org.jurassicraft.server.entity.item.BluePrintEntity;
import org.jurassicraft.server.lang.AdvLang;

public class BluePrintItem extends Item
{
    public BluePrintItem()
    {
        this.setCreativeTab(JCCreativeTabs.items);
        this.setMaxStackSize(1);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        int dinoId = getDinosaur(stack);
        Dinosaur dino = JCEntityRegistry.getDinosaurById(dinoId);
        String name = "blue_print.blank.name";

        if (dino != null)
        {
            name = "entity.jurassicraft." + dino.getName().toLowerCase().replaceAll(" ", "_") + ".name";
        }

        return new AdvLang("item.blue_print.name").withProperty("type", name).build();
    }

    public void setDinosaur(ItemStack stack, int dino)
    {
        NBTTagCompound nbt = stack.getTagCompound();

        if (nbt == null)
        {
            nbt = new NBTTagCompound();
        }

        nbt.setInteger("Dinosaur", dino);

        stack.setTagCompound(nbt);
    }

    public int getDinosaur(ItemStack stack)
    {
        NBTTagCompound nbt = stack.getTagCompound();

        if (nbt != null)
        {
            if (nbt.hasKey("Dinosaur"))
            {
                return nbt.getInteger("Dinosaur");
            }
        }

        return -1;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (side == EnumFacing.DOWN)
        {
            return false;
        }
        else if (side == EnumFacing.UP)
        {
            return false;
        }
        else
        {
            BlockPos blockpos1 = pos.offset(side);

            if (!playerIn.canPlayerEdit(blockpos1, side, stack))
            {
                return false;
            }
            else
            {
                int dinosaur = getDinosaur(stack);

                if (dinosaur != -1)
                {
                    BluePrintEntity bluePrint = new BluePrintEntity(worldIn, blockpos1, side, dinosaur);

                    if (bluePrint.onValidSurface())
                    {
                        if (!worldIn.isRemote)
                        {
                            worldIn.spawnEntityInWorld(bluePrint);
                        }

                        --stack.stackSize;

                        return true;
                    }
                }
            }
        }

        return false;
    }
}
