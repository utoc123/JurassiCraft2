package org.jurassicraft.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.entity.dinosaur.MicroraptorEntity;

public class MicroraptorDismountMessage extends AbstractMessage<MicroraptorDismountMessage> {

    public int entityId;
    public byte controlState;

    public MicroraptorDismountMessage(int entityId, byte controlState) {
        this.entityId = entityId;
        this.controlState = controlState;
    }

    public MicroraptorDismountMessage() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        controlState = buf.readByte();

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeByte(controlState);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, MicroraptorDismountMessage message, EntityPlayer player, MessageContext messageContext) {
    }

    @Override
    public void onServerReceived(MinecraftServer server, MicroraptorDismountMessage message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.entityId);
        if (entity instanceof MicroraptorEntity) {
            MicroraptorEntity microraptor = (MicroraptorEntity) entity;
            if (microraptor.isOwner(player)) {
                microraptor.setControlState(message.controlState);
            }
        }
    }
}