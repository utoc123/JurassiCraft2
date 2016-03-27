package org.jurassicraft.server.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.api.ISequencableItem;
import org.jurassicraft.server.container.DNASequencerContainer;
import org.jurassicraft.server.item.ItemHandler;

import java.util.Random;

public class DNASequencerTile extends MachineBaseTile
{
    private int[] inputs = new int[] { 0, 1, 2, 3, 4, 5 };
    private int[] inputsProcess1 = new int[] { 0, 1 };
    private int[] inputsProcess2 = new int[] { 2, 3 };
    private int[] inputsProcess3 = new int[] { 4, 5 };

    private int[] outputs = new int[] { 6, 7, 8 };

    private ItemStack[] slots = new ItemStack[9];

    @Override
    protected int getProcess(int slot)
    {
        return (int) Math.floor(slot / 2);
    }

    @Override
    protected boolean canProcess(int process)
    {
        int tissue = process * 2;

        ItemStack input = slots[tissue];
        ItemStack storage = slots[tissue + 1];

        ISequencableItem sequencableItem = ISequencableItem.getSequencableItem(input);

        if (sequencableItem != null && sequencableItem.isSequencable(input))
        {
            if (storage != null && storage.getItem() == ItemHandler.INSTANCE.storage_disc)
            {
                if (slots[process + 6] == null)
                {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected void processItem(int process)
    {
        Random rand = new Random();

        int tissue = process * 2;

        ItemStack sequencableStack = slots[tissue];

        mergeStack(process + 6, ISequencableItem.getSequencableItem(sequencableStack).getSequenceOutput(sequencableStack, rand));

        decreaseStackSize(tissue);
        decreaseStackSize(tissue + 1);
    }

    @Override
    protected int getMainOutput(int process)
    {
        return (process * 2) + 1;
    }

    @Override
    protected int getStackProcessTime(ItemStack stack)
    {
        return 2000;
    }

    @Override
    protected int getProcessCount()
    {
        return 3;
    }

    @Override
    protected int[] getInputs()
    {
        return inputs;
    }

    @Override
    protected int[] getInputs(int process)
    {
        if (process == 0)
        {
            return inputsProcess1;
        }
        else if (process == 1)
        {
            return inputsProcess2;
        }

        return inputsProcess3;
    }

    @Override
    protected int[] getOutputs()
    {
        return outputs;
    }

    @Override
    protected ItemStack[] getSlots()
    {
        return slots;
    }

    @Override
    protected void setSlots(ItemStack[] slots)
    {
        this.slots = slots;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new DNASequencerContainer(playerInventory, this);
    }

    @Override
    public String getGuiID()
    {
        return JurassiCraft.MODID + ":dna_sequencer";
    }

    @Override
    public String getName()
    {
        return hasCustomName() ? customName : "container.dna_sequencer";
    }
}
