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

        for (int row = 0; row < 3; row++)
        {
            for (int column = 0; column < 2; column++)
            {
                this.addSlotToContainer(new GrindableItemSlot(fossilGrinder, row + (column * 3), row * 18 + 23, column * 18 + 26));
            }
        }

        for (int row = 0; row < 3; row++)
        {
            for (int column = 0; column < 2; column++)
            {
                this.addSlotToContainer(new CustomSlot(fossilGrinder, row + (column * 3) + 6, row * 18 + 93 + 15, column * 18 + 26, stack -> false));
            }
        }

        for (int row = 0; row < 3; ++row)
        {
            for (int column = 0; column < 9; ++column)
            {
                this.addSlotToContainer(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }

        for (int column = 0; column < 9; ++column)
        {
            this.addSlotToContainer(new Slot(playerInventory, column, 8 + column * 18, 142));
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
