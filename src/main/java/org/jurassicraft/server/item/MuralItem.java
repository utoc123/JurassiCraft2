package org.jurassicraft.server.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.item.MuralEntity;
import org.jurassicraft.server.tab.TabHandler;

public class MuralItem extends Item {
    public MuralItem() {
        this.setCreativeTab(TabHandler.DECORATIONS);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (side != EnumFacing.DOWN && side != EnumFacing.UP) {
            BlockPos offset = pos.offset(side);

            if (player.canPlayerEdit(offset, side, stack)) {
                MuralEntity mural = new MuralEntity(world, offset, side);

                if (mural.onValidSurface()) {
                    if (!world.isRemote) {
                        world.spawnEntityInWorld(mural);
                    }

                    stack.stackSize--;

                    return EnumActionResult.SUCCESS;
                }
            }
        }

        return EnumActionResult.PASS;
    }
}
