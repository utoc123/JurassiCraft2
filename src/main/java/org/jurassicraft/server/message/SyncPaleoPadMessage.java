package org.jurassicraft.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.capability.PlayerDataCapabilityImplementation;

public class SyncPaleoPadMessage extends AbstractMessage<SyncPaleoPadMessage>
{
    private NBTTagCompound nbt;

    public SyncPaleoPadMessage()
    {
    }

    public SyncPaleoPadMessage(EntityPlayer player)
    {
        nbt = new NBTTagCompound();
        PlayerDataCapabilityImplementation.get(player).save(nbt);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientMessage(SyncPaleoPadMessage message, EntityPlayer player)
    {
        PlayerDataCapabilityImplementation.get(player).load(message.nbt);
    }

    @Override
    public void handleServerMessage(SyncPaleoPadMessage message, EntityPlayer player)
    {
        PlayerDataCapabilityImplementation.get(player).load(message.nbt);
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
