package org.jurassicraft.server.block.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;

public abstract class MachineBaseBlockEntity extends TileEntityLockable implements ITickable, ISidedInventory {
    protected String customName;

    protected int[] processTime = new int[this.getProcessCount()];
    protected int[] totalProcessTime = new int[this.getProcessCount()];

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        NBTTagList itemList = compound.getTagList("Items", 10);

        for (int i = 0; i < itemList.tagCount(); ++i) {
            NBTTagCompound item = itemList.getCompoundTagAt(i);

            byte slot = item.getByte("Slot");

            if (slot >= 0 && slot < this.getSizeInventory()) {
                this.getSlots().set(slot, new ItemStack(item));
            }
        }

        for (int i = 0; i < this.getProcessCount(); i++) {
            this.processTime[i] = compound.getShort("ProcessTime" + i);
            this.totalProcessTime[i] = compound.getShort("ProcessTimeTotal" + i);
        }

        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);

        for (int i = 0; i < this.getProcessCount(); i++) {
            compound.setShort("ProcessTime" + i, (short) this.processTime[i]);
            compound.setShort("ProcessTimeTotal" + i, (short) this.totalProcessTime[i]);
        }

        NonNullList<ItemStack> slots = this.getSlots();

        NBTTagList itemList = new NBTTagList();

        for (int slot = 0; slot < this.getSizeInventory(); ++slot) {
            ItemStack stack = slots.get(slot);
            if (!stack.isEmpty()) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setByte("Slot", (byte) slot);

                stack.writeToNBT(itemTag);
                itemList.appendTag(itemTag);
            }
        }

        compound.setTag("Items", itemList);

        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }

        return compound;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.getSlots().get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.getSlots(), index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.getSlots(), index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        NonNullList<ItemStack> slots = this.getSlots();

        boolean stacksEqual = !stack.isEmpty() && stack.isItemEqual(slots.get(index)) && ItemStack.areItemStackTagsEqual(stack, slots.get(index));
        slots.set(index, stack);

        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (!stacksEqual) {
            int process = this.getProcess(index);
            if (process >= 0 && process < this.getProcessCount()) {
                this.totalProcessTime[process] = this.getStackProcessTime(stack);
                if (!this.canProcess(process)) {
                    this.processTime[process] = 0;
                }
                this.markDirty();
            }
        }
    }

    private boolean isOutput(int slot) {
        int[] outputs = this.getOutputs();

        for (int output : outputs) {
            if (output == slot) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && this.customName.length() > 0;
    }

    public void setCustomInventoryName(String customName) {
        this.customName = customName;
    }

    @Override
    public int getSizeInventory() {
        return this.getSlots().size();
    }

    public boolean isProcessing(int index) {
        return this.processTime[index] > 0;
    }

    @Override
    public void update() {
        NonNullList<ItemStack> slots = this.getSlots();

        for (int process = 0; process < this.getProcessCount(); process++) {
            boolean flag = this.isProcessing(process);
            boolean sync = false;

            if (!this.world.isRemote) {
                boolean hasInput = false;

                for (int input : this.getInputs(process)) {
                    if (!slots.get(input).isEmpty()) {
                        hasInput = true;
                        break;
                    }
                }

                if (hasInput && this.canProcess(process)) {
                    this.processTime[process]++;

                    if (this.processTime[process] >= this.totalProcessTime[process]) {
                        this.processTime[process] = 0;
                        this.totalProcessTime[process] = this.getStackProcessTime(slots.get(this.getInputs()[0]));
                        this.processItem(process);
                    }

                    sync = true;
                } else if (this.isProcessing(process)) {
                    if (this.shouldResetProgress()) {
                        this.processTime[process] = 0;
                    } else if (this.processTime[process] > 0) {
                        this.processTime[process]--;
                    }

                    sync = true;
                }

                if (flag != this.isProcessing(process)) {
                    sync = true;
                }

                if (sync) {
                    this.markDirty();
                }
            }
        }
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.world.getTileEntity(this.pos) == this && player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return !this.isOutput(index);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return side == EnumFacing.DOWN ? this.getOutputs() : this.getInputs();
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
        return this.isItemValidForSlot(index, stack);
    }

    protected abstract int getProcess(int slot);

    protected abstract boolean canProcess(int process);

    protected abstract void processItem(int process);

    protected abstract int getMainOutput(int process);

    protected abstract int getStackProcessTime(ItemStack stack);

    protected abstract int getProcessCount();

    protected abstract int[] getInputs();

    protected abstract int[] getInputs(int process);

    protected abstract int[] getOutputs();

    protected abstract NonNullList<ItemStack> getSlots();

    protected abstract void setSlots(NonNullList<ItemStack> slots);

    public boolean hasOutputSlot(ItemStack output) {
        return this.getOutputSlot(output) != -1;
    }

    public int getOutputSlot(ItemStack output) {
        NonNullList<ItemStack> slots = this.getSlots();

        int[] outputs = this.getOutputs();

        for (int slot : outputs) {
            ItemStack stack = slots.get(slot);

            if (stack.isEmpty() || ((ItemStack.areItemStackTagsEqual(stack, output) && stack.getCount() + output.getCount() <= stack.getCount()) && stack.getItem() == output.getItem() && stack.getItemDamage() == output.getItemDamage())) {
                return slot;
            }
        }

        return -1;
    }

    @Override
    public int getField(int id) {
        int processCount = this.getProcessCount();

        if (id < processCount) {
            return this.processTime[id];
        } else if (id < processCount * 2) {
            return this.totalProcessTime[id - processCount];
        }

        return 0;
    }

    @Override
    public void setField(int id, int value) {
        int processCount = this.getProcessCount();

        if (id < processCount) {
            this.processTime[id] = value;
        } else if (id < processCount * 2) {
            this.totalProcessTime[id - processCount] = value;
        }
    }

    @Override
    public int getFieldCount() {
        return this.getProcessCount() * 2;
    }

    @Override
    public void clear() {
        this.getSlots().clear();
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return true;
    }

    protected void mergeStack(int slot, ItemStack stack) {
        NonNullList<ItemStack> slots = this.getSlots();
        ItemStack current = slots.get(slot);

        if (current.isEmpty()) {
            slots.set(slot, stack);
        } else if (current.getItem() == stack.getItem() && ItemStack.areItemStackTagsEqual(current, stack)) {
            current.grow(stack.getCount());
        }
    }

    protected void decreaseStackSize(int slot) {
        NonNullList<ItemStack> slots = this.getSlots();
        ItemStack stack = slots.get(slot);

        stack.shrink(1);

        if (stack.getCount() <= 0) {
            slots.set(slot, ItemStack.EMPTY);
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
    }

    protected boolean shouldResetProgress() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.getSlots()) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
