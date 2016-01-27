package org.jurassicraft.server.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.jurassicraft.server.container.slot.SyringeSlot;
import org.jurassicraft.server.container.slot.WaterBucketSlot;
import org.jurassicraft.server.tileentity.CultivatorTile;

public class CultivateContainer extends Container
{
    private CultivatorTile cultivator;

    public CultivateContainer(InventoryPlayer playerInventory, TileEntity tileEntity)
    {
        this.cultivator = (CultivatorTile) tileEntity;
        this.addSlotToContainer(new SyringeSlot(cultivator, 0, 122, 44));
        this.addSlotToContainer(new Slot(cultivator, 1, 208, 20));
        this.addSlotToContainer(new WaterBucketSlot(cultivator, 2, 12, 20));
        this.addSlotToContainer(new Slot(cultivator, 3, 12, 68));

        int i;

        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 106 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 164));
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        if (!player.worldObj.isRemote)
        {
            cultivator.closeInventory(player);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return cultivator.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int i)
    {
        return null;
    }
}
