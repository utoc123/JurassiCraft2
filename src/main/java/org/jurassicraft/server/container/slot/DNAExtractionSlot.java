package org.jurassicraft.server.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.item.ItemHandler;

public class DNAExtractionSlot extends Slot
{
    public DNAExtractionSlot(IInventory inventory, int slotIndex, int xPosition, int yPosition)
    {
        super(inventory, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return stack.getItem() == ItemHandler.INSTANCE.AMBER || stack.getItem() == ItemHandler.INSTANCE.SEA_LAMPREY || stack.getItem() == ItemHandler.INSTANCE.DINOSAUR_MEAT;
    }
}
