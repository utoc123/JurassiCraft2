package org.jurassicraft.server.container;

import java.util.Locale;

import javax.annotation.Nullable;

import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.container.slot.FossilSlot;
import org.jurassicraft.server.container.slot.FossilSlotCrafting;
import org.jurassicraft.server.container.slot.SkeletonCraftingSlot;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.item.FossilItem;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.util.LangHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SkeletonAssemblyContainer extends Container {
    /** The crafting matrix inventory (3x3). */
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 5, 5);
    public IInventory craftResult = new InventoryCraftResult();
    private final World worldObj;
    /** Position of the workbench */
    private final BlockPos pos;
    public String error;

    public SkeletonAssemblyContainer(InventoryPlayer playerInventory, World worldIn, BlockPos posIn) {
        this.worldObj = worldIn;
        this.pos = posIn;
        this.addSlotToContainer(
                new SkeletonCraftingSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, 135, 45));

        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                this.addSlotToContainer(new FossilSlotCrafting(this.craftMatrix, j + i * 5, 27 + j * 18, 9 + i * 18));
            }
        }

        for (int k = 0; k < 3; ++k) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlotToContainer(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 112 + k * 18));
            }
        }

        for (int l = 0; l < 9; ++l) {
            this.addSlotToContainer(new Slot(playerInventory, l, 8 + l * 18, 170));
        }

        this.onCraftMatrixChanged(this.craftMatrix);
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        error = "";
        boolean init = false;
        boolean fresh = false;
        int dino = 0;
        String[] needed = new String[0];
        int[] needWeights = new int[0];
        for (int i = 0; i < 25; i++) {
            ItemStack is = this.craftMatrix.getStackInSlot(i);
            if (is != null) {
                if (!(is.getItem() instanceof FossilItem)) {
                    this.craftResult.setInventorySlotContents(0, null);
                    return;
                }
                FossilItem item = ((FossilItem) is.getItem());
                if (!init) {
                    init = true;
                    dino = is.getMetadata();
                    fresh = item.isFresh();
                    needed = item.getDinosaur(is).getBones();
                    needWeights = new int[needed.length];
                    for (int x = 0; x < needed.length; x++) {
                        needWeights[x] = (needed[x].indexOf("leg") > -1) ? 2 : 1;
                        // System.out.println(needed[x]+" = "+needWeights[x]);
                    }

                }
                if (fresh != item.isFresh() || dino != is.getMetadata()) {
                    error = new LangHelper("crafting.skeleton.mismatched").build();
                    this.craftResult.setInventorySlotContents(0, null);
                    return;
                } else {
                    String bone = item.getBoneType();
                    for (int x = 0; x < needed.length; x++) {
                        if (bone.equals(needed[x])) {
                            needWeights[x] = needWeights[x] - 1;
                            // System.out.println(needed[x]+" now =
                            // "+needWeights[x]);
                            break;
                        }
                    }
                }

            }
        }
        for (int x = 0; x < needed.length; x++) {
            if (needWeights[x] > 0) {
                error = new LangHelper("crafting.skeleton.needed")
                        .withProperty("bonename", "item." + needed[x] + (fresh ? "_fresh" : "") + ".name")
                        .withProperty("dino", "entity.jurassicraft." + EntityHandler.getDinosaurById(dino).getName()
                                .replace(" ", "_").toLowerCase(Locale.ENGLISH) + ".name")
                        .build();
                this.craftResult.setInventorySlotContents(0, null);
                return;
            } else if (needWeights[x] < 0) {
                error = new LangHelper("crafting.skeleton.toomany")
                        .withProperty("bonename", "item." + needed[x] + (fresh ? "_fresh" : "") + ".name")
                        .withProperty("dino", "entity.jurassicraft." + EntityHandler.getDinosaurById(dino).getName()
                                .replace(" ", "_").toLowerCase(Locale.ENGLISH) + ".name")
                        .build();
                this.craftResult.setInventorySlotContents(0, null);
                return;
            }
        }
        if (init) {
            this.craftResult.setInventorySlotContents(0, ItemHandler.ACTION_FIGURE
                    .establishNBT(new ItemStack(ItemHandler.ACTION_FIGURE, 1, dino), fresh ? 2 : 1, true));
            return;
        }
        this.craftResult.setInventorySlotContents(0, null);
        return;
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);

        if (!this.worldObj.isRemote) {
            for (int i = 0; i < 25; ++i) {
                ItemStack itemstack = this.craftMatrix.removeStackFromSlot(i);

                if (itemstack != null) {
                    playerIn.dropItem(itemstack, false);
                }
            }
        }
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.worldObj.getBlockState(this.pos).getBlock() != BlockHandler.SKELETON_ASSEMBLY ? false
                : playerIn.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
                        (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    @Nullable
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 0)
            {
                if (!this.mergeItemStack(itemstack1, 26, 62, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index >= 26 && index < 53)
            {
                if (!this.mergeItemStack(itemstack1, 53, 62, false))
                {
                    return null;
                }
            }
            else if (index >= 53 && index < 62)
            {
                if (!this.mergeItemStack(itemstack1, 26, 53, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 26, 53, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(playerIn, itemstack1);
        }

        return itemstack;
    }

    /**
     * Called to determine if the current slot is valid for the stack merging
     * (double-click) code. The stack passed in is null for the initial slot
     * that was double-clicked.
     */
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
    }
}