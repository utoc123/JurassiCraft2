package org.jurassicraft.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jurassicraft.JurassiCraft;

public class ChangeTemperatureMessage extends AbstractMessage<ChangeTemperatureMessage> {
    private int slot;
    private int temp;
    private BlockPos pos;

    public ChangeTemperatureMessage() {
    }

    public ChangeTemperatureMessage(BlockPos pos, int slot, int temp) {
        this.slot = slot;
        this.temp = temp;
        this.pos = pos;
    }

    @Override
    public void onClientReceived(Minecraft minecraft, ChangeTemperatureMessage message, EntityPlayer player, MessageContext messageContext) {
        TileEntity tile = player.worldObj.getTileEntity(message.pos);
        if (tile instanceof IInventory) {
            IInventory incubator = (IInventory) tile;
            incubator.setField(message.slot + 10, message.temp);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, ChangeTemperatureMessage message, EntityPlayer player, MessageContext messageContext) {
        TileEntity tile = player.worldObj.getTileEntity(message.pos);
        if (tile instanceof IInventory) {
            IInventory incubator = (IInventory) tile;
            incubator.setField(message.slot + 10, message.temp);
            JurassiCraft.NETWORK_WRAPPER.sendToAll(new ChangeTemperatureMessage(message.pos, message.slot, message.temp));
        }
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(this.slot);
        buffer.writeInt(this.temp);
        buffer.writeLong(this.pos.toLong());
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        this.slot = buffer.readInt();
        this.temp = buffer.readInt();
        this.pos = BlockPos.fromLong(buffer.readLong());
    }
}
