package org.jurassicraft.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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

    public PlacePaddockSignMessage()
    {
    }

    public PlacePaddockSignMessage(EnumFacing facing, BlockPos pos, Dinosaur dino)
    {
        this.dino = JCEntityRegistry.getDinosaurId(dino);
        this.pos = new BlockPos(x, y, z);
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.facing = facing;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientMessage(PlacePaddockSignMessage messagePlacePaddockSign, EntityPlayer entityPlayer)
    {

    }

    @Override
    public void handleServerMessage(PlacePaddockSignMessage messagePlacePaddockSign, EntityPlayer entityPlayer)
    {
        World world = entityPlayer.worldObj;

        EnumFacing side = messagePlacePaddockSign.facing;
        BlockPos pos = messagePlacePaddockSign.pos;

        PaddockSignEntity paddockSign = new PaddockSignEntity(world, pos, side, messagePlacePaddockSign.dino);

        if (entityPlayer.canPlayerEdit(pos, side, entityPlayer.getHeldItem()) && paddockSign.onValidSurface())
        {
            world.spawnEntityInWorld(paddockSign);

            if (!entityPlayer.capabilities.isCreativeMode)
            {
                InventoryPlayer inventory = entityPlayer.inventory;
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
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
        x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();
        dino = buffer.readInt();
        facing = EnumFacing.getFront(buffer.readByte());
        pos = new BlockPos(x, y, z);
    }
}
