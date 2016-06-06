package org.jurassicraft.server.tile;

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
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.EntityHandler;
import org.jurassicraft.server.food.FoodNutrients;
import org.jurassicraft.server.item.ItemHandler;

import java.util.Random;

public class CultivatorTile extends MachineBaseTile
{
    private static final int[] INPUTS = new int[] { 0, 1, 2, 3 };
    private static final int[] OUTPUTS = new int[] { 4 };

    private ItemStack[] slots = new ItemStack[5];

    private int waterLevel;

    private int lipids;
    private int proximates;
    private int minerals;
    private int vitamins;

    private static final int MAX_NUTRIENTS = 3000;

    @Override
    protected int getProcess(int slot)
    {
        return 0;
    }

    @Override
    protected boolean canProcess(int process)
    {
        if (slots[0] != null && slots[0].getItem() == ItemHandler.INSTANCE.SYRINGE && waterLevel == 3)
        {
            Dinosaur dino = EntityHandler.INSTANCE.getDinosaurById(slots[0].getItemDamage());

            if (dino != null)
            {
                if (dino.isMammal() && lipids >= dino.getLipids() && minerals >= dino.getMinerals() && proximates >= dino.getProximates() && vitamins >= dino.getVitamins())
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
        Dinosaur dinoInEgg = EntityHandler.INSTANCE.getDinosaurById(slots[0].getItemDamage());

        waterLevel = 0;

        if (dinoInEgg != null)
        {
            lipids -= dinoInEgg.getLipids();
            minerals -= dinoInEgg.getMinerals();
            vitamins -= dinoInEgg.getVitamins();
            proximates -= dinoInEgg.getProximates();

            Class<? extends DinosaurEntity> dinoClass = dinoInEgg.getDinosaurClass();

            try
            {
                DinosaurEntity dino = dinoClass.getConstructor(World.class).newInstance(worldObj);

                dino.setDNAQuality(slots[0].getTagCompound().getInteger("DNAQuality"));
                dino.setGenetics((slots[0].getTagCompound().getString("Genetics")));

                int blockX = pos.getX();
                int blockY = pos.getY();
                int blockZ = pos.getZ();

                dino.setAge(0);

                dino.setLocationAndAngles(blockX + 0.5, blockY + 1, blockZ + 0.5, MathHelper.wrapDegrees(worldObj.rand.nextFloat() * 360.0F), 0.0F);
                dino.rotationYawHead = dino.rotationYaw;
                dino.renderYawOffset = dino.rotationYaw;

                worldObj.spawnEntityInWorld(dino);

                slots[0].stackSize--;

                if (slots[0].stackSize <= 0)
                {
                    slots[0] = null;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update()
    {
        super.update();

        boolean sync = false;

        if (!worldObj.isRemote)
        {
            if (waterLevel < 3 && slots[2] != null && slots[2].getItem() == Items.WATER_BUCKET)
            {
                if (slots[3] == null || slots[3].stackSize < 16)
                {
                    slots[2].stackSize--;

                    if (slots[2].stackSize <= 0)
                    {
                        slots[2] = null;
                    }

                    waterLevel++;

                    if (slots[3] == null)
                    {
                        slots[3] = new ItemStack(Items.BUCKET);
                    }
                    else if (slots[3].getItem() == Items.BUCKET)
                    {
                        slots[3].stackSize++;
                    }

                    sync = true;
                }
            }

            if (slots[1] != null && FoodNutrients.FOOD_LIST.containsKey(slots[1].getItem()))
            {
                if ((proximates < MAX_NUTRIENTS) || (minerals < MAX_NUTRIENTS) || (vitamins < MAX_NUTRIENTS) || (lipids < MAX_NUTRIENTS))
                {
                    consumeNutrients();
                    sync = true;
                }
            }
        }

        if (sync)
        {
            this.markDirty();
        }
    }

    private void consumeNutrients()
    {
        FoodNutrients nutrients = FoodNutrients.values()[FoodNutrients.FOOD_LIST.get(slots[1].getItem())];

        if (this.slots[1].getItem() instanceof ItemBucketMilk)
        {
            this.slots[1] = null;
            this.slots[1] = new ItemStack(Items.BUCKET);
        }
        else
        {
            this.slots[1].stackSize--;

            if (this.slots[1].stackSize <= 0)
            {
                this.slots[1] = null;
            }
        }

        Random random = new Random();

        if (proximates < MAX_NUTRIENTS)
        {
            proximates = (short) (proximates + (800 + random.nextInt(201)) * nutrients.getProximate());

            if (proximates > MAX_NUTRIENTS)
            {
                proximates = (short) MAX_NUTRIENTS;
            }
        }

        if (minerals < MAX_NUTRIENTS)
        {
            minerals = (short) (minerals + (900 + random.nextInt(101)) * nutrients.getMinerals());

            if (minerals > MAX_NUTRIENTS)
            {
                minerals = (short) MAX_NUTRIENTS;
            }
        }

        if (vitamins < MAX_NUTRIENTS)
        {
            vitamins = (short) (vitamins + (900 + random.nextInt(101)) * nutrients.getVitamins());

            if (vitamins > MAX_NUTRIENTS)
            {
                vitamins = (short) MAX_NUTRIENTS;
            }
        }

        if (lipids < MAX_NUTRIENTS)
        {
            lipids = (short) (lipids + (980 + random.nextInt(101)) * nutrients.getLipids());

            if (lipids > MAX_NUTRIENTS)
            {
                lipids = (short) MAX_NUTRIENTS;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        this.waterLevel = compound.getShort("WaterLevel");
        this.lipids = compound.getInteger("Lipids");
        this.minerals = compound.getInteger("Minerals");
        this.vitamins = compound.getInteger("Vitamins");
        this.proximates = compound.getInteger("Proximates");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);

        compound.setShort("WaterLevel", (short) this.waterLevel);
        compound.setInteger("Lipids", lipids);
        compound.setInteger("Minerals", minerals);
        compound.setInteger("Vitamins", vitamins);
        compound.setInteger("Proximates", proximates);

        return compound;
    }

    @Override
    protected int getMainOutput(int process)
    {
        return 4;
    }

    @Override
    protected int getStackProcessTime(ItemStack stack)
    {
        return 2000;
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
        return INPUTS;
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
        return new CultivateContainer(playerInventory, this);
    }

    @Override
    public String getGuiID()
    {
        return JurassiCraft.MODID + ":cultivator";
    }

    @Override
    public String getName()
    {
        return hasCustomName() ? customName : "container.cultivator";
    }

    public int getWaterLevel()
    {
        return waterLevel;
    }

    public int getMaxNutrients()
    {
        return MAX_NUTRIENTS;
    }

    public int getProximates()
    {
        return proximates;
    }

    public int getMinerals()
    {
        return minerals;
    }

    public int getVitamins()
    {
        return vitamins;
    }

    public int getLipids()
    {
        return lipids;
    }

    @Override
    public int getField(int id)
    {
        int processCount = getProcessCount();

        if (id < processCount)
        {
            return processTime[id];
        }
        else if (id < processCount * 2)
        {
            return totalProcessTime[id - processCount];
        }
        else
        {
            int type = id - (processCount * 2);

            switch (type)
            {
                case 0:
                    return waterLevel;
                case 1:
                    return proximates;
                case 2:
                    return minerals;
                case 3:
                    return vitamins;
                case 4:
                    return lipids;
            }
        }

        return 0;
    }

    @Override
    public void setField(int id, int value)
    {
        int processCount = getProcessCount();

        if (id < processCount)
        {
            processTime[id] = value;
        }
        else if (id < processCount * 2)
        {
            totalProcessTime[id - processCount] = value;
        }
        else
        {
            int type = id - (processCount * 2);

            switch (type)
            {
                case 0:
                    waterLevel = value;
                    break;
                case 1:
                    proximates = value;
                    break;
                case 2:
                    minerals = value;
                    break;
                case 3:
                    vitamins = value;
                    break;
                case 4:
                    lipids = value;
                    break;
            }
        }
    }

    @Override
    public int getFieldCount()
    {
        return getProcessCount() * 2 + 5;
    }

    public Dinosaur getDinosaur()
    {
        if (slots[0] != null)
        {
            return EntityHandler.INSTANCE.getDinosaurById(slots[0].getItemDamage());
        }

        return null;
    }
}
