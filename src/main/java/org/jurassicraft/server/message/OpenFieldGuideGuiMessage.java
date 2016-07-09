package org.jurassicraft.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class OpenFieldGuideGuiMessage extends AbstractMessage<OpenFieldGuideGuiMessage>
{
    private DinosaurEntity entity;
    private int entityId;
    private int hunger;
    private int thirst;

    public OpenFieldGuideGuiMessage()
    {
    }

    public OpenFieldGuideGuiMessage(DinosaurEntity entity)
    {
        this.entity = entity;
        this.entityId = entity.getEntityId();
    }

    @Override
    public void onClientReceived(Minecraft minecraft, OpenFieldGuideGuiMessage message, EntityPlayer player, MessageContext context)
    {
        Entity entity = player.worldObj.getEntityByID(message.entityId);

        if (entity instanceof DinosaurEntity)
        {
            DinosaurEntity dinosaur = (DinosaurEntity) entity;
            dinosaur.getMetabolism().setEnergy(hunger);
            dinosaur.getMetabolism().setWater(thirst);
            JurassiCraft.PROXY.openFieldGuide(dinosaur);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, OpenFieldGuideGuiMessage message, EntityPlayer player, MessageContext context)
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.entityId = buf.readInt();
        this.hunger = buf.readInt();
        this.thirst = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(entityId);
        buf.writeInt(entity.getMetabolism().getEnergy());
        buf.writeInt(entity.getMetabolism().getWater());
    }
}
