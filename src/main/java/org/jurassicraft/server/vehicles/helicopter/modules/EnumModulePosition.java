package org.jurassicraft.server.vehicles.helicopter.modules;

import com.google.common.base.Function;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public enum EnumModulePosition
{
    FRONT(new Function<Vec3d, Boolean>()
    {
        @Override
        public Boolean apply(Vec3d vec)
        {
            return vec.zCoord > 0.6;
        }
    }),
    LEFT_SIDE(new Function<Vec3d, Boolean>()
    {
        @Override
        public Boolean apply(Vec3d vec)
        {
            return vec.zCoord < 0.6 && vec.xCoord > 0;
        }
    }),
    RIGHT_SIDE(new Function<Vec3d, Boolean>()
    {
        @Override
        public Boolean apply(Vec3d vec)
        {
            return vec.zCoord < 0.6 && vec.xCoord < 0;
        }
    });

    private final Function<Vec3d, Boolean> func;

    EnumModulePosition(Function<Vec3d, Boolean> clickCheckFunc)
    {
        this.func = Objects.requireNonNull(clickCheckFunc);
    }

    /**
     * Checks if the module is been clicked.
     *
     * @param v The helicopter-relative raytrace produced by the player trying to interact
     * @return True if the raytrace ends up in this module, false otherwise
     */
    public boolean isClicked(Vec3d v)
    {
        return func.apply(v);
    }
}
