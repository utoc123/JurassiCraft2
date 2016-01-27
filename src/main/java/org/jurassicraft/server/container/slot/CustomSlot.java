package org.jurassicraft.server.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CustomSlot extends Slot
{
    private Item item;

    public CustomSlot(IInventory inventory, int slotIndex, int xPosition, int yPosition, Item item)
    {
        super(inventory, slotIndex, xPosition, yPosition);
        this.item = item;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return stack.getItem() == item;
    }
}
