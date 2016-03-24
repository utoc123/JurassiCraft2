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
    ;
    private static int MAX_AGE = 7;


    protected PropertyInteger getAgeProperty()
    {
        return AGE;
    }

    protected int getMaxAge()
    {
        return 7;
    }

    // NOTE:  This is called on parent object construction.
    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] { AGE });
    }

    //================

}
