package org.jurassicraft.server.plant;

import net.minecraft.block.Block;

public abstract class Plant implements Comparable<Plant>
{
    public abstract String getName();

    public abstract Block getBlock();

    public boolean shouldRegister()
    {
        return true;
    }

    @Override
    public int compareTo(Plant plant)
    {
        return this.getName().compareTo(plant.getName());
    }
}
