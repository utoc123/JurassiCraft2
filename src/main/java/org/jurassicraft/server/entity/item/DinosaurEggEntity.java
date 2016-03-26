package org.jurassicraft.server.entity.item;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.JCEntityRegistry;
import org.jurassicraft.server.item.JCItemRegistry;

import java.util.List;

public class DinosaurEggEntity extends Entity implements IEntityAdditionalSpawnData
{

    private DinosaurEntity dinosaur;
    private boolean marine;
    private int hatchTime;

    public DinosaurEggEntity(World world, DinosaurEntity dinosaur)
    {
        super(world);
        this.dinosaur = dinosaur;
        this.marine = dinosaur.getDinosaur().isMarineAnimal();
    }

    public DinosaurEggEntity(World world)
    {
        super(world);
        this.setSize(.3F, .5F);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        if (!this.worldObj.isRemote)
        {
            hatchTime--;
            if (hatchTime == 0)
                hatch();
            if (!this.onGround)
            {
                this.motionY -= 0.035D;
            }
            this.moveEntity(0, this.motionY, 0);
        }
    }

    @Override
    public void entityInit()
    {
        hatchTime = randomWithRange(200, 300);
    }

    @Override
    public boolean canBePushed()
    {
        return true;
    }

    public boolean isMarine()
    {
        return marine;
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return true;
    }

    @Override
    public boolean interactFirst(EntityPlayer playerIn)
    {
        if (dinosaur != null && !worldObj.isRemote)
        {
            this.entityDropItem(new ItemStack(JCItemRegistry.egg, 1, JCEntityRegistry.getDinosaurId(dinosaur.getDinosaur())), 0.5F);
        }
        return true;
    }

    public void hatch()
    {
        if (!isMarine() && dinosaur.isInWater())
        {
            warnPlayersWithinRadius("An egg is in the water and that animal is not aquatic!");
            hatchTime += 1000;
            return;
        }
        if (isMarine() && !dinosaur.isInWater())
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
            DinosaurEntity entity = dinosaur.getClass().getConstructor(World.class).newInstance(worldObj);
            entity.setAge(0);
            entity.setDNAQuality(100);
            entity.setHealth(entity.getMaxHealth());
            entity.setPosition(posX, posY, posZ);
            entity.prevPosX = prevPosX;
            entity.prevPosY = prevPosY;
            entity.prevPosZ = prevPosZ;
            entity.motionX = 0;
            entity.motionY = 0;
            entity.motionZ = 0;
            entity.fallDistance = 0;
            entity.deathTime = 0;
            entity.hurtTime = 0;
            entity.ticksExisted = 0;
            worldObj.spawnEntityInWorld(entity);
            entity.playLivingSound();
            this.setDead();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void warnPlayersWithinRadius(String message)
    {
        List<EntityPlayer> players = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.fromBounds(this.posX - 30, this.posY - 10, this.posZ - 30, this.posX + 30, this.posY + 10, this.posZ + 30));
        for (EntityPlayer player : players)
        {
            player.addChatMessage(new ChatComponentText(message));
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
        return this.worldObj.getBlockState(new BlockPos(blockX, blockY, blockZ)).getBlock().isAir(this.worldObj, new BlockPos(blockX, blockY, blockZ));
    }

    public int getHatchTime()
    {
        return hatchTime;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        marine = compound.getBoolean("Marine");
        hatchTime = compound.getInteger("HatchTime");
        try
        {
            dinosaur = JCEntityRegistry.getDinosaurById(compound.getInteger("Dinosaur")).getDinosaurClass().getConstructor(World.class).newInstance(worldObj);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setBoolean("Marine", marine);
        compound.setInteger("Dinosaur", JCEntityRegistry.getDinosaurId(dinosaur.getDinosaur()));
        compound.setInteger("HatchTime", hatchTime);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeInt(EntityList.getEntityID(dinosaur));
    }

    @Override
    public void readSpawnData(ByteBuf additionalData)
    {
        dinosaur = (DinosaurEntity) EntityList.createEntityByID(additionalData.readInt(), worldObj);
    }
}