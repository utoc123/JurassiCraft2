package org.jurassicraft.server.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;
import org.jurassicraft.server.container.slot.CustomSlot;
import org.jurassicraft.server.container.slot.GrindableItemSlot;
import org.jurassicraft.server.tile.FossilGrinderTile;

public class FossilGrinderContainer extends MachineContainer
{
    private FossilGrinderTile fossilGrinder;

    public FossilGrinderContainer(InventoryPlayer playerInventory, TileEntity tileEntity)
    {
        super((IInventory) tileEntity);

        this.fossilGrinder = (FossilGrinderTile) tileEntity;
        this.addSlotToContainer(new GrindableItemSlot(fossilGrinder, 0, 50, 35));

        int i;

        for (i = 0; i < 3; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                this.addSlotToContainer(new CustomSlot(fossilGrinder, i + (j * 3) + 1, i * 18 + 93 + 15, j * 18 + 26, stack -> false));
            }
        }

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
            fossilGrinder.closeInventory(player);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return fossilGrinder.isUseableByPlayer(player);
    }
}
