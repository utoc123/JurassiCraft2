package org.jurassicraft.server.item;

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

public final class FordExplorerItem extends Item {
    public FordExplorerItem() {
        this.setCreativeTab(TabHandler.ITEMS);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof TourRailBlock) {
            if (!world.isRemote) {
                FordExplorerEntity.OrientatedRail orientatedRail = FordExplorerEntity.OrientatedRail.get(state);
                FordExplorerEntity entity = new FordExplorerEntity(world);
                entity.setHead(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5);
                entity.rotationYaw = orientatedRail.getOutputVector().getHorizontalAngle();
                entity.pull();
                world.spawnEntity(entity);
                stack.stackSize--;
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }
}
