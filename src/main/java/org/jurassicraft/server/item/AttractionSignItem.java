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
import org.jurassicraft.server.creativetab.TabHandler;
import org.jurassicraft.server.entity.item.AttractionSignEntity;
import org.jurassicraft.server.lang.AdvLang;

import java.util.List;

public class AttractionSignItem extends Item
{
    public AttractionSignItem()
    {
        this.setCreativeTab(TabHandler.INSTANCE.items);
        this.setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        return new AdvLang("item.attraction_sign.name").withProperty("type", "attraction_sign." + (AttractionSignEntity.AttractionSignType.values()[stack.getItemDamage()].name().toLowerCase()) + ".name").build();
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
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
            BlockPos offset = pos.offset(side);

            if (!player.canPlayerEdit(offset, side, stack))
            {
                return false;
            }
            else
            {
                AttractionSignEntity sign = new AttractionSignEntity(world, offset, side, AttractionSignEntity.AttractionSignType.values()[stack.getItemDamage()]);

                if (sign.onValidSurface())
                {
                    if (!world.isRemote)
                    {
                        world.spawnEntityInWorld(sign);
                    }

                    stack.stackSize--;

                    return true;
                }
            }
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems)
    {
        for (AttractionSignEntity.AttractionSignType signType : AttractionSignEntity.AttractionSignType.values())
        {
            subItems.add(new ItemStack(item, 1, signType.ordinal()));
        }
    }
}
