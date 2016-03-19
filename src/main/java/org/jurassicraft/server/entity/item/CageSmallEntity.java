package org.jurassicraft.server.entity.item;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.item.JCItemRegistry;

public class CageSmallEntity extends Entity implements IEntityAdditionalSpawnData
{
    private DinosaurEntity entity;
    private boolean marine;

    private static DataParameter<Integer> DATA_WATCHER_ENTITY_ID = EntityDataManager.createKey(CageSmallEntity.class, DataSerializers.VARINT);
    private static DataParameter<Integer> DATA_WATCHER_AGE = EntityDataManager.createKey(CageSmallEntity.class, DataSerializers.VARINT);
    private static DataParameter<Integer> DATA_WATCHER_DNA_QUALITY = EntityDataManager.createKey(CageSmallEntity.class, DataSerializers.VARINT);
    private static DataParameter<String> DATA_WATCHER_GENETICS = EntityDataManager.createKey(CageSmallEntity.class, DataSerializers.STRING);
    private static DataParameter<Boolean> DATA_WATCHER_GENDER = EntityDataManager.createKey(CageSmallEntity.class, DataSerializers.BOOLEAN);

    public CageSmallEntity(World world)
    {
        super(world);
        this.setSize(1.0F, 1.0F);
    }

    public CageSmallEntity(World world, boolean marine)
    {
        this(world);
        this.marine = marine;
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity entity)
    {
        return entity.getEntityBoundingBox();
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return true;
    }

    @Override
    public boolean canBePushed()
    {
        return false;
    }

    @Override
    protected void entityInit()
    {
        this.dataWatcher.register(DATA_WATCHER_ENTITY_ID, -1);
        this.dataWatcher.register(DATA_WATCHER_AGE, 0);
        this.dataWatcher.register(DATA_WATCHER_DNA_QUALITY, 0);
        this.dataWatcher.register(DATA_WATCHER_GENETICS, "");
        this.dataWatcher.register(DATA_WATCHER_GENDER, false);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (worldObj.isRemote)
        {
            int id = dataWatcher.get(DATA_WATCHER_ENTITY_ID);

            if (id != -1)
            {
                entity = (DinosaurEntity) EntityList.createEntityByID(id, worldObj);

                if (entity != null)
                {
                    entity.setMale(dataWatcher.get(DATA_WATCHER_GENDER));
                    entity.setAge(dataWatcher.get(DATA_WATCHER_AGE));
                    entity.setDNAQuality(dataWatcher.get(DATA_WATCHER_DNA_QUALITY));
                    entity.setGenetics(dataWatcher.get(DATA_WATCHER_GENETICS));
                }
            }
            else
            {
                entity = null;
            }
        }

        if (entity != null)
        {
            entity.ticksExisted = this.ticksExisted;
            entity.onUpdate();
        }

        if (!worldObj.isRemote)
        {
            if (entity != null)
            {
                dataWatcher.set(DATA_WATCHER_ENTITY_ID, EntityList.getEntityID(entity));
                dataWatcher.set(DATA_WATCHER_AGE, entity.getDinosaurAge());
                dataWatcher.set(DATA_WATCHER_DNA_QUALITY, entity.getDNAQuality());
                dataWatcher.set(DATA_WATCHER_GENETICS, entity.getGenetics().toString());
                dataWatcher.set(DATA_WATCHER_GENDER, entity.isMale());
            }
            else
            {
                dataWatcher.set(DATA_WATCHER_ENTITY_ID, -1);
            }
        }
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, ItemStack stack, EnumHand hand)
    {
        if (entity != null && !worldObj.isRemote)
        {
            entity.setPosition(posX, posY, posZ);
            entity.prevPosX = prevPosX;
            entity.prevPosY = prevPosY;
            entity.prevPosZ = prevPosZ;
            entity.motionX = 0;
            entity.motionY = 0;
            entity.motionZ = 0;
            entity.fallDistance = 0;
            entity.setHealth(entity.getMaxHealth());
            entity.deathTime = 0;
            entity.hurtTime = 0;
            entity.ticksExisted = 0;

            worldObj.spawnEntityInWorld(entity);

            this.setDead();
            this.entityDropItem(new ItemStack(JCItemRegistry.cage_small, 1, marine ? 1 : 0), 0.5F);
        }

        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        this.setDead();

        if (!worldObj.isRemote)
        {
            ItemStack stack = new ItemStack(JCItemRegistry.cage_small, 1, marine ? 1 : 0);

            if (entity != null)
            {
                NBTTagCompound nbt = new NBTTagCompound();

                this.writeEntityToNBT(nbt);

                stack.setTagCompound(nbt);
            }

            this.entityDropItem(stack, 0.5F);
        }

        return true;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        int cagedId = tagCompund.getInteger("CagedID");

        if (cagedId != -1)
        {
            entity = (DinosaurEntity) EntityList.createEntityByID(cagedId, worldObj);
            entity.readFromNBT(tagCompund.getCompoundTag("Entity"));
        }

        marine = tagCompund.getBoolean("Marine");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        if (entity != null)
        {
            int entityId = EntityList.getEntityID(entity);

            NBTTagCompound nbt = new NBTTagCompound();
            entity.writeToNBT(nbt);

            tagCompound.setTag("Entity", nbt);
            tagCompound.setInteger("CagedID", entityId);
        }
        else
        {
            tagCompound.setInteger("CagedID", -1);
        }

        tagCompound.setBoolean("Marine", marine);
    }

    public void setEntity(int entity)
    {
        this.entity = (DinosaurEntity) EntityList.createEntityByID(entity, worldObj);
    }

    public void setEntity(Entity entity)
    {
        this.entity = (DinosaurEntity) entity;

        NBTTagCompound nbt = new NBTTagCompound();
        entity.writeToNBT(nbt);

        setEntityData(nbt);
    }

    public void setEntityData(NBTTagCompound entityData)
    {
        if (entity != null)
        {
            entity.readFromNBT(entityData);
        }
    }

    public Entity getEntity()
    {
        return entity;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeBoolean(marine);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData)
    {
        marine = additionalData.readBoolean();
    }

    public boolean isMarine()
    {
        return marine;
    }
}
