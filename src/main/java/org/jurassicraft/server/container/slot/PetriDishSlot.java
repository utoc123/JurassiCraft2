package org.jurassicraft.server.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.item.ItemHandler;

public class PetriDishSlot extends Slot
{
    public PetriDishSlot(IInventory inventory, int slotIndex, int xPosition, int yPosition)
    {
        super(inventory, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return stack.getItem() == ItemHandler.INSTANCE.plant_cells_petri_dish || stack.getItem() == ItemHandler.INSTANCE.petri_dish;
    }
}
