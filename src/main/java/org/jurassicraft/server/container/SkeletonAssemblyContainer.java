package org.jurassicraft.server.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.container.slot.FossilSlotCrafting;
import org.jurassicraft.server.container.slot.SkeletonCraftingSlot;
import org.jurassicraft.server.item.DisplayBlockItem;
import org.jurassicraft.server.item.FossilItem;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.util.LangHelper;

import javax.annotation.Nullable;

public class SkeletonAssemblyContainer extends Container {
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 5, 5);
    public IInventory craftResult = new InventoryCraftResult();
    private final World worldObj;
    private final BlockPos pos;
    public String error;

    public SkeletonAssemblyContainer(InventoryPlayer playerInventory, World worldIn, BlockPos posIn) {
        this.worldObj = worldIn;
        this.pos = posIn;
        this.addSlotToContainer(new SkeletonCraftingSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, 140, 52));

        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                this.addSlotToContainer(new FossilSlotCrafting(this.craftMatrix, j + i * 5, 16 + j * 18, 16 + i * 18));
            }
        }

        for (int k = 0; k < 3; ++k) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlotToContainer(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 119 + k * 18));
            }
        }

        for (int l = 0; l < 9; ++l) {
            this.addSlotToContainer(new Slot(playerInventory, l, 8 + l * 18, 177));
        }

        this.onCraftMatrixChanged(this.craftMatrix);
    }

/*    @Override
    public void onCraftMatrixChanged(IInventory inventory) {
        this.error = "";
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
                    this.error = new LangHelper("crafting.skeleton.mismatched").build();
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
                this.error = new LangHelper("crafting.skeleton.needed")
                        .withProperty("bonename", "item." + needed[x] + (fresh ? "_fresh" : "") + ".name")
                        .withProperty("dino", "entity.jurassicraft." + EntityHandler.getDinosaurById(dino).getName()
                                .replace(" ", "_").toLowerCase(Locale.ENGLISH) + ".name")
                        .build();
                this.craftResult.setInventorySlotContents(0, null);
                return;
            } else if (needWeights[x] < 0) {
                this.error = new LangHelper("crafting.skeleton.toomany")
                        .withProperty("bonename", "item." + needed[x] + (fresh ? "_fresh" : "") + ".name")
                        .withProperty("dino", "entity.jurassicraft." + EntityHandler.getDinosaurById(dino).getName()
                                .replace(" ", "_").toLowerCase(Locale.ENGLISH) + ".name")
                        .build();
                this.craftResult.setInventorySlotContents(0, null);
                return;
            }
        }
        if (init) {
                int metadata = ItemHandler.DISPLAY_BLOCK.getMetadata(dino, fresh ? 2 : 1, true);
                this.craftResult.setInventorySlotContents(0, new ItemStack(ItemHandler.DISPLAY_BLOCK, 1, metadata));
                return;
        }
        this.craftResult.setInventorySlotContents(0, null);
    }*/

    @Override
    public void onCraftMatrixChanged(IInventory inventory) {
        error = "";
        boolean init = false;
        boolean fresh = false;
        int dino = 0;
        String[][] recipe = { {} };
        int maxX = -1;
        int maxY = -1;
        int minX = 25;
        int minY = 25;

        for (int i = 0; i < 25; i++) {
            ItemStack is = this.craftMatrix.getStackInSlot(i);
            if (is != null) {
                if (!(is.getItem() instanceof FossilItem)) {
                    this.craftResult.setInventorySlotContents(0, null);
                    return;
                }
                maxX = Math.max(maxX, i % 5);
                maxY = Math.max(maxY, i / 5);
                minX = Math.min(minX, i % 5);
                minY = Math.min(minY, i / 5);

                FossilItem item = ((FossilItem) is.getItem());
                if (!init) {
                    init = true;
                    dino = is.getMetadata();
                    fresh = item.isFresh();
                    recipe = item.getDinosaur(is).getRecipe();
                }
                if (fresh != item.isFresh() || dino != is.getMetadata()) {
                    this.error = new LangHelper("crafting.skeleton.mismatched").build();
                    this.craftResult.setInventorySlotContents(0, null);
                    return;
                }
            }
        }
        if (!init) {
            this.craftResult.setInventorySlotContents(0, null);
            return;
        }
        if (maxX - minX == recipe[0].length - 1 && maxY - minY == recipe.length - 1) {
            for (int y = 0; y < recipe.length; y++) {
                for (int x = 0; x < recipe.length; x++) {
                    ItemStack is = this.craftMatrix.getStackInSlot(x + minX + (y + minY) * 5);
                    String name = "";
                    if (is != null) {
                        name = ((FossilItem) is.getItem()).getBoneType();
                    }
                    if (!recipe[y][x].equals(name)) {
                        this.craftResult.setInventorySlotContents(0, null);
                        return;
                    }
                }
            }
            int metadata = DisplayBlockItem.getMetadata(dino, fresh ? 2 : 1, true);
            this.craftResult.setInventorySlotContents(0, new ItemStack(ItemHandler.DISPLAY_BLOCK, 1, metadata));
            return;
        }
        this.craftResult.setInventorySlotContents(0, null);
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

        if (!this.worldObj.isRemote) {
            for (int i = 0; i < 25; ++i) {
                ItemStack stack = this.craftMatrix.removeStackFromSlot(i);

                if (stack != null) {
                    player.dropItem(stack, false);
                }
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.worldObj.getBlockState(this.pos).getBlock() == BlockHandler.SKELETON_ASSEMBLY && playerIn.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
                (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    @Nullable
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack stack = null;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            stack = itemstack1.copy();

            if (index == 0) {
                if (!this.mergeItemStack(itemstack1, 26, 62, true)) {
                    return null;
                }

                slot.onSlotChange(itemstack1, stack);
            } else if (index >= 26 && index < 53) {
                if (!this.mergeItemStack(itemstack1, 53, 62, false)) {
                    return null;
                }
            } else if (index >= 53 && index < 62) {
                if (!this.mergeItemStack(itemstack1, 26, 53, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 26, 53, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == stack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(playerIn, itemstack1);
        }

        return stack;
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
    }
}