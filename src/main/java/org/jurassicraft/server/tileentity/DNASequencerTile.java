package org.jurassicraft.server.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.container.DNASequencerContainer;
import org.jurassicraft.server.genetics.DinoDNA;
import org.jurassicraft.server.genetics.GeneticsHelper;
import org.jurassicraft.server.item.JCItemRegistry;
import org.jurassicraft.server.item.SoftTissueItem;

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

        if (input != null && input.getItem() instanceof SoftTissueItem)
        {
            if (storage != null && storage.getItem() == JCItemRegistry.storage_disc)
            {
                ItemStack output = new ItemStack(JCItemRegistry.storage_disc, 1, input.getItemDamage());
                output.setTagCompound(input.getTagCompound());

                if (slots[process + 6] == null || ItemStack.areItemsEqual(slots[process + 6], output) && ItemStack.areItemStackTagsEqual(slots[process + 6], output))
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
        if (this.canProcess(process))
        {
            Random rand = new Random();

            int tissue = process * 2;

            // EntityPlayer player = worldObj.getPlayerEntityByUUID(UUID.fromString(slots[1].getTagCompound().getString("LastOwner")));

            int quality = rand.nextInt(25) + 1;

            if (rand.nextDouble() < 0.10)
            {
                quality += 25;

                if (rand.nextDouble() < 0.10)
                {
                    quality += 25;

                    if (rand.nextDouble() < 0.10)
                    {
                        quality += 25;
                    }
                }
            }

            NBTTagCompound nbt = slots[tissue + 1].getTagCompound();

            if (nbt == null)
            {
                nbt = new NBTTagCompound();
            }

            DinoDNA dna = new DinoDNA(quality, GeneticsHelper.randomGenetics(rand, slots[tissue].getItemDamage(), quality).toString());
            dna.writeToNBT(nbt);

            ItemStack output = new ItemStack(JCItemRegistry.storage_disc, 1, slots[tissue].getItemDamage());
            output.setItemDamage(dna.getContainer().getDinosaur());
            output.setTagCompound(nbt);

            // JCPlayerData.getPlayerData(player).addSequencedDNA(new DinoDNA(quality, GeneticsHelper.randomGenetics(rand, slots[0].getItemDamage(), quality).toString()));

            mergeStack(process + 6, output);

            decreaseStackSize(tissue);
            decreaseStackSize(tissue + 1);
        }
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

    public String getName()
    {
        return hasCustomName() ? customName : "container.dna_sequencer";
    }
}
