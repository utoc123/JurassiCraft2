package org.jurassicraft.server.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.jurassicraft.server.container.slot.CustomSlot;
import org.jurassicraft.server.container.slot.PetriDishSlot;
import org.jurassicraft.server.container.slot.TestTubeSlot;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.tileentity.EmbryonicMachineTile;

public class EmbryonicMachineContainer extends Container
{
    private EmbryonicMachineTile embryonicMachine;

    public EmbryonicMachineContainer(InventoryPlayer playerInventory, TileEntity tileEntity)
    {
        this.embryonicMachine = (EmbryonicMachineTile) tileEntity;
        this.addSlotToContainer(new TestTubeSlot(embryonicMachine, 0, 24, 49));
        this.addSlotToContainer(new PetriDishSlot(embryonicMachine, 1, 50, 49));
        this.addSlotToContainer(new CustomSlot(embryonicMachine, 2, 50, 13, ItemHandler.INSTANCE.empty_syringe));

        int i;

        for (i = 0; i < 2; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                this.addSlotToContainer(new Slot(embryonicMachine, i + (j * 2) + 3, i * 18 + 119, j * 18 + 26));
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
            embryonicMachine.closeInventory(player);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return embryonicMachine.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int i)
    {
        return null;
    }
}
