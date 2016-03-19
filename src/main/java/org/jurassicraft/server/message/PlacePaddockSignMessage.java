package org.jurassicraft.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.JCEntityRegistry;
import org.jurassicraft.server.entity.item.PaddockSignEntity;

public class PlacePaddockSignMessage extends AbstractMessage<PlacePaddockSignMessage>
{
    private int dino;
    private BlockPos pos;
    private int x;
    private int y;
    private int z;
    private EnumFacing facing;
    private EnumHand hand;

    public PlacePaddockSignMessage()
    {
    }

    public PlacePaddockSignMessage(EnumHand hand, EnumFacing facing, BlockPos pos, Dinosaur dino)
    {
        this.dino = JCEntityRegistry.getDinosaurId(dino);
        this.pos = new BlockPos(x, y, z);
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.facing = facing;
        this.hand = hand;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientMessage(PlacePaddockSignMessage message, EntityPlayer entityPlayer)
    {

    }

    @Override
    public void handleServerMessage(PlacePaddockSignMessage message, EntityPlayer player)
    {
        World world = player.worldObj;

        EnumFacing side = message.facing;
        BlockPos pos = message.pos;

        PaddockSignEntity paddockSign = new PaddockSignEntity(world, pos, side, message.dino);

        if (player.canPlayerEdit(pos, side, player.getHeldItem(message.hand)) && paddockSign.onValidSurface())
        {
            world.spawnEntityInWorld(paddockSign);

            if (!player.capabilities.isCreativeMode)
            {
                InventoryPlayer inventory = player.inventory;
                inventory.decrStackSize(inventory.currentItem, 1);
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeInt(dino);
        buffer.writeByte((byte) facing.getIndex());
        buffer.writeByte((byte) hand.ordinal());
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
        x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();
        dino = buffer.readInt();
        facing = EnumFacing.getFront(buffer.readByte());
        hand = EnumHand.values()[buffer.readByte()];
        pos = new BlockPos(x, y, z);
    }
}
