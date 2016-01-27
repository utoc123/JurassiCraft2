package org.jurassicraft.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.entity.data.JCPlayerData;

public class SyncPaleoPadMessage extends AbstractMessage<SyncPaleoPadMessage>
{
    private NBTTagCompound nbt;

    public SyncPaleoPadMessage()
    {
    }

    public SyncPaleoPadMessage(EntityPlayer player)
    {
        nbt = new NBTTagCompound();
        JCPlayerData.getPlayerData(player).saveNBTData(nbt);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientMessage(SyncPaleoPadMessage messageSyncPaleoPad, EntityPlayer entityPlayer)
    {
        JCPlayerData.setPlayerData(null, messageSyncPaleoPad.nbt);
    }

    @Override
    public void handleServerMessage(SyncPaleoPadMessage messageSyncPaleoPad, EntityPlayer entityPlayer)
    {
        JCPlayerData.setPlayerData(entityPlayer, messageSyncPaleoPad.nbt);
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {
        ByteBufUtils.writeTag(buffer, nbt);
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
        nbt = ByteBufUtils.readTag(buffer);
    }
}
