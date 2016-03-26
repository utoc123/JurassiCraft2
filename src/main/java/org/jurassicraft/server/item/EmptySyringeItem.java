package org.jurassicraft.server.item;

import net.minecraft.block.BlockBush;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.jurassicraft.server.creativetab.TabHandler;

public class EmptySyringeItem extends Item
{
    public EmptySyringeItem()
    {
        super();
        this.setCreativeTab(TabHandler.INSTANCE.items);
    }

    /**
     * Called when a Block is right-clicked with this Item
     *
     * @param pos  The block being right-clicked
     * @param side The side being right-clicked
     */
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.getBlockState(pos).getBlock() instanceof BlockBush)
        {
            playerIn.inventory.addItemStackToInventory(new ItemStack(ItemHandler.INSTANCE.plant_cells));
            stack.stackSize--;

            return true;
        }

        return false;
    }
}
