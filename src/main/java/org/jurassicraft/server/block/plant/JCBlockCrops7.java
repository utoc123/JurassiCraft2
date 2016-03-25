package org.jurassicraft.server.block.plant;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;

/**
 * Copyright 2016 Timeless Modding Mod
 */
public abstract class JCBlockCrops7 extends JCBlockCropsBase
{
    private static PropertyInteger AGE = PropertyInteger.create("age", 0, 6);;
    private static int MAX_AGE = 6;


    protected PropertyInteger getAgeProperty()
    {
        return AGE;
    }

    protected int getMaxAge()
    {
        return MAX_AGE;
    }

    // NOTE:  This is called on parent object construction.
    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {AGE});
    }

    //================

}
