package org.jurassicraft.server.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.jurassicraft.server.container.slot.StorageSlot;
import org.jurassicraft.server.tileentity.DNACombinatorHybridizerTile;

public class DNACombinatorHybridizerContainer extends Container
{
    private DNACombinatorHybridizerTile dnaHybridizer;
    private InventoryPlayer playerInventory;

    public DNACombinatorHybridizerContainer(InventoryPlayer playerInventory, TileEntity tileEntity)
    {
        this.dnaHybridizer = (DNACombinatorHybridizerTile) tileEntity;
        this.playerInventory = playerInventory;
        this.updateSlots(dnaHybridizer.getMode());
    }

    public void updateSlots(boolean mode)
    {
        this.inventorySlots.clear();
        this.inventoryItemStacks.clear();

        if (mode)
        {
            this.addSlotToContainer(new StorageSlot(dnaHybridizer, 0, 10, 17, true));
            this.addSlotToContainer(new StorageSlot(dnaHybridizer, 1, 30, 17, true));
            this.addSlotToContainer(new StorageSlot(dnaHybridizer, 2, 50, 17, true));
            this.addSlotToContainer(new StorageSlot(dnaHybridizer, 3, 70, 17, true));
            this.addSlotToContainer(new StorageSlot(dnaHybridizer, 4, 90, 17, true));
            this.addSlotToContainer(new StorageSlot(dnaHybridizer, 5, 110, 17, true));
            this.addSlotToContainer(new StorageSlot(dnaHybridizer, 6, 130, 17, true));
            this.addSlotToContainer(new StorageSlot(dnaHybridizer, 7, 150, 17, true));
            this.addSlotToContainer(new StorageSlot(dnaHybridizer, 10, 80, 56, true));
        }
        else
        {
            this.addSlotToContainer(new StorageSlot(dnaHybridizer, 8, 55, 13, true));
            this.addSlotToContainer(new StorageSlot(dnaHybridizer, 9, 105, 13, true));
            this.addSlotToContainer(new StorageSlot(dnaHybridizer, 11, 81, 60, true));
        }

        int i;

        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        if (!player.worldObj.isRemote)
        {
            dnaHybridizer.closeInventory(player);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return dnaHybridizer.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int i)
    {
        return null;
    }
}
