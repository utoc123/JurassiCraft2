package org.jurassicraft.server.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.jurassicraft.server.container.slot.DNAExtractionSlot;
import org.jurassicraft.server.container.slot.StorageSlot;
import org.jurassicraft.server.tileentity.DNAExtractorTile;

public class DNAExtractorContainer extends Container
{
    private DNAExtractorTile extractor;

    public DNAExtractorContainer(InventoryPlayer playerInventory, TileEntity tileEntity)
    {
        this.extractor = (DNAExtractorTile) tileEntity;
        this.addSlotToContainer(new StorageSlot(extractor, 1, 55, 47, false));
        this.addSlotToContainer(new DNAExtractionSlot(extractor, 0, 55, 26));
        this.addSlotToContainer(new StorageSlot(extractor, 2, 108, 28, true));
        this.addSlotToContainer(new StorageSlot(extractor, 3, 126, 28, true));
        this.addSlotToContainer(new StorageSlot(extractor, 4, 108, 46, true));
        this.addSlotToContainer(new StorageSlot(extractor, 5, 126, 46, true));

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
            extractor.closeInventory(player);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return extractor.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int i)
    {
        return null;
    }
}
