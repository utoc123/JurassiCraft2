package org.jurassicraft.server.entity.base;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.client.model.tools.ChainBuffer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.animation.Animations;
import org.jurassicraft.server.damagesource.DinosaurEntityDamageSource;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.ai.HerdEntityAI;
import org.jurassicraft.server.entity.ai.MateEntityAI;
import org.jurassicraft.server.entity.ai.SleepEntityAI;
import org.jurassicraft.server.entity.ai.animations.CallAnimationAI;
import org.jurassicraft.server.entity.ai.animations.HeadCockAnimationAI;
import org.jurassicraft.server.entity.ai.animations.LookAnimationAI;
import org.jurassicraft.server.entity.ai.metabolism.DrinkEntityAI;
import org.jurassicraft.server.entity.ai.metabolism.EatFoodItemEntityAI;
import org.jurassicraft.server.entity.ai.metabolism.FindPlantEntityAI;
import org.jurassicraft.server.genetics.GeneticsHelper;
import org.jurassicraft.server.item.BluePrintItem;
import org.jurassicraft.server.item.ItemHandler;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public abstract class DinosaurEntity extends EntityCreature implements IEntityAdditionalSpawnData, IAnimatedEntity
{
    protected Dinosaur dinosaur;

    protected int dinosaurAge;
    protected int prevAge;
    private int growthSpeedOffset;

    private boolean isCarcass;
    private int carcassHealth;

    private boolean isHealthy;

    private String genetics;
    private int geneticsQuality;
    private boolean isMale;

    // For animation AI system
    private Animation animation;
    private int animTick;

    private boolean hasTracker;

    @SideOnly(Side.CLIENT)
    public ChainBuffer tailBuffer;

    private UUID owner;

    private final InventoryDinosaur inventory;

    private static final int WATCHER_IS_CARCASS = 25;
    private static final int WATCHER_AGE = 26;
    private static final int WATCHER_GROWTH_OFFSET = 27;
    private static final int WATCHER_IS_SLEEPING = 28;
    private static final int WATCHER_HAS_TRACKER = 29;

    private final MetabolismContainer metabolism;

    private final PandorasBox condition;

    private boolean isSleeping;
    private int stayAwakeTime;

    private static final Logger LOGGER = LogManager.getLogger();

    private int rareVariant;

    private boolean useInertialTweens;

    protected EntityAITasks animationTasks;

    public DinosaurEntity(World world)
    {
        super(world);

        this.animationTasks = new EntityAITasks(world != null ? world.theProfiler : null);

        this.metabolism = new MetabolismContainer(this);
        this.inventory = new InventoryDinosaur(this);
        this.condition = new PandorasBox(this);
        this.tailBuffer = new ChainBuffer();

        // SetupAI
        //tasks.addTask(0, new EscapeBlockEntityAI(this));

        if (!dinosaur.isMarineAnimal())
        {
            this.tasks.addTask(0, new EntityAISwimming(this));
//            this.tasks.addTask(0, new AdvancedSwimEntityAI(this));
        }

        this.animationTasks.addTask(0, new SleepEntityAI(this));

        this.animationTasks.addTask(1, new DrinkEntityAI(this));
        this.animationTasks.addTask(1, new MateEntityAI(this));
        this.animationTasks.addTask(1, new EatFoodItemEntityAI(this));

        if (dinosaur.getDiet().doesEatPlants())
        {
            this.tasks.addTask(1, new FindPlantEntityAI(this));
        }

        this.tasks.addTask(2, new EntityAIWander(this, 0.8F, 60));
        this.tasks.addTask(2, new HerdEntityAI(this));

        this.tasks.addTask(3, new EntityAILookIdle(this));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityLivingBase.class, 6.0F));
        this.animationTasks.addTask(3, new CallAnimationAI(this));
        this.animationTasks.addTask(3, new LookAnimationAI(this));
        this.animationTasks.addTask(3, new HeadCockAnimationAI(this));

        this.setFullyGrown();

        this.genetics = GeneticsHelper.randomGenetics(rand);
        this.isMale = rand.nextBoolean();

        this.animTick = 0;
        this.setAnimation(Animations.IDLE.get());

        int rareVariantCount = dinosaur.getRareVariants().length;

        if (rareVariantCount > 0)
        {
            if (rand.nextInt(100) < 2)
            {
                this.rareVariant = rand.nextInt(rareVariantCount) + 1;
            }
        }

        this.setUseInertialTweens(true);

        this.setHealthy(true);
    }

    public boolean shouldSleep()
    {
        return getDinosaurTime() > getDinosaur().getSleepingSchedule().getAwakeTime() && !this.hasPredators() && !this.metabolism.isDehydrated() && !this.metabolism.isStarving();
    }

    private boolean hasPredators()
    {
        for (EntityLiving predator : this.getEntitiesWithinDistance(EntityLiving.class, 10.0F, 5.0F))
        {
            if (this.getLastAttacker() == predator || predator.getAttackTarget() == this)
            {
                return true;
            }
        }

        return false;
    }

    private <T extends Entity> List<T> getEntitiesWithinDistance(Class<T> entity, double width, double height)
    {
        List<T> entities = this.worldObj.getEntitiesWithinAABB(entity, new AxisAlignedBB(this.posX - width, this.posY - height, this.posZ - width, this.posX + width, this.posY + height, this.posZ + width));
        entities.remove(this);
        return entities;
    }

    public void setSleeping(boolean sleeping)
    {
        this.isSleeping = sleeping;
    }

    public int getDinosaurTime()
    {
        SleepingSchedule sleepingSchedule = getDinosaur().getSleepingSchedule();

        long time = (worldObj.getWorldTime() % 24000) - sleepingSchedule.getWakeUpTime();

        if (time < 0)
        {
            time += 24000;
        }

        return (int) time;
    }

    public abstract int getTailBoxCount();

    public boolean hasTracker()
    {
        return hasTracker;
    }

    public void setHasTracker(boolean hasTracker)
    {
        this.hasTracker = hasTracker;
    }

    public UUID getOwner()
    {
        return owner;
    }

    public void setOwner(EntityPlayer player)
    {
        this.owner = player.getUniqueID();
    }

    @Override
    public boolean attackEntityAsMob(Entity entity)
    {
        this.setAnimation(Animations.ATTACKING.get());

        float damage = (float) getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
        int knockback = 0;

        if (entity instanceof EntityLivingBase)
        {
            damage += EnchantmentHelper.func_152377_a(getHeldItem(), ((EntityLivingBase) entity).getCreatureAttribute());
            knockback += EnchantmentHelper.getKnockbackModifier(this);
        }

        if (entity.attackEntityFrom(new DinosaurEntityDamageSource("mob", this), damage))
        {
            if (knockback > 0)
            {
                entity.addVelocity(-MathHelper.sin(rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F, 0.1D, MathHelper.cos(rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F);
                motionX *= 0.6D;
                motionZ *= 0.6D;
            }

            this.applyEnchantments(this, entity);

            return true;
        }

        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float amount)
    {
        boolean canHarmInCreative = damageSource.canHarmInCreative();

        if (!isCarcass())
        {
            if (getHealth() - amount <= 0.0F)
            {
                if (!canHarmInCreative)
                {
                    this.setHealth(getMaxHealth());
                    this.setCarcass(true);
                    return false;
                }

                return super.attackEntityFrom(damageSource, amount);
            }
            else
            {
                if (getAnimation() == Animations.IDLE.get())
                {
                    this.setAnimation(Animations.INJURED.get());
                }

                if (isSleeping)
                {
                    disturbSleep();
                }

                return super.attackEntityFrom(damageSource, amount);
            }
        }
        else if (!worldObj.isRemote)
        {
            if (canHarmInCreative)
            {
                return super.attackEntityFrom(damageSource, amount);
            }

            if (damageSource != DamageSource.drown)
            {
                carcassHealth--;

                if (!dead && carcassHealth >= 0 && worldObj.getGameRules().getBoolean("doMobLoot"))
                {
                    dropMeat(damageSource.getEntity());
                }

                if (carcassHealth < 0)
                {
                    this.onDeath(damageSource);
                    this.setDead();
                }
            }
        }

        return false;
    }

    private void dropMeat(Entity attacker)
    {
        int fortune = 0;

        if (attacker instanceof EntityLivingBase)
        {
            fortune = EnchantmentHelper.getLootingModifier((EntityLivingBase) attacker);
        }

        int count = rand.nextInt(2) + 1 + fortune;

        boolean burning = isBurning();

        for (int i = 0; i < count; ++i)
        {
            int meta = EntityHandler.INSTANCE.getDinosaurId(dinosaur);

            if (burning)
            {
                entityDropItem(new ItemStack(ItemHandler.INSTANCE.dino_steak, 1, meta), 0.0F);
            }
            else
            {
                dropStackWithGenetics(new ItemStack(ItemHandler.INSTANCE.dino_meat, 1, meta));
            }
        }
    }

    @Override
    public EntityItem entityDropItem(ItemStack stack, float offsetY)
    {
        if (stack.stackSize != 0 && stack.getItem() != null)
        {
            Random rand = new Random();

            EntityItem item = new EntityItem(this.worldObj, this.posX + ((rand.nextFloat() * width) - width / 2), this.posY + (double) offsetY, this.posZ + ((rand.nextFloat() * width) - width / 2), stack);
            item.setDefaultPickupDelay();

            if (captureDrops)
            {
                this.capturedDrops.add(item);
            }
            else
            {
                this.worldObj.spawnEntityInWorld(item);
            }

            return item;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void knockBack(Entity entity, float p_70653_2_, double motionX, double motionZ)
    {
        if (rand.nextDouble() >= getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue())
        {
            this.isAirBorne = true;
            float distance = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
            float multiplier = 0.4F;
            this.motionX /= 2.0D;
            this.motionZ /= 2.0D;
            this.motionX -= motionX / distance * multiplier;
            this.motionZ -= motionZ / distance * multiplier;

            // TODO
            // We should make knockback bigger and into air if dino is much smaller than attacking dino
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void performHurtAnimation()
    {
        this.setAnimation(Animations.INJURED.get());
    }

    @Override
    public void playLivingSound()
    {
        if (getAnimation() == Animations.IDLE.get())
        {
            this.setAnimation(Animations.SPEAK.get());
            super.playLivingSound();
        }
    }

    @Override
    public void entityInit()
    {
        super.entityInit();

        this.dataWatcher.addObject(WATCHER_IS_CARCASS, 0);
        this.dataWatcher.addObject(WATCHER_AGE, 0);
        this.dataWatcher.addObject(WATCHER_GROWTH_OFFSET, 0);
        this.dataWatcher.addObject(WATCHER_IS_SLEEPING, 0);
        this.dataWatcher.addObject(WATCHER_HAS_TRACKER, 0);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();

        dinosaur = EntityHandler.INSTANCE.getDinosaurByClass(getClass());

        getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
        updateCreatureData();
        adjustHitbox();
    }

    public void updateCreatureData()
    {
        double prevHealth = getMaxHealth();
        double newHealth = transitionFromAge(dinosaur.getBabyHealth(), dinosaur.getAdultHealth());

        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(newHealth);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(transitionFromAge(dinosaur
                .getBabySpeed(), dinosaur.getAdultSpeed()));
//        getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(transitionFromAge(dinosaur.getBabyKnockback(), dinosaur.getAdultKnockback())); TODO

        // adjustHitbox();

        getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(transitionFromAge(dinosaur.getBabyStrength(), dinosaur.getAdultStrength()));

        // EntityLiving has a base of 16 the AI needs to have longer range for things like Herding
        // DO NOT CHANGE FOR NOW - Eventually we'll make the AI work in smaller increments and probably
        //                         have different ranges for different eyesights, but for now please keep it long.
        getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0D);

        if (prevHealth != newHealth)
        {
            heal((float) (newHealth - lastDamage));
        }
    }

    private void adjustHitbox()
    {
        float width = (float) transitionFromAge(dinosaur.getBabySizeX(), dinosaur.getAdultSizeX());
        float height = (float) transitionFromAge(dinosaur.getBabySizeY(), dinosaur.getAdultSizeY());

        if (isCarcass)
        {
            setSize(height, width);
        }
        else
        {
            setSize(width, height);
        }
    }

    public double transitionFromAge(double baby, double adult)
    {
        int maxAge = dinosaur.getMaximumAge();

        if (dinosaurAge > maxAge)
        {
            dinosaurAge = maxAge;
        }

        return (adult - baby) / maxAge * dinosaurAge + baby;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance)
    {
        return true;
    }

    public void applySettingsForActionFigure()
    {
        this.setFullyGrown();
        this.setMale(true);
        this.ticksExisted = 4;
    }

    @Override
    public int getTalkInterval()
    {
        return 200;
    }

    @Override
    public float getSoundPitch()
    {
        return (float) transitionFromAge(1.5F, 1.0F) + ((rand.nextFloat() - 0.5F) * 0.125F);
    }

    @Override
    public float getSoundVolume()
    {
        return (isCarcass || isSleeping) ? 0.0F : (2.0F * ((float) transitionFromAge(0.3F, 1.0F)));
    }

    public void setGenetics(String genetics)
    {
        this.genetics = genetics;
    }

    public String getGenetics()
    {
        return genetics;
    }

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (!isCarcass)
        {
            if (firstUpdate)
            {
                updateCreatureData();
            }

            updateGrowth();

            metabolism.update();

            condition.update();

            if (this.ticksExisted % 62 == 0)
            {
                this.playSound(getBreathingSound(), this.getSoundVolume(), this.getSoundPitch());
            }
        }
    }

    private void updateGrowth()
    {
        if (!this.isDead && ticksExisted % 8 == 0 && !worldObj.isRemote)
        {
            if (worldObj.getGameRules().getBoolean("dinoGrowth"))
            {
                dinosaurAge += Math.min(growthSpeedOffset, 960) + 1;
                metabolism.decreaseEnergy((int) ((Math.min(growthSpeedOffset, 960) + 1) * 0.1));
            }

            if (growthSpeedOffset > 0)
            {
                growthSpeedOffset -= 10;

                if (growthSpeedOffset < 0)
                {
                    growthSpeedOffset = 0;
                }
            }
        }
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        this.tailBuffer.calculateChainSwingBuffer(68.0F, 5, 4.0F, this);

        if (!worldObj.isRemote && animation != null && animation != Animations.IDLE.get())
        {
            int animationLength = dinosaur.getPoseHandler().getAnimationLength(animation, this.getGrowthStage());

            if (animTick < animationLength)
            {
                if (!Animations.getAnimation(animation).shouldHold() || animTick < animationLength - 1)
                {
                    animTick++;
                }
            }
            else
            {
                animTick = 0;
                animation = Animations.IDLE.get();
            }
        }

        if (!worldObj.isRemote)
        {
            dataWatcher.updateObject(WATCHER_AGE, dinosaurAge);

            dataWatcher.updateObject(WATCHER_GROWTH_OFFSET, growthSpeedOffset);
            dataWatcher.updateObject(WATCHER_IS_SLEEPING, isSleeping ? 1 : 0);
            dataWatcher.updateObject(WATCHER_IS_CARCASS, isCarcass ? 1 : 0);
            dataWatcher.updateObject(WATCHER_HAS_TRACKER, hasTracker ? 1 : 0);
        }
        else
        {
            dinosaurAge = dataWatcher.getWatchableObjectInt(WATCHER_AGE);
            growthSpeedOffset = dataWatcher.getWatchableObjectInt(WATCHER_GROWTH_OFFSET);
            isSleeping = dataWatcher.getWatchableObjectInt(WATCHER_IS_SLEEPING) == 1;
            isCarcass = dataWatcher.getWatchableObjectInt(WATCHER_IS_CARCASS) == 1;
            hasTracker = dataWatcher.getWatchableObjectInt(WATCHER_HAS_TRACKER) == 1;
        }

        if (ticksExisted % 16 == 0)
        {
            updateCreatureData();
            adjustHitbox();
        }

        if (isCarcass)
        {
            if (getAnimation() != Animations.DYING.get())
            {
                this.setAnimation(Animations.DYING.get());
            }
        }

        if (isSleeping)
        {
            if (getAnimation() != Animations.SLEEPING.get())
            {
                this.setAnimation(Animations.SLEEPING.get());
            }

            if (ticksExisted % 20 == 0)
            {
                if (stayAwakeTime <= 0 && this.hasPredators())
                {
                    this.disturbSleep();
                }
            }

            if (!shouldSleep() && !worldObj.isRemote)
            {
                isSleeping = false;
            }
        }
        else if (getAnimation() == Animations.SLEEPING.get())
        {
            this.setAnimation(Animations.IDLE.get());
        }

        if (!shouldSleep() && !isSleeping && stayAwakeTime > 0)
        {
            stayAwakeTime = 0;
        }

        if (getAnimation() != Animations.IDLE.get())
        {
            animTick++;
        }

        if (this.isServerWorld())
        {
            animationTasks.onUpdateTasks();
        }

        if (stayAwakeTime > 0)
        {
            stayAwakeTime--;
        }

        prevAge = dinosaurAge;
    }

    @Override
    public boolean isMovementBlocked()
    {
        return isCarcass() || isSleeping() || (animation != null && Animations.getAnimation(animation).doesBlockMovement());
    }

    public int getDaysExisted()
    {
        return (int) Math.floor((dinosaurAge * 8.0F) / 24000.0F);
    }

    public void setFullyGrown()
    {
        dinosaurAge = dinosaur.getMaximumAge();
    }

    public Dinosaur getDinosaur()
    {
        return dinosaur;
    }

    @Override
    public boolean canDespawn()
    {
        return false;
    }

    public int getDinosaurAge()
    {
        return dinosaurAge;
    }

    public void setAge(int age)
    {
        dinosaurAge = age;
    }

    @Override
    public float getEyeHeight()
    {
        return (float) transitionFromAge(dinosaur.getBabyEyeHeight(), dinosaur.getAdultEyeHeight());
    }

    @Override
    protected void dropFewItems(boolean playerAttack, int looting)
    {
        for (String bone : dinosaur.getBones())
        {
            if (rand.nextInt(10) != 0)
            {
                dropStackWithGenetics(new ItemStack(ItemHandler.INSTANCE.fresh_fossils.get(bone), 1, EntityHandler.INSTANCE.getDinosaurId(dinosaur)));
            }
        }
    }

    private void dropStackWithGenetics(ItemStack stack)
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("DNAQuality", geneticsQuality);
        nbt.setString("Genetics", genetics);
        stack.setTagCompound(nbt);

        entityDropItem(stack, 0.0F);
    }

    public void setCarcass(boolean carcass)
    {
        isCarcass = carcass;

        if (carcass)
        {
            carcassHealth = (int) Math.sqrt(width * height) * 2;
            inventory.dropItems(worldObj, rand);
        }
    }

    public boolean isCarcass()
    {
        return isCarcass;
    }

    @Override
    public boolean interact(EntityPlayer player)
    {
        if (player.isSneaking())
        {
            if (getAgePercentage() > 75)
            {
                player.displayGUIChest(inventory);
            }
            else
            {
                if (worldObj.isRemote)
                {
                    String msg;

                    if (hasCustomName())
                    {
                        msg = getCustomNameTag();
                    }
                    else
                    {
                        msg = "This " + getName();
                    }

                    player.addChatComponentMessage(new ChatComponentText(msg + " is not old enough to hold items!")); //TODO translation
                }
            }
        }
        else
        {
            ItemStack heldItem = player.getHeldItem();

            if (heldItem != null)
            {
                Item item = heldItem.getItem();

                if (item instanceof BluePrintItem)
                {
                    ((BluePrintItem) item).setDinosaur(heldItem, EntityHandler.INSTANCE.getDinosaurId(getDinosaur()));
                }
            }
        }

        return false;
    }

    @Override
    public boolean allowLeashing()
    {
        return !getLeashed() && (width < 1.5);
    }

    public int getDNAQuality()
    {
        return geneticsQuality;
    }

    public void setDNAQuality(int quality)
    {
        this.geneticsQuality = quality;
    }

    @Override
    public void setAnimation(Animation newAnimation)
    {
        Animation oldAnimation = animation;

        this.animation = newAnimation;

        if (oldAnimation != newAnimation)
        {
            AnimationHandler.INSTANCE.sendAnimationMessage(this, newAnimation);
        }
    }

    @Override
    public Animation[] getAnimations()
    {
        return Animations.getAnimations();
    }

    @Override
    public void setAnimationTick(int tick)
    {
        animTick = tick;
    }

    @Override
    public Animation getAnimation()
    {
        return animation;
    }

    @Override
    public int getAnimationTick()
    {
        return animTick;
    }

    protected String getSound(String sound)
    {
        return JurassiCraft.MODID + ":" + dinosaur.getName().toLowerCase() + "_" + sound;
    }

    @Override
    public String getLivingSound()
    {
        return getSoundForAnimation(Animations.IDLE.get());
    }

    @Override
    public String getHurtSound()
    {
        return getSoundForAnimation(Animations.INJURED.get());
    }

    public String getSoundForAnimation(Animation animation)
    {
        if (animation == Animations.INJURED.get())
        {
            return getSound("hurt");
        }
        else if (animation == Animations.IDLE.get())
        {
            return getSound("living");
        }
        else if (animation == Animations.CALLING.get())
        {
            return getSound("call");
        }
        else if (animation == Animations.DYING.get())
        {
            return getSound("death");
        }
        else if (animation == Animations.ROARING.get())
        {
            return getSound("roar");
        }

        return null;
    }

    public String getBreathingSound()
    {
        return getSound("breathing");
    }

    public double getAttackDamage()
    {
        return transitionFromAge(dinosaur.getBabyStrength(), dinosaur.getAdultStrength());
    }

    public boolean isStronger(DinosaurEntity dinosaur)
    {
        return getHealth() * (float) getAttackDamage() < dinosaur.getHealth() * (float) dinosaur.getAttackDamage();
    }

    public boolean isMale()
    {
        return isMale;
    }

    public void setMale(boolean male)
    {
        isMale = male;
    }

    public int getAgePercentage()
    {
        int age = getDinosaurAge();
        return age != 0 ? age * 100 / getDinosaur().getMaximumAge() : 0;
    }

    public GrowthStage getGrowthStage()
    {
        GrowthStage stage = GrowthStage.INFANT;

        int percent = getAgePercentage();

        if (percent > 75)
        {
            stage = GrowthStage.ADULT;
        }
        else if (percent > 50)
        {
            stage = GrowthStage.ADOLESCENT;
        }
        else if (percent > 25)
        {
            stage = GrowthStage.JUVENILE;
        }

        return stage;
    }

    public void increaseGrowthSpeed()
    {
        growthSpeedOffset += 240;
    }

    /*
     * Used by DinosaurAnimator class to allow different cyclic animations when land dinosaur is in water (need to @Override the performMowzieSwimmingAnimations() method)
     */
    public boolean isSwimming()
    {
        return (isInWater() || isInLava());
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        nbt.setDouble("DinosaurAge", dinosaurAge);
        nbt.setBoolean("IsCarcass", isCarcass);
        nbt.setInteger("DNAQuality", geneticsQuality);
        nbt.setString("Genetics", genetics);
        nbt.setBoolean("IsMale", isMale);
        nbt.setInteger("GrowthSpeedOffset", growthSpeedOffset);
        nbt.setByte("RareVariant", (byte) rareVariant);
        nbt.setInteger("StayAwakeTime", stayAwakeTime);
        nbt.setBoolean("IsSleeping", isSleeping);
        nbt.setInteger("CarcassHealth", carcassHealth);

        metabolism.writeToNBT(nbt);

        if (owner != null)
        {
            nbt.setString("OwnerUUID", owner.toString());
        }

        inventory.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        dinosaurAge = nbt.getInteger("DinosaurAge");
        isCarcass = nbt.getBoolean("IsCarcass");
        geneticsQuality = nbt.getInteger("DNAQuality");
        genetics = nbt.getString("Genetics");
        isMale = nbt.getBoolean("IsMale");
        growthSpeedOffset = nbt.getInteger("GrowthSpeedOffset");
        rareVariant = nbt.getByte("RareVariant");
        stayAwakeTime = nbt.getInteger("StayAwakeTime");
        isSleeping = nbt.getBoolean("IsSleeping");
        carcassHealth = nbt.getInteger("CarcassHealth");

        metabolism.readFromNBT(nbt);

        String ownerUUID = nbt.getString("OwnerUUID");

        if (ownerUUID != null && ownerUUID.length() > 0)
        {
            owner = UUID.fromString(ownerUUID);
        }

        inventory.readFromNBT(nbt);

        this.updateCreatureData();
        this.adjustHitbox();
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeInt(dinosaurAge);
        buffer.writeBoolean(isCarcass);
        buffer.writeInt(geneticsQuality);
        buffer.writeBoolean(isMale);
        buffer.writeInt(growthSpeedOffset);
        buffer.writeByte((byte) rareVariant);

        ByteBufUtils.writeUTF8String(buffer, genetics); //TODO do we need to add the things that are on the dataManager?
        metabolism.writeSpawnData(buffer);

        ByteBufUtils.writeUTF8String(buffer, genetics.toString()); //TODO do we need to add the things that are on the datawatcher?
    }

    @Override
    public void readSpawnData(ByteBuf additionalData)
    {
        dinosaurAge = additionalData.readInt();
        isCarcass = additionalData.readBoolean();
        geneticsQuality = additionalData.readInt();
        isMale = additionalData.readBoolean();
        growthSpeedOffset = additionalData.readInt();
        rareVariant = additionalData.readByte();

        metabolism.readSpawnData(additionalData);

        genetics = ByteBufUtils.readUTF8String(additionalData);

        updateCreatureData();
        adjustHitbox();
    }

    public MetabolismContainer getMetabolism()
    {
        return metabolism;
    }

    public boolean setSleepLocation(BlockPos sleepLocation, boolean moveTo)
    {
        return !moveTo || getNavigator().tryMoveToXYZ(sleepLocation.getX(), sleepLocation.getY(), sleepLocation.getZ(), 1.0);
    }

    public boolean isSleeping()
    {
        return isSleeping;
    }

    public int getStayAwakeTime()
    {
        return stayAwakeTime;
    }

    public void disturbSleep()
    {
        this.isSleeping = false;
        this.stayAwakeTime = 400;
    }

    public void writeStatsToLog()
    {
        LOGGER.info(this);
    }

    @Override
    public String toString()
    {
        return "DinosaurEntity{ " +
                dinosaur.getName() +
                ", id=" + getEntityId() +
                ", remote=" + getEntityWorld().isRemote +
                ", isDead=" + isDead +
                ", isCarcass=" + isCarcass +
                ", isSleeping=" + isSleeping +
                ", stayAwakeTime=" + stayAwakeTime +
                "\n    " +
                ", dinosaurAge=" + dinosaurAge +
                ", prevAge=" + prevAge +
                ", maxAge" + dinosaur.getMaximumAge() +
                ", ticksExisted=" + ticksExisted +
                ", entityAge=" + entityAge +
                ", isMale=" + isMale +
                ", growthSpeedOffset=" + growthSpeedOffset +
                "\n    " +
                ", food=" + metabolism.getEnergy() + " / " + metabolism.getMaxEnergy() + " (" + metabolism.getMaxEnergy() * 0.875 + ")" +
                ", water=" + metabolism.getWater() + " / " + metabolism.getMaxWater() + " (" + metabolism.getMaxWater() * 0.875 + ")" +
                ", digestingFood=" + metabolism.getDigestingFood() + " / " + MetabolismContainer.MAX_DIGESTION_AMOUNT +
                ", health=" + getHealth() + " / " + getMaxHealth() +
                "\n    " +
                ", pos=" + getPosition() +
                ", eyePos=" + getHeadPos() +
                ", eyeHeight=" + getEyeHeight() +
                ", lookX=" + getLookHelper().getLookPosX() + ", lookY=" + getLookHelper().getLookPosY() + ", lookZ=" + getLookHelper().getLookPosZ() +
                "\n    " +
                ", width=" + width +
                ", bb=" + getEntityBoundingBox() +
//                "\n    " +
//                ", anim=" + animation + (animation != null ? ", duration" + animation.duration : "" ) +

//                "dinosaur=" + dinosaur +
//                ", genetics=" + genetics +
//                ", geneticsQuality=" + geneticsQuality +
//                ", currentAnim=" + currentAnim +
//                ", animation=" + animation +
//                ", animTick=" + animTick +
//                ", hasTracker=" + hasTracker +
//                ", tailBuffer=" + tailBuffer +
//                ", owner=" + owner +
//                ", inventory=" + inventory +
//                ", metabolism=" + metabolism +
                " }";
    }

    public Vec3 getHeadPos()
    {
        double scale = transitionFromAge(dinosaur.getScaleInfant(), dinosaur.getScaleAdult());

        double[] headPos = dinosaur.getHeadPosition(getGrowthStage(), ((360 - rotationYawHead)) % 360 - 180);

        double headX = ((headPos[0] * 0.0625F) - dinosaur.getOffsetX()) * scale;
        double headY = (((24 - headPos[1]) * 0.0625F) - dinosaur.getOffsetY()) * scale;
        double headZ = ((headPos[2] * 0.0625F) - dinosaur.getOffsetZ()) * scale;

        return new Vec3(posX + headX, posY + (headY), posZ + headZ);
    }

    public boolean areEyelidsClosed()
    {
        return (isCarcass || isSleeping) || ticksExisted % 100 < 4;
    }

    public int getRareVariant()
    {
        return rareVariant;
    }

    public boolean getUseInertialTweens()
    {
        return useInertialTweens;
    }

    public void setUseInertialTweens(boolean parUseInertialTweens)
    {
        useInertialTweens = parUseInertialTweens;
    }

    @Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
        return new ItemStack(ItemHandler.INSTANCE.spawn_egg, 1, EntityHandler.INSTANCE.getDinosaurId(dinosaur));
    }

    @Override
    public void applyEntityCollision(Entity entity)
    {
        if (this.isSleeping)
        {
            if (!entity.noClip && !this.noClip)
            {
                if (entity.getClass() != this.getClass())
                {
                    this.disturbSleep();
                }
            }
        }
    }

    public PandorasBox getCondition()
    {
        return condition;
    }

    public boolean isHealthy()
    {
        return isHealthy;
    }

    public void setHealthy(boolean isHealthy)
    {
        this.isHealthy = isHealthy;
    }

    @Override
    protected void setSize(float width, float height)
    {
        if (width != this.width || height != this.height)
        {
            this.width = width;
            this.height = height;
            AxisAlignedBB bounds = this.getEntityBoundingBox();
            this.setEntityBoundingBox(new AxisAlignedBB(bounds.minX, bounds.minY, bounds.minZ, bounds.minX + (double) this.width, bounds.minY + (double) this.height, bounds.minZ + (double) this.width));
        }
    }
}
