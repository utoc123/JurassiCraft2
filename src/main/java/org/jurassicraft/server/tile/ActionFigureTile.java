package org.jurassicraft.server.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.EntityHandler;

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

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        dinosaur = nbt.getInteger("DinosaurId");

        updateEntity();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt = super.writeToNBT(nbt);

        nbt.setInteger("DinosaurId", dinosaur);

        return nbt;
    }

    public void updateEntity()
    {
        if (worldObj != null)
        {
            try
            {
                entity = EntityHandler.getDinosaurById(dinosaur).getDinosaurClass().getDeclaredConstructor(World.class).newInstance(worldObj);
                entity.applySettingsForActionFigure();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound = this.writeToNBT(compound);
        return new SPacketUpdateTileEntity(this.pos, this.getBlockMetadata(), compound);
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet)
    {
        this.readFromNBT(packet.getNbtCompound());
    }
}
