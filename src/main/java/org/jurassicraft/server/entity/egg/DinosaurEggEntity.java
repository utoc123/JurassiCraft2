package org.jurassicraft.server.entity.egg;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.JCEntityRegistry;

import java.util.List;

public class DinosaurEggEntity extends Entity implements IEntityAdditionalSpawnData
{

    private DinosaurEntity dinosaur;
    private boolean marine = false;
    private int hatchTime;

    public DinosaurEggEntity(World world, boolean marine, DinosaurEntity dinosaur)
    {
        super(world);
        this.dinosaur = dinosaur;
        this.marine = marine;
    }

    public DinosaurEggEntity(World world)
    {
        super(world);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        if (!this.worldObj.isRemote)
        {
            hatchTime--;
            if (hatchTime == 0)
            {
                hatch();
            }
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
            entity.setPosition(this.posX + 1, this.posY, this.posZ + 1);
            entity.setAge(0);
            entity.setDNAQuality(100);
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
        }
        catch (Exception e)
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