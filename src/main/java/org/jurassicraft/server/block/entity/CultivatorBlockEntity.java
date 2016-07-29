package org.jurassicraft.server.block.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.container.CultivateContainer;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.food.FoodNutrients;
import org.jurassicraft.server.item.ItemHandler;

import java.util.Random;

public class CultivatorBlockEntity extends MachineBaseBlockEntity {
    private static final int[] INPUTS = new int[] { 0, 1, 2, 3 };
    private static final int[] OUTPUTS = new int[] { 4 };
    private static final int MAX_NUTRIENTS = 3000;
    private ItemStack[] slots = new ItemStack[5];
    private int waterLevel;
    private int lipids;
    private int proximates;
    private int minerals;
    private int vitamins;

    @Override
    protected int getProcess(int slot) {
        return 0;
    }

    @Override
    protected boolean canProcess(int process) {
        if (this.slots[0] != null && this.slots[0].getItem() == ItemHandler.SYRINGE && this.waterLevel == 3) {
            Dinosaur dino = EntityHandler.getDinosaurById(this.slots[0].getItemDamage());

            if (dino != null) {
                if (dino.isMammal() && this.lipids >= dino.getLipids() && this.minerals >= dino.getMinerals() && this.proximates >= dino.getProximates() && this.vitamins >= dino.getVitamins()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected void processItem(int process) {
        Dinosaur dinoInEgg = EntityHandler.getDinosaurById(this.slots[0].getItemDamage());

        this.waterLevel = 0;

        if (dinoInEgg != null) {
            this.lipids -= dinoInEgg.getLipids();
            this.minerals -= dinoInEgg.getMinerals();
            this.vitamins -= dinoInEgg.getVitamins();
            this.proximates -= dinoInEgg.getProximates();

            Class<? extends DinosaurEntity> dinoClass = dinoInEgg.getDinosaurClass();

            try {
                DinosaurEntity dino = dinoClass.getConstructor(World.class).newInstance(this.worldObj);

                dino.setDNAQuality(this.slots[0].getTagCompound().getInteger("DNAQuality"));
                dino.setGenetics((this.slots[0].getTagCompound().getString("Genetics")));

                int blockX = this.pos.getX();
                int blockY = this.pos.getY();
                int blockZ = this.pos.getZ();

                dino.setAge(0);

                dino.setLocationAndAngles(blockX + 0.5, blockY + 1, blockZ + 0.5, MathHelper.wrapDegrees(this.worldObj.rand.nextFloat() * 360.0F), 0.0F);
                dino.rotationYawHead = dino.rotationYaw;
                dino.renderYawOffset = dino.rotationYaw;

                this.worldObj.spawnEntityInWorld(dino);

                this.slots[0].stackSize--;

                if (this.slots[0].stackSize <= 0) {
                    this.slots[0] = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update() {
        super.update();

        boolean sync = false;

        if (!this.worldObj.isRemote) {
            if (this.waterLevel < 3 && this.slots[2] != null && this.slots[2].getItem() == Items.WATER_BUCKET) {
                if (this.slots[3] == null || this.slots[3].stackSize < 16) {
                    this.slots[2].stackSize--;

                    if (this.slots[2].stackSize <= 0) {
                        this.slots[2] = null;
                    }

                    this.waterLevel++;

                    if (this.slots[3] == null) {
                        this.slots[3] = new ItemStack(Items.BUCKET);
                    } else if (this.slots[3].getItem() == Items.BUCKET) {
                        this.slots[3].stackSize++;
                    }

                    sync = true;
                }
            }

            if (this.slots[1] != null && FoodNutrients.FOOD_LIST.containsKey(this.slots[1].getItem())) {
                if ((this.proximates < MAX_NUTRIENTS) || (this.minerals < MAX_NUTRIENTS) || (this.vitamins < MAX_NUTRIENTS) || (this.lipids < MAX_NUTRIENTS)) {
                    this.consumeNutrients();
                    sync = true;
                }
            }
        }

        if (sync) {
            this.markDirty();
        }
    }

    private void consumeNutrients() {
        FoodNutrients nutrients = FoodNutrients.values()[FoodNutrients.FOOD_LIST.get(this.slots[1].getItem())];

        if (this.slots[1].getItem() instanceof ItemBucketMilk) {
            this.slots[1] = null;
            this.slots[1] = new ItemStack(Items.BUCKET);
        } else {
            this.slots[1].stackSize--;

            if (this.slots[1].stackSize <= 0) {
                this.slots[1] = null;
            }
        }

        Random random = new Random();

        if (this.proximates < MAX_NUTRIENTS) {
            this.proximates = (short) (this.proximates + (800 + random.nextInt(201)) * nutrients.getProximate());

            if (this.proximates > MAX_NUTRIENTS) {
                this.proximates = (short) MAX_NUTRIENTS;
            }
        }

        if (this.minerals < MAX_NUTRIENTS) {
            this.minerals = (short) (this.minerals + (900 + random.nextInt(101)) * nutrients.getMinerals());

            if (this.minerals > MAX_NUTRIENTS) {
                this.minerals = (short) MAX_NUTRIENTS;
            }
        }

        if (this.vitamins < MAX_NUTRIENTS) {
            this.vitamins = (short) (this.vitamins + (900 + random.nextInt(101)) * nutrients.getVitamins());

            if (this.vitamins > MAX_NUTRIENTS) {
                this.vitamins = (short) MAX_NUTRIENTS;
            }
        }

        if (this.lipids < MAX_NUTRIENTS) {
            this.lipids = (short) (this.lipids + (980 + random.nextInt(101)) * nutrients.getLipids());

            if (this.lipids > MAX_NUTRIENTS) {
                this.lipids = (short) MAX_NUTRIENTS;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        this.waterLevel = compound.getShort("WaterLevel");
        this.lipids = compound.getInteger("Lipids");
        this.minerals = compound.getInteger("Minerals");
        this.vitamins = compound.getInteger("Vitamins");
        this.proximates = compound.getInteger("Proximates");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);

        compound.setShort("WaterLevel", (short) this.waterLevel);
        compound.setInteger("Lipids", this.lipids);
        compound.setInteger("Minerals", this.minerals);
        compound.setInteger("Vitamins", this.vitamins);
        compound.setInteger("Proximates", this.proximates);

        return compound;
    }

    @Override
    protected int getMainOutput(int process) {
        return 4;
    }

    @Override
    protected int getStackProcessTime(ItemStack stack) {
        return 2000;
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
        return INPUTS;
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
        return new CultivateContainer(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return JurassiCraft.MODID + ":cultivator";
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.cultivator";
    }

    public int getWaterLevel() {
        return this.waterLevel;
    }

    public int getMaxNutrients() {
        return MAX_NUTRIENTS;
    }

    public int getProximates() {
        return this.proximates;
    }

    public int getMinerals() {
        return this.minerals;
    }

    public int getVitamins() {
        return this.vitamins;
    }

    public int getLipids() {
        return this.lipids;
    }

    @Override
    public int getField(int id) {
        int processCount = this.getProcessCount();

        if (id < processCount) {
            return this.processTime[id];
        } else if (id < processCount * 2) {
            return this.totalProcessTime[id - processCount];
        } else {
            int type = id - (processCount * 2);

            switch (type) {
                case 0:
                    return this.waterLevel;
                case 1:
                    return this.proximates;
                case 2:
                    return this.minerals;
                case 3:
                    return this.vitamins;
                case 4:
                    return this.lipids;
            }
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
        } else {
            int type = id - (processCount * 2);

            switch (type) {
                case 0:
                    this.waterLevel = value;
                    break;
                case 1:
                    this.proximates = value;
                    break;
                case 2:
                    this.minerals = value;
                    break;
                case 3:
                    this.vitamins = value;
                    break;
                case 4:
                    this.lipids = value;
                    break;
            }
        }
    }

    @Override
    public int getFieldCount() {
        return this.getProcessCount() * 2 + 5;
    }

    public Dinosaur getDinosaur() {
        if (this.slots[0] != null) {
            return EntityHandler.getDinosaurById(this.slots[0].getItemDamage());
        }

        return null;
    }
}
