package org.jurassicraft.server.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.jurassicraft.server.container.slot.CustomSlot;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.tileentity.IncubatorTile;

public class IncubatorContainer extends SyncedFieldContainer
{
    private IncubatorTile incubator;

    public IncubatorContainer(InventoryPlayer playerInventory, TileEntity tileEntity)
    {
        super((IInventory) tileEntity);

        this.incubator = (IncubatorTile) tileEntity;
        this.addSlotToContainer(new CustomSlot(incubator, 0, 33, 28, ItemHandler.INSTANCE.egg));
        this.addSlotToContainer(new CustomSlot(incubator, 1, 56, 21, ItemHandler.INSTANCE.egg));
        this.addSlotToContainer(new CustomSlot(incubator, 2, 79, 14, ItemHandler.INSTANCE.egg));
        this.addSlotToContainer(new CustomSlot(incubator, 3, 102, 21, ItemHandler.INSTANCE.egg));
        this.addSlotToContainer(new CustomSlot(incubator, 4, 125, 28, ItemHandler.INSTANCE.egg));

        this.addSlotToContainer(new Slot(incubator, 5, 79, 49));

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
            incubator.closeInventory(player);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return incubator.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int i)
    {
        return null;
    }
}
