package org.jurassicraft.server.block.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.container.EmbryonicMachineContainer;
import org.jurassicraft.server.item.DNAItem;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.item.PlantDNAItem;

public class EmbryonicMachineBlockEntity extends MachineBaseBlockEntity {
    private static final int[] INPUTS = new int[] { 0, 1, 2 };
    private static final int[] OUTPUTS = new int[] { 3, 4, 5, 6 };

    private ItemStack[] slots = new ItemStack[7];

    @Override
    protected int getProcess(int slot) {
        return 0;
    }

    @Override
    protected boolean canProcess(int process) {
        ItemStack dna = this.slots[0];
        ItemStack petridish = this.slots[1];
        ItemStack syringe = this.slots[2];

        if (dna != null && petridish != null && syringe != null && syringe.getItem() == ItemHandler.EMPTY_SYRINGE) {
            ItemStack output = null;

            if (petridish.getItem() == ItemHandler.PETRI_DISH && dna.getItem() instanceof DNAItem) {
                output = new ItemStack(ItemHandler.SYRINGE, 1, dna.getItemDamage());
                output.setTagCompound(dna.getTagCompound());
            } else if (petridish.getItem() == ItemHandler.PLANT_CELLS_PETRI_DISH && dna.getItem() instanceof PlantDNAItem) {
                output = new ItemStack(ItemHandler.PLANT_CALLUS, 1, dna.getItemDamage());
                output.setTagCompound(dna.getTagCompound());
            }

            return output != null && this.hasOutputSlot(output);
        }

        return false;
    }

    @Override
    protected void processItem(int process) {
        if (this.canProcess(process)) {
            ItemStack output = null;

            if (this.slots[0].getItem() instanceof DNAItem && this.slots[1].getItem() == ItemHandler.PETRI_DISH) {
                output = new ItemStack(ItemHandler.SYRINGE, 1, this.slots[0].getItemDamage());
            } else if (this.slots[0].getItem() instanceof PlantDNAItem && this.slots[1].getItem() == ItemHandler.PLANT_CELLS_PETRI_DISH) {
                output = new ItemStack(ItemHandler.PLANT_CALLUS, 1, this.slots[0].getItemDamage());
            }

            output.setTagCompound(this.slots[0].getTagCompound());

            int emptySlot = this.getOutputSlot(output);

            if (emptySlot != -1) {
                this.mergeStack(emptySlot, output);

                this.decreaseStackSize(0);
                this.decreaseStackSize(1);
                this.decreaseStackSize(2);
            }
        }
    }

    @Override
    protected int getMainOutput(int process) {
        return 1;
    }

    @Override
    protected int getStackProcessTime(ItemStack stack) {
        return 200;
    }

    @Override
    protected int getProcessCount() {
        return 1;
    }

    @Override
    protected int[] getInputs() {
        return INPUTS;
    }

    @Override
    protected int[] getInputs(int process) {
        return this.getInputs();
    }

    @Override
    protected int[] getOutputs() {
        return OUTPUTS;
    }

    @Override
    protected ItemStack[] getSlots() {
        return this.slots;
    }

    @Override
    protected void setSlots(ItemStack[] slots) {
        this.slots = slots;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new EmbryonicMachineContainer(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return JurassiCraft.MODID + ":embryonic_machine";
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.embryonic_machine";
    }

    @Override
    protected void onSlotUpdate() {
        super.onSlotUpdate();
        this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
    }
}
