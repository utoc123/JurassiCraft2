package org.jurassicraft.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class SetOrderMessage extends AbstractMessage<SetOrderMessage>
{
    private int entityId;
    private DinosaurEntity.Order order;

    public SetOrderMessage()
    {
    }

    public SetOrderMessage(DinosaurEntity entity)
    {
        entityId = entity.getEntityId();
        order = entity.getOrder();
    }

    @Override
    public void onClientReceived(Minecraft minecraft, SetOrderMessage message, EntityPlayer player, MessageContext messageContext)
    {
    }

    @Override
    public void onServerReceived(MinecraftServer server, SetOrderMessage message, EntityPlayer player, MessageContext messageContext)
    {
        Entity entity = player.getEntityWorld().getEntityByID(message.entityId);

        if (entity instanceof DinosaurEntity)
        {
            DinosaurEntity dinosaur = (DinosaurEntity) entity;

            if (dinosaur.getOwner() != null && dinosaur.getOwner().equals(player.getUniqueID()))
            {
                dinosaur.setOrder(message.order);
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        entityId = buf.readInt();
        order = DinosaurEntity.Order.values()[Math.max(0, buf.readByte() % DinosaurEntity.Order.values().length)];
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(entityId);
        buf.writeByte(order.ordinal());
    }
}
