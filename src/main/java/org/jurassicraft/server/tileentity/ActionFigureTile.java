package org.jurassicraft.server.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.JCEntityRegistry;

public class ActionFigureTile extends TileEntity
{
    public int dinosaur;
    public DinosaurEntity entity;

    public void setDinosaur(int dinosaur)
    {
        this.dinosaur = dinosaur;

        updateEntity();

        this.markDirty();
    }

    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        dinosaur = nbt.getInteger("DinosaurId");

        updateEntity();
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        nbt.setInteger("DinosaurId", dinosaur);
    }

    public void updateEntity()
    {
        if (worldObj != null)
        {
            try
            {
                entity = JCEntityRegistry.getDinosaurById(dinosaur).getDinosaurClass().getDeclaredConstructor(World.class).newInstance(worldObj);
                entity.applySettingsForActionFigure();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound compound = new NBTTagCompound();
        this.writeToNBT(compound);
        return new SPacketUpdateTileEntity(this.pos, this.getBlockMetadata(), compound);
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet)
    {
        NBTTagCompound compound = packet.getNbtCompound();
        this.readFromNBT(compound);
    }
}
