package org.jurassicraft.server.item;

import net.minecraft.block.BlockBush;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.server.creativetab.JCCreativeTabs;

public class EmptySyringeItem extends Item
{
    public EmptySyringeItem()
    {
        super();
        this.setCreativeTab(JCCreativeTabs.items);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.getBlockState(pos).getBlock() instanceof BlockBush)
        {
            playerIn.inventory.addItemStackToInventory(new ItemStack(JCItemRegistry.plant_cells));
            stack.stackSize--;

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }
}
