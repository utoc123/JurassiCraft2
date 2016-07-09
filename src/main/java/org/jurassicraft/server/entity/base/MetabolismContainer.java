package org.jurassicraft.server.entity.base;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class MetabolismContainer
{
    public static final double STARVING_THRESHOLD = 0.1;

    public static final int MAX_DIGESTION_AMOUNT = 3000;

    private final int MAX_ENERGY;
    private final int MAX_WATER;

    private int energy;
    private int digestingFood;
    private int water;

    private DinosaurEntity dinosaur;

    public MetabolismContainer(DinosaurEntity dinosaur)
    {
        this.dinosaur = dinosaur;

        double health = dinosaur.getDinosaur().getAdultHealth();

        MAX_ENERGY = (int) (health / 15.0 * 24000);
        MAX_WATER = (int) (health / 15.0 * 24000);

        this.energy = MAX_ENERGY;
        this.water = MAX_WATER;
    }

    public void update()
    {
        if (!dinosaur.isDead && !dinosaur.isCarcass() && dinosaur.worldObj.getGameRules().getBoolean("dinoMetabolism"))
        {
            this.decreaseEnergy(1);
            this.decreaseWater(1);

            if (dinosaur.isWet())
            {
                water = MAX_WATER;
            }

            if (digestingFood > 0)
            {
                increaseEnergy(2);
                digestingFood--;
            }
        }
    }

    public int getWater()
    {
        return water;
    }

    public void setWater(int water)
    {
        this.water = Math.min(water, MAX_WATER);
    }

    public int getEnergy()
    {
        return energy;
    }

    public void setEnergy(int energy)
    {
        this.energy = Math.min(energy, MAX_ENERGY);
    }

    public int getDigestingFood()
    {
        return digestingFood;
    }

    public void decreaseEnergy(int amount)
    {
        energy -= amount;
        energy = Math.max(0, energy);

        if (isStarving() && dinosaur.ticksExisted % 10 == 0)
        {
            dinosaur.attackEntityFrom(DamageSource.starve, 5.0F);
        }
    }

    public void decreaseWater(int amount)
    {
        water -= amount;
        water = Math.max(0, water);

        if (isDehydrated() && dinosaur.ticksExisted % 10 == 0)
        {
            dinosaur.attackEntityFrom(DamageSource.starve, 5.0F);
        }
    }

    public void setDigestingFoodAmount(int digesting)
    {
        this.digestingFood = Math.min(digesting, MAX_DIGESTION_AMOUNT);
    }

    public void readFromNBT(NBTTagCompound nbt)
    {
        this.water = nbt.getInteger("Water");
        this.energy = nbt.getInteger("Energy");
        this.digestingFood = nbt.getInteger("DigestingFood");
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("Water", water);
        nbt.setInteger("Energy", energy);
        nbt.setInteger("DigestingFood", digestingFood);
    }

    public int getMaxEnergy()
    {
        return MAX_ENERGY;
    }

    public int getMaxWater()
    {
        return MAX_WATER;
    }

    public void increaseEnergy(int amount)
    {
        this.setEnergy(energy + amount);
    }

    public void eat(int amount)
    {
        this.increaseEnergy(amount / 10);
        this.setDigestingFoodAmount(digestingFood + amount);
    }

    public void increaseWater(int amount)
    {
        this.setWater(water + amount);
    }

    public boolean isStarving()
    {
        return (double) this.energy < 50 && digestingFood <= 0;
    }

    public boolean isDehydrated()
    {
        return (double) this.water < 50;
    }

    public boolean isHungry()
    {
        return (this.energy + (digestingFood * 2) < MAX_ENERGY * 0.8 || energy < 50) && digestingFood + 500 < MAX_DIGESTION_AMOUNT;
    }

    public boolean isThirsty()
    {
        return this.water < MAX_WATER * 0.5;
    }
}
