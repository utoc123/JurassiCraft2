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
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.EntityHandler;
import org.jurassicraft.server.item.ItemHandler;

import java.util.List;

public class DinosaurEggEntity extends Entity implements IEntityAdditionalSpawnData
{
    private Dinosaur dinosaur;
    private int dnaQuality;
    private String genetics;
    private int hatchTime;

    public DinosaurEggEntity(World world, Dinosaur dinosaur, int dnaQuality, String genetics)
    {
        this(world);
        this.dinosaur = dinosaur;
        this.dnaQuality = dnaQuality;
        this.genetics = genetics;
    }

    public DinosaurEggEntity(World world)
    {
        super(world);
        this.setSize(0.3F, 0.5F);
        this.hatchTime = randomWithRange(200, 300);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (!this.worldObj.isRemote)
        {
            hatchTime--;

            if (hatchTime <= 0)
            {
                hatch();
            }

            if (!onGround)
            {
                this.motionY -= 0.035D;
            }

            motionX *= 0.85;
            motionY *= 0.85;
            motionZ *= 0.85;

            this.moveEntity(motionX, motionY, motionZ);
        }
    }

    @Override
    public void entityInit()
    {
    }

    @Override
    public boolean canBePushed()
    {
        return true;
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return true;
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, ItemStack stack, EnumHand hand)
    {
        if (dinosaur != null && !worldObj.isRemote)
        {
            ItemStack eggStack = new ItemStack(ItemHandler.EGG, 1, EntityHandler.getDinosaurId(dinosaur));
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("DNAQuality", dnaQuality);
            nbt.setString("Genetics", genetics);
            eggStack.setTagCompound(nbt);
            this.entityDropItem(eggStack, 0.1F);
            this.setDead();
        }

        return true;
    }

    public void hatch()
    {
        if (!dinosaur.isMarineAnimal() && isInWater())
        {
            warnPlayersWithinRadius("An egg is in the water and that animal is not aquatic!");
            hatchTime += 1000;
            return;
        }

        if (dinosaur.isMarineAnimal() && !isInWater())
        {
            warnPlayersWithinRadius("An aquatic animals egg is on land!");
            hatchTime += 1000;
            return;
        }

        if (!isNextBlockAir(1, 0, 0) || !isNextBlockAir(0, 1, 0) || !isNextBlockAir(0, 0, 1))
        {
            warnPlayersWithinRadius("There is not enough space for the egg to hatch!");
            hatchTime += 1000;
            return;
        }

        try
        {
            DinosaurEntity entity = dinosaur.getDinosaurClass().getConstructor(World.class).newInstance(worldObj);
            entity.setAge(0);
            entity.setDNAQuality(dnaQuality);
            entity.setGenetics(genetics);
            entity.setPosition(posX, posY, posZ);
            worldObj.spawnEntityInWorld(entity);
            entity.playLivingSound();
            this.setDead();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void warnPlayersWithinRadius(String message)
    {
        List<EntityPlayer> players = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(this.posX - 30, this.posY - 10, this.posZ - 30, this.posX + 30, this.posY + 10, this.posZ + 30));
        for (EntityPlayer player : players)
        {
            player.addChatMessage(new TextComponentString(message));
        }
    }

    public int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }

    public Boolean isNextBlockAir(int xChange, int yChange, int zChange)
    {
        int blockX = MathHelper.floor_double(this.posX) + xChange;
        int blockY = MathHelper.floor_double(this.getEntityBoundingBox().minY) + yChange;
        int blockZ = MathHelper.floor_double(this.posZ) + zChange;
        return this.worldObj.isAirBlock(new BlockPos(blockX, blockY, blockZ));
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        hatchTime = compound.getInteger("HatchTime");
        dinosaur = EntityHandler.getDinosaurById(compound.getInteger("Dinosaur"));
        dnaQuality = compound.getByte("DNAQuality");
        genetics = compound.getString("Genetics");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setInteger("Dinosaur", EntityHandler.getDinosaurId(dinosaur));
        compound.setInteger("HatchTime", hatchTime);
        compound.setByte("DNAQuality", (byte) dnaQuality);
        compound.setString("Genetics", genetics);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeInt(EntityHandler.getDinosaurId(dinosaur));
        buffer.writeByte(dnaQuality);
        ByteBufUtils.writeUTF8String(buffer, genetics);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData)
    {
        dinosaur = EntityHandler.getDinosaurById(additionalData.readInt());
        dnaQuality = additionalData.readByte();
        genetics = ByteBufUtils.readUTF8String(additionalData);
    }

    public Dinosaur getDinosaur()
    {
        return dinosaur;
    }
}