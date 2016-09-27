package org.jurassicraft.server.entity.item;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.item.ItemHandler;

import java.util.List;

public class DinosaurEggEntity extends Entity implements IEntityAdditionalSpawnData {
    private Dinosaur dinosaur;
    private int dnaQuality;
    private String genetics;
    private int hatchTime;

    public DinosaurEggEntity(World world, Dinosaur dinosaur, int dnaQuality, String genetics) {
        this(world);
        this.dinosaur = dinosaur;
        this.dnaQuality = dnaQuality;
        this.genetics = genetics;
    }

    public DinosaurEggEntity(World world) {
        super(world);
        this.setSize(0.3F, 0.5F);
        this.hatchTime = this.randomWithRange(200, 300);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (!this.worldObj.isRemote) {
            this.hatchTime--;

            if (this.hatchTime <= 0) {
                this.hatch();
            }

            if (!this.onGround) {
                this.motionY -= 0.035D;
            }

            this.motionX *= 0.85;
            this.motionY *= 0.85;
            this.motionZ *= 0.85;

            this.moveEntity(this.motionX, this.motionY, this.motionZ);
        }
    }

    @Override
    public void entityInit() {
    }

    @Override
    public boolean canBePushed() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, ItemStack stack, EnumHand hand) {
        if (this.dinosaur != null && !this.worldObj.isRemote) {
            ItemStack eggStack = new ItemStack(ItemHandler.EGG, 1, EntityHandler.getDinosaurId(this.dinosaur));
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("DNAQuality", this.dnaQuality);
            nbt.setString("Genetics", this.genetics);
            eggStack.setTagCompound(nbt);
            this.entityDropItem(eggStack, 0.1F);
            this.setDead();
        }

        return true;
    }

    public void hatch() {
        if (!this.dinosaur.isMarineCreature() && this.isInWater()) {
            this.warnPlayersWithinRadius("An egg is in the water and that animal is not aquatic!");
            this.hatchTime += 1000;
            return;
        }

        if (this.dinosaur.isMarineCreature() && !this.isInWater()) {
            this.warnPlayersWithinRadius("An aquatic animals egg is on land!");
            this.hatchTime += 1000;
            return;
        }

        if (!this.isNextBlockAir(1, 0, 0) || !this.isNextBlockAir(0, 1, 0) || !this.isNextBlockAir(0, 0, 1)) {
            this.warnPlayersWithinRadius("There is not enough space for the egg to hatch!");
            this.hatchTime += 1000;
            return;
        }

        try {
            DinosaurEntity entity = this.dinosaur.getDinosaurClass().getConstructor(World.class).newInstance(this.worldObj);
            entity.setAge(0);
            entity.setDNAQuality(this.dnaQuality);
            entity.setGenetics(this.genetics);
            entity.setPosition(this.posX, this.posY, this.posZ);
            this.worldObj.spawnEntityInWorld(entity);
            entity.playLivingSound();
            this.setDead();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void warnPlayersWithinRadius(String message) {
        List<EntityPlayer> players = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(this.posX - 30, this.posY - 10, this.posZ - 30, this.posX + 30, this.posY + 10, this.posZ + 30));
        for (EntityPlayer player : players) {
            player.addChatMessage(new TextComponentString(message));
        }
    }

    public int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }

    public Boolean isNextBlockAir(int xChange, int yChange, int zChange) {
        int blockX = MathHelper.floor_double(this.posX) + xChange;
        int blockY = MathHelper.floor_double(this.getEntityBoundingBox().minY) + yChange;
        int blockZ = MathHelper.floor_double(this.posZ) + zChange;
        return this.worldObj.isAirBlock(new BlockPos(blockX, blockY, blockZ));
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.hatchTime = compound.getInteger("HatchTime");
        this.dinosaur = EntityHandler.getDinosaurById(compound.getInteger("Dinosaur"));
        this.dnaQuality = compound.getByte("DNAQuality");
        this.genetics = compound.getString("Genetics");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("Dinosaur", EntityHandler.getDinosaurId(this.dinosaur));
        compound.setInteger("HatchTime", this.hatchTime);
        compound.setByte("DNAQuality", (byte) this.dnaQuality);
        compound.setString("Genetics", this.genetics);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(EntityHandler.getDinosaurId(this.dinosaur));
        buffer.writeByte(this.dnaQuality);
        ByteBufUtils.writeUTF8String(buffer, this.genetics);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        this.dinosaur = EntityHandler.getDinosaurById(additionalData.readInt());
        this.dnaQuality = additionalData.readByte();
        this.genetics = ByteBufUtils.readUTF8String(additionalData);
    }

    public Dinosaur getDinosaur() {
        return this.dinosaur;
    }
}