package org.jurassicraft.server.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.api.ISynthesizableItem;
import org.jurassicraft.server.container.DNASynthesizerContainer;
import org.jurassicraft.server.item.ItemHandler;

import java.util.Random;

public class DNASynthesizerTile extends MachineBaseTile
{
    private static final int[] INPUTS = new int[] { 0, 1, 2 };
    private static final int[] OUTPUTS = new int[] { 3, 4, 5, 6 };

    private ItemStack[] slots = new ItemStack[7];

    @Override
    protected int getProcess(int slot)
    {
        return 0;
    }

    @Override
    protected boolean canProcess(int process)
    {
        ItemStack storage = slots[0];
        ItemStack testTube = slots[1];
        ItemStack baseMaterial = slots[2];

        ISynthesizableItem synthesizableItem = ISynthesizableItem.getSynthesizableItem(storage);

        if (synthesizableItem != null && synthesizableItem.isSynthesizable(storage) && testTube != null && testTube.getItem() == ItemHandler.INSTANCE.empty_test_tube && baseMaterial != null && baseMaterial.getItem() == ItemHandler.INSTANCE.dna_base && (storage.getTagCompound() != null && storage.getTagCompound().hasKey("DNAQuality")))
        {
            ItemStack output = synthesizableItem.getSynthesizedItem(storage, new Random(0));

            return output != null && hasOutputSlot(output);
        }

        return false;
    }

    @Override
    protected void processItem(int process)
    {
        ItemStack storageDisc = slots[0];

        ItemStack output = ISynthesizableItem.getSynthesizableItem(storageDisc).getSynthesizedItem(storageDisc, new Random());

        int emptySlot = getOutputSlot(output);

        if (emptySlot != -1)
        {
            mergeStack(emptySlot, output);

            decreaseStackSize(1);
            decreaseStackSize(2);
        }
    }

    @Override
    protected int getMainOutput(int process)
    {
        return 1;
    }

    @Override
    protected int getStackProcessTime(ItemStack stack)
    {
        return 1000;
    }

    @Override
    protected int getProcessCount()
    {
        return 1;
    }

    @Override
    protected int[] getInputs()
    {
        return INPUTS;
    }

    @Override
    protected int[] getInputs(int process)
    {
        return getInputs();
    }

    @Override
    protected int[] getOutputs()
    {
        return OUTPUTS;
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
        return new DNASynthesizerContainer(playerInventory, this);
    }

    @Override
    public String getGuiID()
    {
        return JurassiCraft.MODID + ":dna_synthesizer";
    }

    @Override
    public String getName()
    {
        return hasCustomName() ? customName : "container.dna_synthesizer";
    }
}
