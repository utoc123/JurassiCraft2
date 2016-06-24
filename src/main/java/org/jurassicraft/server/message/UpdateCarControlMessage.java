package org.jurassicraft.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jurassicraft.server.entity.vehicle.CarEntity;

public class UpdateCarControlMessage extends AbstractMessage<UpdateCarControlMessage>
{
    private int entityId;
    private byte state;

    public UpdateCarControlMessage()
    {
    }

    public UpdateCarControlMessage(CarEntity entity)
    {
        this.entityId = entity.getEntityId();
        this.state = entity.getState();
    }

    @Override
    public void onClientReceived(Minecraft minecraft, UpdateCarControlMessage message, EntityPlayer player, MessageContext context)
    {
    }

    @Override
    public void onServerReceived(MinecraftServer server, UpdateCarControlMessage message, EntityPlayer player, MessageContext context)
    {
        Entity entity = player.worldObj.getEntityByID(message.entityId);

        if (entity instanceof CarEntity)
        {
            CarEntity car = (CarEntity) entity;

            if (car.getSeat(0) != null && car.getSeat(0).getControllingPassenger() == player)
            {
                car.setState(message.state);
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        entityId = buf.readInt();
        state = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(entityId);
        buf.writeByte(state);
    }
}
