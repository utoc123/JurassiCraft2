package org.jurassicraft.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.vehicles.helicopter.HelicopterBaseEntity;
import org.jurassicraft.server.vehicles.helicopter.modules.EnumModulePosition;
import org.jurassicraft.server.vehicles.helicopter.modules.HelicopterModuleSpot;

public class HelicopterModulesMessage extends AbstractMessage<HelicopterModulesMessage>
{
    private NBTTagCompound compound;
    private EnumModulePosition pos;
    private HelicopterModuleSpot spot;
    private int heliID;

    public HelicopterModulesMessage()
    {
    }

    public HelicopterModulesMessage(int heliID, EnumModulePosition pos, HelicopterModuleSpot spot)
    {
        this.heliID = heliID;
        this.pos = pos;
        this.spot = spot;
        compound = new NBTTagCompound();
        spot.writeToNBT(compound);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientMessage(HelicopterModulesMessage messageHelicopterModules, EntityPlayer entityPlayer)
    {
        HelicopterBaseEntity helicopter = HelicopterMessages.getHeli(entityPlayer.worldObj, messageHelicopterModules.heliID);
        if (helicopter != null)
        {
            System.out.println(messageHelicopterModules.heliID);
            HelicopterModuleSpot spot = helicopter.getModuleSpot(messageHelicopterModules.pos);
            spot.readFromNBT(messageHelicopterModules.compound);
            System.out.println(messageHelicopterModules.compound);
        }
    }

    @Override
    public void handleServerMessage(HelicopterModulesMessage messageHelicopterModules, EntityPlayer entityPlayer)
    {
        HelicopterBaseEntity helicopter = HelicopterMessages.getHeli(entityPlayer.worldObj, messageHelicopterModules.heliID);
        if (helicopter != null)
        {
            System.out.println(messageHelicopterModules.heliID);
            HelicopterModuleSpot spot = helicopter.getModuleSpot(messageHelicopterModules.pos);
            spot.readFromNBT(messageHelicopterModules.compound);
            System.out.println(messageHelicopterModules.compound);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        heliID = buf.readInt();
        pos = EnumModulePosition.values()[buf.readInt()];
        compound = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(heliID);
        buf.writeInt(pos.ordinal());
        ByteBufUtils.writeTag(buf, compound);
    }
}
