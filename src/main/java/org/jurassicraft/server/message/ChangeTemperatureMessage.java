package org.jurassicraft.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jurassicraft.JurassiCraft;

public class ChangeTemperatureMessage extends AbstractMessage<ChangeTemperatureMessage> {
    private int slot;
    private int temp;
    private int x;
    private int y;
    private int z;
    private BlockPos pos;

    public ChangeTemperatureMessage() {
    }

    public ChangeTemperatureMessage(BlockPos pos, int slot, int temp) {
        this.slot = slot;
        this.temp = temp;
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.pos = new BlockPos(this.x, this.y, this.z);
    }

    @Override
    public void onClientReceived(Minecraft minecraft, ChangeTemperatureMessage message, EntityPlayer player, MessageContext messageContext) {
        IInventory incubator = (IInventory) player.worldObj.getTileEntity(message.pos);
        incubator.setField(message.slot + 10, message.temp);
    }

    @Override
    public void onServerReceived(MinecraftServer server, ChangeTemperatureMessage message, EntityPlayer player, MessageContext messageContext) {
        IInventory incubator = (IInventory) player.worldObj.getTileEntity(message.pos);
        incubator.setField(message.slot + 10, message.temp);
        JurassiCraft.NETWORK_WRAPPER.sendToAll(new ChangeTemperatureMessage(message.pos, message.slot, message.temp));
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(this.slot);
        buffer.writeInt(this.temp);
        buffer.writeInt(this.x);
        buffer.writeInt(this.y);
        buffer.writeInt(this.z);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        this.slot = buffer.readInt();
        this.temp = buffer.readInt();
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();

        this.pos = new BlockPos(this.x, this.y, this.z);
    }
}
