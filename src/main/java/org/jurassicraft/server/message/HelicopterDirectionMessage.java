package org.jurassicraft.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jurassicraft.server.entity.vehicle.HelicopterBaseEntity;
import org.jurassicraft.server.util.MutableVec3;

public class HelicopterDirectionMessage extends AbstractMessage<HelicopterDirectionMessage>
{
    private int heliID;
    private MutableVec3 direction;

    public HelicopterDirectionMessage()
    {
        direction = new MutableVec3(0, 0, 0);
    }

    public HelicopterDirectionMessage(int heliID, MutableVec3 direction)
    {
        this.heliID = heliID;
        this.direction = direction;
    }

    @Override
    public void onClientReceived(Minecraft minecraft, HelicopterDirectionMessage message, EntityPlayer entityPlayer, MessageContext messageContext)
    {
        HelicopterBaseEntity helicopter = HelicopterMessages.getHeli(entityPlayer.worldObj, message.heliID);
        if (helicopter != null)
        {
            helicopter.setDirection(message.direction);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, HelicopterDirectionMessage message, EntityPlayer entityPlayer, MessageContext messageContext)
    {
        HelicopterBaseEntity helicopter = HelicopterMessages.getHeli(entityPlayer.worldObj, message.heliID);
        if (helicopter != null)
        {
            helicopter.setDirection(message.direction);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        heliID = buf.readInt();
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        direction.set(x, y, z);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(heliID);
        buf.writeDouble(direction.xCoord);
        buf.writeDouble(direction.yCoord);
        buf.writeDouble(direction.zCoord);
    }
}
