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

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     *
     * @param subItems The List of sub-items. This is a List of ItemStacks.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
    {
        subItems.add(new ItemStack(itemIn, 1, 0));
        subItems.add(new ItemStack(itemIn, 1, 1));
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     *
     * @param tooltip  All lines to display in the Item's tooltip. This is a List of Strings.
     * @param advanced Whether the setting "Advanced tooltips" is enabled
     */
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
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
    {
        pos = pos.offset(side);

        if (player.canPlayerEdit(pos, side, stack) && !world.isRemote)
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
