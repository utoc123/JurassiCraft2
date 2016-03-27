package org.jurassicraft.server.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.api.ISequencableItem;

public class SequencableItemSlot extends Slot
{
    public SequencableItemSlot(IInventory inventory, int slotIndex, int xPosition, int yPosition)
    {
        super(inventory, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        ISequencableItem sequencableItem = ISequencableItem.getSequencableItem(stack);
        return sequencableItem != null && sequencableItem.isSequencable(stack);
    }
}
