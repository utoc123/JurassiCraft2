package org.jurassicraft.server.container;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class SyncedFieldContainer extends Container
{
    private int[] fields;
    private IInventory inventory;

    public SyncedFieldContainer(IInventory inventory)
    {
        this.inventory = inventory;
        this.fields = new int[inventory.getFieldCount()];
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (ICrafting crafter : this.crafters)
        {
            for (int fieldIndex = 0; fieldIndex < fields.length; fieldIndex++)
            {
                int field = inventory.getField(fieldIndex);

                if (field != fields[fieldIndex])
                {
                    crafter.sendProgressBarUpdate(this, fieldIndex, field);
                }
            }
        }

        for (int fieldIndex = 0; fieldIndex < fields.length; fieldIndex++)
        {
            fields[fieldIndex] = inventory.getField(fieldIndex);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        this.inventory.setField(id, data);
    }
}
