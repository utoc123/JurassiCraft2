package org.jurassicraft.server.tile;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.sound.SoundHandler;
import org.jurassicraft.server.block.machine.FeederBlock;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.food.FoodHelper;

import java.util.Random;

public class FeederTile extends TileEntityLockable implements ITickable, ISidedInventory
{
    private static final int[] CARNIVOROUS_SLOTS = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
    private static final int[] HERBIVOROUS_SLOTS = new int[] { 9, 10, 11, 12, 13, 14, 15, 16, 17 };
    public int prevOpenAnimation;
    public int openAnimation;
    protected String customName;
    private ItemStack[] slots = new ItemStack[18];
    private int stayOpen;
    private boolean open;
    private DinosaurEntity feeding;

    @Override
    public Container createContainer(InventoryPlayer inventory, EntityPlayer player)
    {
        return null;
    }

    @Override
    public String getGuiID()
    {
        return JurassiCraft.MODID + ":feeder";
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return side.getAxis() == EnumFacing.Axis.Y ? CARNIVOROUS_SLOTS : HERBIVOROUS_SLOTS;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction)
    {
        return this.isItemValidForSlot(index, stack);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        return true;
    }

    @Override
    public int getSizeInventory()
    {
        return slots.length;
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        return slots[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        if (slots[index] != null)
        {
            ItemStack stack;

            if (slots[index].stackSize <= count)
            {
                stack = slots[index];
                slots[index] = null;
                return stack;
            }
            else
            {
                stack = slots[index].splitStack(count);

                if (slots[index].stackSize == 0)
                {
                    slots[index] = null;
                }

                return stack;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        if (slots[index] != null)
        {
            ItemStack itemstack = slots[index];
            slots[index] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        slots[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.worldObj.getTileEntity(this.pos) == this && player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {
    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public boolean receiveClientEvent(int id, int type)
    {
        if (id == 0)
        {
            open = type == 1;
            worldObj.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundHandler.FEEDER, SoundCategory.BLOCKS, 1.0F, open ? 1.0F : 0.9F, false);
            return true;
        }
        else
        {
            return super.receiveClientEvent(id, type);
        }
    }

    @Override
    public void clear()
    {
        for (int i = 0; i < slots.length; ++i)
        {
            slots[i] = null;
        }
    }

    @Override
    public String getName()
    {
        return hasCustomName() ? customName : "container.feeder";
    }

    @Override
    public boolean hasCustomName()
    {
        return this.customName != null && this.customName.length() > 0;
    }

    public void setCustomInventoryName(String customName)
    {
        this.customName = customName;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        NBTTagList itemList = compound.getTagList("Items", 10);
        ItemStack[] slots = new ItemStack[this.slots.length];

        for (int i = 0; i < itemList.tagCount(); ++i)
        {
            NBTTagCompound item = itemList.getCompoundTagAt(i);

            byte slot = item.getByte("Slot");

            if (slot >= 0 && slot < slots.length)
            {
                slots[slot] = ItemStack.loadItemStackFromNBT(item);
            }
        }

        if (compound.hasKey("CustomName", 8))
        {
            this.customName = compound.getString("CustomName");
        }

        this.slots = slots;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);

        NBTTagList itemList = new NBTTagList();

        for (int slot = 0; slot < getSizeInventory(); ++slot)
        {
            if (slots[slot] != null)
            {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setByte("Slot", (byte) slot);

                slots[slot].writeToNBT(itemTag);
                itemList.appendTag(itemTag);
            }
        }

        compound.setTag("Items", itemList);

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.customName);
        }

        return compound;
    }

    @Override
    public void update()
    {
        prevOpenAnimation = openAnimation;

        if (open && openAnimation < 20)
        {
            openAnimation++;
        }
        else if (!open && openAnimation > 0)
        {
            openAnimation--;
        }

        if (open && openAnimation == 19)
        {
            stayOpen = 20;
        }

        if (open && openAnimation == 20)
        {
            if (stayOpen > 0)
            {
                stayOpen--;

                if (stayOpen == 10 && feeding != null)
                {
                    int feedSlot = getFoodForDinosaur(feeding.getDinosaur());

                    Random random = new Random();

                    float offsetX = 0.5F;
                    float offsetY = 0.5F;
                    float offsetZ = 0.5F;

                    float motionX = 0.0F;
                    float motionY = 0.0F;
                    float motionZ = 0.0F;

                    switch (worldObj.getBlockState(pos).getValue(FeederBlock.FACING))
                    {
                        case UP:
                            offsetY = 1.0F;
                            motionY = 1.0F;
                            motionX = random.nextFloat() - 0.5F;
                            motionZ = random.nextFloat() - 0.5F;
                            break;
                        case DOWN:
                            offsetY = -1.0F;
                            break;
                        case NORTH:
                            offsetZ = -1.0F;
                            motionY = 0.5F;
                            motionZ = -0.5F;
                            break;
                        case SOUTH:
                            offsetZ = 1.0F;
                            motionY = 0.5F;
                            motionZ = 0.5F;
                            break;
                        case WEST:
                            offsetX = -1.0F;
                            motionY = 0.5F;
                            motionX = -0.5F;
                            break;
                        case EAST:
                            offsetX = 1.0F;
                            motionY = 0.5F;
                            motionX = 0.5F;
                            break;
                    }

                    ItemStack stack = slots[feedSlot];

                    if (stack != null)
                    {
                        EntityItem itemEntity = new EntityItem(worldObj, pos.getX() + offsetX, pos.getY() + offsetY, pos.getZ() + offsetZ, new ItemStack(stack.getItem(), 1, stack.getItemDamage()));
                        itemEntity.setDefaultPickupDelay();
                        itemEntity.motionX = motionX * 0.3F;
                        itemEntity.motionY = motionY * 0.3F;
                        itemEntity.motionZ = motionZ * 0.3F;
                        worldObj.spawnEntityInWorld(itemEntity);

                        decrStackSize(feedSlot, 1);
                    }

                    feeding = null;
                }
            }
            else
            {
                setOpen(false);
            }
        }
    }

    public void setOpen(boolean open)
    {
        this.open = open;

        if (!worldObj.isRemote)
        {
            this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 0, open ? 1 : 0);
        }

        if (!open)
        {
            feeding = null;
        }
    }

    public boolean canFeedDinosaur(Dinosaur dinosaur)
    {
        return getFoodForDinosaur(dinosaur) != -1;
    }

    private int getFoodForDinosaur(Dinosaur dinosaur)
    {
        int i = 0;

        for (ItemStack stack : slots)
        {
            if (stack != null && stack.stackSize > 0 && FoodHelper.isEdible(dinosaur.getDiet(), stack.getItem()))
            {
                return i;
            }

            i++;
        }

        return -1;
    }

    public DinosaurEntity getFeeding()
    {
        return feeding;
    }

    public void setFeeding(DinosaurEntity feeding)
    {
        this.feeding = feeding;
    }
}
