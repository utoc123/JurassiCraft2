package org.jurassicraft.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.entity.ai.Herd;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class OpenFieldGuideGuiMessage extends AbstractMessage<OpenFieldGuideGuiMessage> {
    private DinosaurEntity entity;
    private int entityId;
    private int hunger;
    private int thirst;
    private boolean flocking;
    private boolean fleeing;

    public OpenFieldGuideGuiMessage() {
    }

    public OpenFieldGuideGuiMessage(DinosaurEntity entity) {
        this.entity = entity;
        this.entityId = entity.getEntityId();
    }

    @Override
    public void onClientReceived(Minecraft minecraft, OpenFieldGuideGuiMessage message, EntityPlayer player, MessageContext context) {
        Entity entity = player.worldObj.getEntityByID(message.entityId);

        if (entity instanceof DinosaurEntity) {
            DinosaurEntity dinosaur = (DinosaurEntity) entity;
            dinosaur.getMetabolism().setEnergy(message.hunger);
            dinosaur.getMetabolism().setWater(message.thirst);
            dinosaur.setFieldGuideFlocking(message.flocking);
            dinosaur.setFieldGuideFleeing(message.fleeing);

            JurassiCraft.PROXY.openFieldGuide(dinosaur);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, OpenFieldGuideGuiMessage message, EntityPlayer player, MessageContext context) {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
        this.hunger = buf.readInt();
        this.thirst = buf.readInt();
        this.flocking = buf.readBoolean();
        this.fleeing = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeInt(this.entity.getMetabolism().getEnergy());
        buf.writeInt(this.entity.getMetabolism().getWater());
        buf.writeBoolean(this.entity.herd != null && this.entity.herd.state == Herd.State.MOVING);
        buf.writeBoolean(this.entity.herd != null && this.entity.herd.fleeing);
    }
}
