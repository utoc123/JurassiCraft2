package org.jurassicraft.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
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
    public void onClientReceived(Minecraft client, SyncPaleoPadMessage message, EntityPlayer player, MessageContext messageContext)
    {
        JCPlayerData.setPlayerData(null, message.nbt);
    }

    @Override
    public void onServerReceived(MinecraftServer server, SyncPaleoPadMessage message, EntityPlayer player, MessageContext messageContext)
    {
        JCPlayerData.setPlayerData(player, message.nbt);
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
