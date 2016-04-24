package org.jurassicraft.server.entity.base;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class MetabolismContainer
{
    public static final double STARVING_THRESHOLD = 0.1;

    public static final int MAX_DIGESTION_AMOUNT = 3000;

    // Basically this is ticks of energy and ticks of water.  Specia actions like mating/healing
    // cause them to loose faster.
    private final int MAX_ENERGY;
    private final int MAX_WATER;

    private int energy;
    private int digestingFood;
    private int water;

    private DinosaurEntity dinosaur;

    public MetabolismContainer(DinosaurEntity dinosaur)
    {
        this.dinosaur = dinosaur;

        // Each 24000 is one day!  So an adult dino (like an apatosaur) has like 8 days of energy?
        MAX_ENERGY = (int) (24000 * (dinosaur.getDinosaur().getAdultHealth() / 15));
        MAX_WATER = (int) (24000 * (dinosaur.getDinosaur().getAdultHealth() / 15));

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

    public int getEnergy()
    {
        return energy;
    }

    public int getDigestingFood()
    {
        return digestingFood;
    }

    public void decreaseEnergy(int amount)
    {
        energy -= amount;

        if (energy <= 0)
        {
            dinosaur.attackEntityFrom(DamageSource.starve, 1.0F);
        }
    }

    public void decreaseWater(int amount)
    {
        water -= amount;

        if (water <= 0)
        {
            dinosaur.attackEntityFrom(DamageSource.starve, 1.0F);
        }
    }

    public void setWater(int water)
    {
        this.water = Math.min(water, MAX_WATER);
    }

    public void setEnergy(int energy)
    {
        this.energy = Math.min(energy, MAX_ENERGY);
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

    public void writeSpawnData(ByteBuf buf)
    {
        buf.writeInt(water);
        buf.writeInt(energy);
        buf.writeInt(digestingFood);
    }

    public void readSpawnData(ByteBuf buf)
    {
        water = buf.readInt();
        energy = buf.readInt();
        digestingFood = buf.readInt();
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

    public void increaseDigestingFood(int amount)
    {
        this.setDigestingFoodAmount(digestingFood + amount);
    }

    public void increaseWater(int amount)
    {
        this.setWater(water + amount);
    }

    public boolean isStarving()
    {
        return (double) this.energy / MAX_ENERGY < STARVING_THRESHOLD;
    }

    public boolean isDehydrated()
    {
        return (double) this.water / MAX_WATER < STARVING_THRESHOLD;
    }

    public boolean isHungry()
    {
        return (this.energy + (digestingFood * 2) < MAX_ENERGY * 0.9 || isStarving()) && digestingFood < MAX_DIGESTION_AMOUNT;
    }

    public boolean isThirsty()
    {
        return this.water < MAX_WATER * 0.5;
    }
}
