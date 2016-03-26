package org.jurassicraft.server.block.plant;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;

/**
 * Copyright 2016 Timeless Modding Mod
 */
public abstract class JCBlockCrops8 extends JCBlockCropsBase
{
    private static PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
    private static int MAX_AGE = 7;

    @Override
    protected PropertyInteger getAgeProperty()
    {
        return AGE;
    }

    @Override
    protected int getMaxAge()
    {
        return MAX_AGE;
    }

    // NOTE:  This is called on parent object construction.
    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] { AGE });
    }

    //================

}
