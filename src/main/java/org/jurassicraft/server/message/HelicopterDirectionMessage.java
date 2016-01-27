package org.jurassicraft.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.timeless.unilib.utils.MutableVec3;
import org.jurassicraft.server.vehicles.helicopter.HelicopterBaseEntity;

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
    @SideOnly(Side.CLIENT)
    public void handleClientMessage(HelicopterDirectionMessage messageHelicopterDirection, EntityPlayer entityPlayer)
    {
        HelicopterBaseEntity helicopter = HelicopterMessages.getHeli(entityPlayer.worldObj, messageHelicopterDirection.heliID);
        if (helicopter != null)
        {
            helicopter.setDirection(messageHelicopterDirection.direction);
        }
    }

    @Override
    public void handleServerMessage(HelicopterDirectionMessage messageHelicopterDirection, EntityPlayer entityPlayer)
    {
        HelicopterBaseEntity helicopter = HelicopterMessages.getHeli(entityPlayer.worldObj, messageHelicopterDirection.heliID);
        if (helicopter != null)
        {
            helicopter.setDirection(messageHelicopterDirection.direction);
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
