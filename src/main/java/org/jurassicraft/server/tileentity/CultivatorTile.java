package org.jurassicraft.server.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.container.CultivateContainer;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.EntityHandler;
import org.jurassicraft.server.entity.item.CageSmallEntity;
import org.jurassicraft.server.food.JCFoodNutrients;
import org.jurassicraft.server.item.ItemHandler;

import java.util.List;
import java.util.Random;

public class CultivatorTile extends MachineBaseTile
{
    private int[] inputs = new int[] { 0, 1, 2, 3 };
    private int[] outputs = new int[] { 4 };

    private ItemStack[] slots = new ItemStack[5];

    private int waterLevel;

    private int lipids;
    private int proximates;
    private int minerals;
    private int vitamins;

    private int maxNutrients = 3000;

    @Override
    protected int getProcess(int slot)
    {
        return 0;
    }

    @Override
    protected boolean canProcess(int process)
    {
        if (slots[0] != null && slots[0].getItem() == ItemHandler.INSTANCE.syringe && waterLevel == 3)
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

                List<CageSmallEntity> cages = worldObj.getEntitiesWithinAABB(CageSmallEntity.class, new AxisAlignedBB(blockX - 2, blockY, blockZ - 2, blockX + 2, blockY + 1, blockZ + 2));

                CageSmallEntity cage = null;

                for (CageSmallEntity cCage : cages)
                {
                    if (cCage.getEntity() == null)
                    {
                        cage = cCage;
                        break;
                    }
                }

                if (cage != null)
                {
                    cage.setEntity(dino);
                }
                else
                {
                    dino.setLocationAndAngles(blockX + 0.5, blockY + 1, blockZ + 0.5, MathHelper.wrapAngleTo180_float(worldObj.rand.nextFloat() * 360.0F), 0.0F);
                    dino.rotationYawHead = dino.rotationYaw;
                    dino.renderYawOffset = dino.rotationYaw;

                    worldObj.spawnEntityInWorld(dino);
                }

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
            if (waterLevel < 3 && slots[2] != null && slots[2].getItem() == Items.water_bucket)
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
                        slots[3] = new ItemStack(Items.bucket);
                    }
                    else if (slots[3].getItem() == Items.bucket)
                    {
                        slots[3].stackSize++;
                    }

                    sync = true;
                }
            }

            if (slots[1] != null && JCFoodNutrients.FOODLIST.containsKey(slots[1].getItem()))
            {
                if ((proximates < maxNutrients) || (minerals < maxNutrients) || (vitamins < maxNutrients) || (lipids < maxNutrients))
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
        JCFoodNutrients nutrients = JCFoodNutrients.values()[JCFoodNutrients.FOODLIST.get(slots[1].getItem())];

        if (this.slots[1].getItem() instanceof ItemBucketMilk)
        {
            this.slots[1] = null;
            this.slots[1] = new ItemStack(Items.bucket);
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

        if (proximates < maxNutrients)
        {
            proximates = (short) (proximates + (800 + random.nextInt(201)) * nutrients.getProximate());

            if (proximates > maxNutrients)
            {
                proximates = (short) maxNutrients;
            }
        }

        if (minerals < maxNutrients)
        {
            minerals = (short) (minerals + (900 + random.nextInt(101)) * nutrients.getMinerals());

            if (minerals > maxNutrients)
            {
                minerals = (short) maxNutrients;
            }
        }

        if (vitamins < maxNutrients)
        {
            vitamins = (short) (vitamins + (900 + random.nextInt(101)) * nutrients.getVitamins());

            if (vitamins > maxNutrients)
            {
                vitamins = (short) maxNutrients;
            }
        }

        if (lipids < maxNutrients)
        {
            lipids = (short) (lipids + (980 + random.nextInt(101)) * nutrients.getLipids());

            if (lipids > maxNutrients)
            {
                lipids = (short) maxNutrients;
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
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        compound.setShort("WaterLevel", (short) this.waterLevel);
        compound.setInteger("Lipids", lipids);
        compound.setInteger("Minerals", minerals);
        compound.setInteger("Vitamins", vitamins);
        compound.setInteger("Proximates", proximates);
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
        return inputs;
    }

    @Override
    protected int[] getInputs(int process)
    {
        return inputs;
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
        return maxNutrients;
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
                    return maxNutrients;
                case 2:
                    return proximates;
                case 3:
                    return minerals;
                case 4:
                    return vitamins;
                case 5:
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
                    maxNutrients = value;
                    break;
                case 2:
                    proximates = value;
                    break;
                case 3:
                    minerals = value;
                    break;
                case 4:
                    vitamins = value;
                    break;
                case 5:
                    lipids = value;
                    break;
            }
        }
    }

    @Override
    public int getFieldCount()
    {
        return getProcessCount() * 2 + 6;
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
