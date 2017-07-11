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
    public boolean isSkeleton;
    private int rotation;

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
        this.rotation = nbt.getInteger("Rotation");
        this.isMale = !nbt.hasKey("IsMale") || nbt.getBoolean("IsMale");
        this.isSkeleton = nbt.getBoolean("IsSkeleton");

        this.updateEntity();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);

        nbt.setInteger("DinosaurId", this.dinosaur);
        nbt.setInteger("Rotation", this.rotation);
        nbt.setBoolean("IsMale", this.isMale);
        nbt.setBoolean("IsSkeleton", this.isSkeleton);

        return nbt;
    }

    public void updateEntity() {
        if (this.world != null) {
            try {
                this.entity = EntityHandler.getDinosaurById(this.dinosaur).getDinosaurClass().getDeclaredConstructor(World.class).newInstance(this.world);
                this.entity.setupActionFigure(this.isMale);
                this.entity.setSkeleton(this.isSkeleton);
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

    public int getRot() {
        return rotation;
    }

    public void setRot(int rotation) {
        this.markDirty();
        this.rotation = rotation;
    }
    
    public DinosaurEntity getEntity(){
        if(entity == null){
            this.updateEntity();
        }
        return entity;
    }
    
}
