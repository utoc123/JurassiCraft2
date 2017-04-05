package org.jurassicraft.server.item;

import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.server.block.TourRailBlock;
import org.jurassicraft.server.entity.vehicle.FordExplorerEntity;
import org.jurassicraft.server.tab.TabHandler;

public class FordExplorerItem extends Item {
    public FordExplorerItem() {
        this.setCreativeTab(TabHandler.ITEMS);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof TourRailBlock) {
            BlockRailBase.EnumRailDirection direction = state.getValue(TourRailBlock.SHAPE);
            if (!direction.isAscending()) {
                if (!world.isRemote) {
                    pos = pos.offset(side);

                    FordExplorerEntity entity = new FordExplorerEntity(world);
                    entity.setPositionAndRotation(pos.getX() + 0.5F, pos.getY() - 1.0F, pos.getZ() + 0.5F, 0.0F, 0.0F);
                    world.spawnEntity(entity);

                    stack.stackSize--;
                }

                return EnumActionResult.SUCCESS;
            }
        }

        return EnumActionResult.PASS;
    }
}
