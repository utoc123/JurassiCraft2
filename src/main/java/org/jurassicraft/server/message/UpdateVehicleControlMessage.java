package org.jurassicraft.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jurassicraft.server.entity.vehicle.VehicleEntity;

public class UpdateVehicleControlMessage extends AbstractMessage<UpdateVehicleControlMessage> {
    private int entityId;
    private byte state;

    public UpdateVehicleControlMessage() {
    }

    public UpdateVehicleControlMessage(VehicleEntity entity) {
        this.entityId = entity.getVehicleID();
        this.state = entity.getState();
    }

    @Override
    public void onClientReceived(Minecraft minecraft, UpdateVehicleControlMessage message, EntityPlayer player, MessageContext context) {
    }

    @Override
    public void onServerReceived(MinecraftServer server, UpdateVehicleControlMessage message, EntityPlayer player, MessageContext context) {
        Entity entity = player.world.getEntityByID(message.entityId);

        if (entity instanceof VehicleEntity) {
            VehicleEntity vehicle = (VehicleEntity) entity;

            if (vehicle.getSeat(0) != null && vehicle.getSeat(0).getControllingPassenger() == player) {
                vehicle.setState(message.state);
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
        this.state = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeByte(this.state);
    }
}
