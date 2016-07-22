package org.jurassicraft.server.message;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.vehicle.HelicopterBaseEntity;

public class HelicopterMessages {
    public static HelicopterBaseEntity getHeli(World world, int heliID) {
        try {
            return (HelicopterBaseEntity) world.getEntityByID(heliID);
        } catch (NullPointerException e) {
            // shhh
        }
        return null;
    }
}
