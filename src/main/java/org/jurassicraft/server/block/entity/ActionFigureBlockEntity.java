package org.jurassicraft.server.block.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.EntityHandler;

public class ActionFigureBlockEntity extends TileEntity {
    public int dinosaur;
    public DinosaurEntity entity;
    public boolean isMale;

    public void setDinosaur(int dinosaur, boolean isMale) {
        this.dinosaur = dinosaur;
        this.isMale = isMale;
        this.updateEntity();
        this.markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        this.dinosaur = nbt.getInteger("DinosaurId");
        this.isMale = !nbt.hasKey("IsMale") || nbt.getBoolean("IsMale");

        this.updateEntity();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);

        nbt.setInteger("DinosaurId", this.dinosaur);
        nbt.setBoolean("IsMale", this.isMale);

        return nbt;
    }

    public void updateEntity() {
        if (this.world != null) {
            try {
                this.entity = EntityHandler.getDinosaurById(this.dinosaur).getDinosaurClass().getDeclaredConstructor(World.class).newInstance(this.world);
                this.entity.setupActionFigure(this.isMale);
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
