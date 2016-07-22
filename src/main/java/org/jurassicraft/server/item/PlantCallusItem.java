package org.jurassicraft.server.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.server.block.plant.JCBlockCropsBase;
import org.jurassicraft.server.lang.LangHelper;
import org.jurassicraft.server.plant.Plant;
import org.jurassicraft.server.plant.PlantHandler;

import java.util.Locale;

public class PlantCallusItem extends Item {
    public PlantCallusItem() {
        super();

        this.setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return new LangHelper("item.plant_callus.name").withProperty("plant", "plants." + PlantHandler.getPlantById(stack.getItemDamage()).getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_") + ".name").build();
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (side == EnumFacing.UP && player.canPlayerEdit(pos.offset(side), side, stack)) {
            if (world.isAirBlock(pos.offset(side)) && world.getBlockState(pos).getBlock() == Blocks.FARMLAND) {
                Plant plant = PlantHandler.getPlantById(stack.getItemDamage());

                if (plant != null) {
                    Block block = plant.getBlock();
                    world.setBlockState(pos.up(), block.getDefaultState());

                    if (!(block instanceof BlockCrops || block instanceof JCBlockCropsBase)) {
                        world.setBlockState(pos, Blocks.DIRT.getDefaultState());
                    }

                    --stack.stackSize;
                    return EnumActionResult.SUCCESS;
                }
            }
        }

        return EnumActionResult.PASS;
    }
}
