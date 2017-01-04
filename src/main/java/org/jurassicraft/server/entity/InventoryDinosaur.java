package org.jurassicraft.server.entity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.util.Random;

public class InventoryDinosaur implements IInventory {
    private DinosaurEntity entity;

    private NonNullList<ItemStack> inventory;

    public InventoryDinosaur(DinosaurEntity entity) {
        this.entity = entity;
        this.inventory = NonNullList.withSize(entity.getDinosaur().getStorage(), ItemStack.EMPTY);
    }

    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.size(); ++i) {
            if (!this.inventory.get(i).isEmpty()) {
                NBTTagCompound slotTag = new NBTTagCompound();
                slotTag.setByte("Slot", (byte) i);
                this.inventory.get(i).writeToNBT(slotTag);
                nbttaglist.appendTag(slotTag);
            }
        }

        nbt.setTag("Items", nbttaglist);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList items = nbt.getTagList("Items", 10);

        for (int i = 0; i < items.tagCount(); ++i) {
            NBTTagCompound slotTag = items.getCompoundTagAt(i);
            int slot = slotTag.getByte("Slot") & 255;

            if (slot >= 0 && slot < this.inventory.size()) {
                this.setInventorySlotContents(slot, new ItemStack(slotTag));
            }
        }
    }

    @Override
    public int getSizeInventory() {
        return this.inventory.size();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.inventory.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.inventory, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.inventory, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.inventory.set(index, stack);

        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return !this.entity.isDead && player.getDistanceSqToEntity(this.entity) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    @Override
    public String getName() {
        return this.entity.getName();
    }

    @Override
    public boolean hasCustomName() {
        return this.entity.hasCustomName();
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.entity.getDisplayName();
    }

    public void dropItems(World world, Random rand) {
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            ItemStack stack = this.getStackInSlot(i);

            if (!stack.isEmpty()) {
                float offsetX = rand.nextFloat() * 0.8F + 0.1F;
                float offsetY = rand.nextFloat() * 0.8F + 0.1F;
                float offsetZ = rand.nextFloat() * 0.8F + 0.1F;

                while (stack.getCount() > 0) {
                    EntityItem itemEntity = new EntityItem(world, this.entity.posX + offsetX, this.entity.posY + offsetY, this.entity.posZ + offsetZ, new ItemStack(stack.getItem(), 1, stack.getItemDamage()));
                    float multiplier = 0.05F;
                    itemEntity.motionX = (float) rand.nextGaussian() * multiplier;
                    itemEntity.motionY = (float) rand.nextGaussian() * multiplier + 0.2F;
                    itemEntity.motionZ = (float) rand.nextGaussian() * multiplier;
                    world.spawnEntity(itemEntity);
                }
            }
        }
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.inventory) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
