package org.jurassicraft.server.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.EntityHandler;

public class ActionFigureTile extends TileEntity {
    public int dinosaur;
    public DinosaurEntity entity;

    public void setDinosaur(int dinosaur) {
        this.dinosaur = dinosaur;

        this.updateEntity();

        this.markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        this.dinosaur = nbt.getInteger("DinosaurId");

        this.updateEntity();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);

        nbt.setInteger("DinosaurId", this.dinosaur);

        return nbt;
    }

    public void updateEntity() {
        if (this.worldObj != null) {
            try {
                this.entity = EntityHandler.getDinosaurById(this.dinosaur).getDinosaurClass().getDeclaredConstructor(World.class).newInstance(this.worldObj);
                this.entity.applySettingsForActionFigure();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
    }
}
