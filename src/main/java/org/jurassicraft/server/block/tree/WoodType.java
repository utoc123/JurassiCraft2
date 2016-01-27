package org.jurassicraft.server.block.tree;

import net.minecraft.util.IStringSerializable;

public enum WoodType implements IStringSerializable
{
    GINKGO(0, "ginkgo"), CALAMITES(1, "calamites");
    private static final WoodType[] META_LOOKUP = new WoodType[values().length];
    private final int meta;
    private final String name;

    private WoodType(int meta, String name)
    {
        this.meta = meta;
        this.name = name;
    }

    public void setMetaLookup()
    {
        WoodType.META_LOOKUP[this.meta] = this;
    }

    public static WoodType[] getMetaLookup()
    {
        return META_LOOKUP;
    }

    public int getMetadata()
    {
        return this.meta;
    }

    public String toString()
    {
        return this.name;
    }

    public String getName()
    {
        return this.name;
    }
}
