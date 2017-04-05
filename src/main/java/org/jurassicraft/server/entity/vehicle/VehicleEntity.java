package org.jurassicraft.server.entity.vehicle;

import net.minecraft.util.DamageSource;
import org.jurassicraft.server.entity.vehicle.modules.SeatEntity;

public interface VehicleEntity {
    double getX();

    double getY();

    double getZ();

    float getRotationYaw();

    float getPrevRotationYaw();

    int getVehicleID();

    void addSeat(SeatEntity seatEntity);

    boolean attackVehicle(DamageSource source, float amount);

    SeatEntity getSeat(int id);

    void setState(byte state);

    byte getState();
}
