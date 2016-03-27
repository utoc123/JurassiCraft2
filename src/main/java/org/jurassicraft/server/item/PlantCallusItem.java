package org.jurassicraft.server.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.jurassicraft.server.lang.AdvLang;
import org.jurassicraft.server.plant.Plant;
import org.jurassicraft.server.plant.PlantHandler;

public class PlantCallusItem extends Item
{
    public PlantCallusItem()
    {
        super();
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        return new AdvLang("item.plant_callus.name").withProperty("plant", "plants." + PlantHandler.INSTANCE.getPlantById(stack.getItemDamage()).getName().toLowerCase().replaceAll(" ", "_") + ".name").build();
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (side != EnumFacing.UP)
        {
            return false;
        }
        else if (!player.canPlayerEdit(pos.offset(side), side, stack))
        {
            return false;
        }
        else if (world.isAirBlock(pos.up()) && world.getBlockState(pos).getBlock() == Blocks.farmland)
        {
            Plant plant = PlantHandler.INSTANCE.getPlantById(stack.getItemDamage());

            if (plant != null)
            {
                world.setBlockState(pos.up(), plant.getBlock().getDefaultState());
                world.setBlockState(pos, Blocks.dirt.getDefaultState());
                --stack.stackSize;
                return true;
            }
        }

        return false;
    }
}
