package org.jurassicraft.server.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.creativetab.TabHandler;
import org.jurassicraft.server.entity.item.CageSmallEntity;

import java.util.List;

public class CageItem extends Item
{
    public CageItem()
    {
        super();
        this.setHasSubtypes(true);
        this.setCreativeTab(TabHandler.INSTANCE.items);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
    {
        subItems.add(new ItemStack(itemIn, 1, 0));
        subItems.add(new ItemStack(itemIn, 1, 1));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        int caged = getCaged(stack);
        NBTTagCompound data = getData(stack);

        if (caged != -1)
        {
            tooltip.add(TextFormatting.BLUE + I18n.translateToLocal("entity.jurassicraft." + EntityList.classToStringMapping.get(EntityList.idToClassMapping.get(caged)) + ".name"));

            if (data != null)
            {
                tooltip.add(TextFormatting.RED + I18n.translateToLocal("gender." + (data.getBoolean("IsMale") ? "male" : "female") + ".name"));
            }
        }
        else
        {
            tooltip.add(TextFormatting.RED + I18n.translateToLocal("cage.empty.name"));
        }
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        pos = pos.offset(side);

        if (!world.isRemote && player.canPlayerEdit(pos, side, stack))
        {
            CageSmallEntity cage = new CageSmallEntity(world, stack.getMetadata() == 1);
            cage.setEntity(getCaged(stack));
            cage.setEntityData(getData(stack));
            cage.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);

            world.spawnEntityInWorld(cage);

            if (!player.capabilities.isCreativeMode)
            {
                stack.stackSize--;
            }

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    private int getCaged(ItemStack stack)
    {
        if (stack.getTagCompound() != null)
        {
            return stack.getTagCompound().getInteger("CagedID");
        }

        return -1;
    }

    private NBTTagCompound getData(ItemStack stack)
    {
        if (stack.getTagCompound() != null)
        {
            return stack.getTagCompound().getCompoundTag("Entity");
        }

        return null;
    }
}
