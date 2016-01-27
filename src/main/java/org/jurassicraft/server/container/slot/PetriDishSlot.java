package org.jurassicraft.server.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.item.JCItemRegistry;

public class PetriDishSlot extends Slot
{
    public PetriDishSlot(IInventory inventory, int slotIndex, int xPosition, int yPosition)
    {
        super(inventory, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return stack.getItem() == JCItemRegistry.plant_cells_petri_dish || stack.getItem() == JCItemRegistry.petri_dish;
    }
}
