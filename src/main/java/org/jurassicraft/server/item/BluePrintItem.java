package org.jurassicraft.server.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.EntityHandler;
import org.jurassicraft.server.entity.item.BluePrintEntity;
import org.jurassicraft.server.lang.LangHelper;
import org.jurassicraft.server.tab.TabHandler;

public class BluePrintItem extends Item
{
    public BluePrintItem()
    {
        this.setCreativeTab(TabHandler.INSTANCE.ITEMS);
        this.setMaxStackSize(1);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        int dinoId = getDinosaur(stack);
        Dinosaur dino = EntityHandler.INSTANCE.getDinosaurById(dinoId);
        String name = "blue_print.blank.name";

        if (dino != null)
        {
            name = "entity.jurassicraft." + dino.getName().toLowerCase().replaceAll(" ", "_") + ".name";
        }

        return new LangHelper("item.blue_print.name").withProperty("type", name).build();
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
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (side != EnumFacing.DOWN && side != EnumFacing.UP)
        {
            BlockPos offset = pos.offset(side);

            if (player.canPlayerEdit(offset, side, stack))
            {
                int dinosaur = getDinosaur(stack);

                if (dinosaur != -1)
                {
                    BluePrintEntity bluePrint = new BluePrintEntity(worldIn, offset, side, dinosaur);

                    if (bluePrint.onValidSurface())
                    {
                        if (!worldIn.isRemote)
                        {
                            worldIn.spawnEntityInWorld(bluePrint);
                        }

                        --stack.stackSize;

                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }

        return EnumActionResult.PASS;
    }
}
