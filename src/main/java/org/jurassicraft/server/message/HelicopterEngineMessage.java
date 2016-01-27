package org.jurassicraft.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.vehicles.helicopter.HelicopterBaseEntity;

public class HelicopterEngineMessage extends AbstractMessage<HelicopterEngineMessage>
{
    private int heliID;
    private boolean engineState;

    public HelicopterEngineMessage()
    {
    }

    public HelicopterEngineMessage(int heliID, boolean engineState)
    {
        this.heliID = heliID;
        this.engineState = engineState;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientMessage(HelicopterEngineMessage messageHelicopterEngine, EntityPlayer entityPlayer)
    {
        HelicopterBaseEntity helicopter = HelicopterMessages.getHeli(entityPlayer.worldObj, messageHelicopterEngine.heliID);
        if (helicopter != null)
        {
            helicopter.setEngineRunning(messageHelicopterEngine.engineState);
        }
    }

    @Override
    public void handleServerMessage(HelicopterEngineMessage messageHelicopterEngine, EntityPlayer entityPlayer)
    {
        HelicopterBaseEntity helicopter = HelicopterMessages.getHeli(entityPlayer.worldObj, messageHelicopterEngine.heliID);
        if (helicopter != null)
        {
            helicopter.setEngineRunning(messageHelicopterEngine.engineState);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        heliID = buf.readInt();
        engineState = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(heliID);
        buf.writeBoolean(engineState);
    }
}
