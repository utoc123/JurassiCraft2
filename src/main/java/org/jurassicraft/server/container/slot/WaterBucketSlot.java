package org.jurassicraft.server.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.tile.CleaningStationTile;

public class WaterBucketSlot extends Slot {
    public WaterBucketSlot(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    @Override
    public boolean isItemValid(ItemStack stack) {
        return CleaningStationTile.isItemFuel(stack);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }
}
