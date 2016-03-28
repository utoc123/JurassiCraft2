package org.jurassicraft.server.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.container.slot.CleanableItemSlot;
import org.jurassicraft.server.container.slot.FossilSlot;
import org.jurassicraft.server.container.slot.WaterBucketSlot;
import org.jurassicraft.server.item.itemblock.EncasedFossilItemBlock;
import org.jurassicraft.server.tileentity.CleaningStationTile;

public class CleaningStationContainer extends SyncedFieldContainer
{
    private final IInventory tileCleaningStation;

    public CleaningStationContainer(InventoryPlayer invPlayer, IInventory cleaningStation)
    {
        super(cleaningStation);
        this.tileCleaningStation = cleaningStation;
        this.addSlotToContainer(new CleanableItemSlot(cleaningStation, 0, 56, 17));
        this.addSlotToContainer(new WaterBucketSlot(cleaningStation, 1, 56, 53));

        int i;

        for (i = 0; i < 3; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                this.addSlotToContainer(new FossilSlot(cleaningStation, i + (j * 3) + 2, i * 18 + 93 + 15, j * 18 + 26));
            }
        }

        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void onCraftGuiOpened(ICrafting listener)
    {
        super.onCraftGuiOpened(listener);
        listener.sendAllWindowProperties(this, this.tileCleaningStation);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileCleaningStation.isUseableByPlayer(playerIn);
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = null;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack transferFrom = slot.getStack();
            itemstack = transferFrom.copy();

            if (index == 2)
            {
                if (!this.mergeItemStack(transferFrom, 3, 39, true))
                {
                    return null;
                }

                slot.onSlotChange(transferFrom, itemstack);
            }
            else if (index != 1 && index != 0)
            {
                if (transferFrom.getItem() instanceof EncasedFossilItemBlock)
                {
                    if (!this.mergeItemStack(transferFrom, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (CleaningStationTile.isItemFuel(transferFrom))
                {
                    if (!this.mergeItemStack(transferFrom, 1, 2, false))
                    {
                        return null;
                    }
                }
                else if (index >= 3 && index < 30)
                {
                    if (!this.mergeItemStack(transferFrom, 30, 39, false))
                    {
                        return null;
                    }
                }
                else if (index >= 30 && index < 39 && !this.mergeItemStack(transferFrom, 3, 30, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(transferFrom, 3, 39, false))
            {
                return null;
            }

            if (transferFrom.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (transferFrom.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(playerIn, transferFrom);
        }

        return itemstack;
    }
}
