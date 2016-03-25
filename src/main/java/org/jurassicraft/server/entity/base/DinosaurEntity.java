package org.jurassicraft.server.entity.base;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.client.model.tools.ChainBuffer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.animation.Animations;
import org.jurassicraft.server.animation.AIAnimation;
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
import org.jurassicraft.server.genetics.GeneticsContainer;
import org.jurassicraft.server.genetics.GeneticsHelper;
import org.jurassicraft.server.item.BluePrintItem;
import org.jurassicraft.server.item.JCItemRegistry;
import org.jurassicraft.server.item.bones.FossilItem;

import java.util.UUID;

public abstract class DinosaurEntity extends EntityCreature implements IEntityAdditionalSpawnData, IAnimatedEntity
{
    protected Dinosaur dinosaur;

    protected int dinosaurAge;
    protected int prevAge;
    private int growthSpeedOffset;

    private boolean isCarcass;
    private int carcassHealth;

    private GeneticsContainer genetics;
    private int geneticsQuality;
    private boolean isMale;

    // For animation AI system
    public AIAnimation currentAnim = null;
    private Animation animation;
    private int animTick;

    private boolean hasTracker;

    @SideOnly(Side.CLIENT)
    public ChainBuffer tailBuffer = new ChainBuffer();

    private UUID owner;

    private final InventoryDinosaur inventory;

    private static final DataParameter<Boolean> WATCHER_IS_CARCASS = EntityDataManager.createKey(DinosaurEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> WATCHER_AGE = EntityDataManager.createKey(DinosaurEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> WATCHER_GROWTH_OFFSET = EntityDataManager.createKey(DinosaurEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> WATCHER_IS_SLEEPING = EntityDataManager.createKey(DinosaurEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> WATCHER_HAS_TRACKER = EntityDataManager.createKey(DinosaurEntity.class, DataSerializers.BOOLEAN);

    private final MetabolismContainer metabolism;

    private boolean isSleeping;
    private boolean goBackToSleep;

    private static final Logger LOGGER = LogManager.getLogger();

    private int rareVariant;

    private boolean useInertialTweens;

    public DinosaurEntity(World world)
    {
        super(world);

        metabolism = new MetabolismContainer(this);
        inventory = new InventoryDinosaur(this);

        // SetupAI
        //tasks.addTask(0, new EscapeBlockEntityAI(this));

        if (!dinosaur.isMarineAnimal())
        {
            tasks.addTask(0, new EntityAISwimming(this));
//            tasks.addTask(0, new AdvancedSwimEntityAI(this));
        }

        // This one make them move around
        tasks.addTask(0, new SleepEntityAI(this));

        // WARNING: Do not enable, under development
        tasks.addTask(1, new DrinkEntityAI(this));
        tasks.addTask(1, new MateEntityAI(this));
        tasks.addTask(1, new EatFoodItemEntityAI(this));

        if (dinosaur.getDiet().doesEatPlants())
        {
            tasks.addTask(1, new FindPlantEntityAI(this));
        }

        tasks.addTask(2, new EntityAIWander(this, 0.8));

        tasks.addTask(2, new HerdEntityAI(this));

        tasks.addTask(3, new CallAnimationAI(this));
        tasks.addTask(3, new LookAnimationAI(this));
        tasks.addTask(3, new HeadCockAnimationAI(this));

        setFullyGrown();

        genetics = GeneticsHelper.randomGenetics(rand, getDinosaur(), getDNAQuality());
        isMale = rand.nextBoolean();

        animTick = 0;
        setAnimation(Animations.IDLE.get());

        goBackToSleep = true;

        ignoreFrustumCheck = true; // stops dino disappearing when hitbox goes off screen

        int rareVariantCount = dinosaur.getRareVariants().length;

        if (rareVariantCount > 0)
        {
            if (rand.nextInt(100) < 2)
            {
                rareVariant = rand.nextInt(rareVariantCount) + 1;
            }
        }
        // animations have inertia, meaning that they start slow then go fast 
        // and then slow at end to give sense of mass  Good for large dinos, not for mechanical
        // or light-weight entities
        setUseInertialTweens(true);
    }

    public boolean shouldSleep()
    {
        return getDinosaurTime() > getDinosaur().getSleepingSchedule().getAwakeTime();
    }

    public void setSleeping(boolean sleeping)
    {
        if (this.isSleeping != sleeping)
        {
            if (sleeping)
            {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, Animations.SLEEPING.get());
            }
            else
            {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, Animations.IDLE.get());
            }
        }

        this.isSleeping = sleeping;
    }

    public int getDinosaurTime()
    {
        EnumSleepingSchedule sleepingSchedule = getDinosaur().getSleepingSchedule();

        long time = (worldObj.getWorldTime() % 24000) - sleepingSchedule.getWakeUpTime();

        if (time < 0)
        {
            time += 24000;
        }

        return (int) time;
    }

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
        AnimationHandler.INSTANCE.sendAnimationMessage(this, Animations.ATTACKING.get());

        float damage = (float) getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int knockback = 0;

        if (entity instanceof EntityLivingBase)
        {
            damage += EnchantmentHelper.getModifierForCreature(getHeldItemMainhand(), ((EntityLivingBase) entity).getCreatureAttribute());
            knockback += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean attackSuccesful = entity.attackEntityFrom(new DinosaurEntityDamageSource("mob", this), damage);

        if (entity instanceof EntityLivingBase)
        {
            EntityLivingBase theEntityLivingBase = (EntityLivingBase) entity;
            // if attacked entity is killed, stop attacking animation
            if (theEntityLivingBase.getHealth() < 0.0F)
            {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, Animations.IDLE.get());
            }
        }

        if (attackSuccesful)
        {
            if (knockback > 0)
            {
                entity.addVelocity(-MathHelper.sin(rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F, 0.1D, MathHelper.cos(rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F);
                motionX *= 0.6D;
                motionZ *= 0.6D;
            }

            applyEnchantments(this, entity);
        }

        return attackSuccesful;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float amount)
    {
        if (!isCarcass())
        {
            if (getHealth() - amount <= 0.0F)
            {
                this.setHealth(getMaxHealth());
                this.setCarcass(true);
                return false;
            }
            else
            {
                if (getAnimation() == Animations.IDLE.get())
                {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, Animations.INJURED.get());
                }

                if (isSleeping)
                {
                    isSleeping = false;
                    dontGoBackToSleep();
                }

                return super.attackEntityFrom(damageSource, amount);
            }
        }
        else if (!worldObj.isRemote)
        {
            carcassHealth--;

            if (worldObj.getGameRules().getBoolean("doMobLoot"))
            {
                dropMeat(damageSource.getEntity());
            }

            if (carcassHealth < 0)
            {
                this.onDeath(damageSource);
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
            int meta = JCEntityRegistry.getDinosaurId(dinosaur);

            if (burning)
            {
                entityDropItem(new ItemStack(JCItemRegistry.dino_steak, 1, meta), 0.0F);
            }
            else
            {
                dropStackWithGenetics(new ItemStack(JCItemRegistry.dino_meat, 1, meta));
            }
        }
    }

    // Need to override because vanilla knockback makes big dinos get knocked into air
    @Override
    public void knockBack(Entity entity, float p_70653_2_, double motionX, double motionZ)
    {
        if (rand.nextDouble() >= getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue())
        {
            isAirBorne = true;
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
        AnimationHandler.INSTANCE.sendAnimationMessage(this, Animations.INJURED.get());
    }

    @Override
    public void playLivingSound()
    {
        if (getAnimation() == Animations.IDLE.get())
        {
            super.playLivingSound();
        }
    }

    @Override
    public void entityInit()
    {
        super.entityInit();

        dataWatcher.register(WATCHER_IS_CARCASS, false);
        dataWatcher.register(WATCHER_AGE, 0);
        dataWatcher.register(WATCHER_GROWTH_OFFSET, 0);
        dataWatcher.register(WATCHER_IS_SLEEPING, false);
        dataWatcher.register(WATCHER_HAS_TRACKER, false);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();

        dinosaur = JCEntityRegistry.getDinosaurByClass(getClass());

        getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        updateCreatureData();
        adjustHitbox();
    }

    public void updateCreatureData()
    {
        double prevHealth = getMaxHealth();
        double newHealth = transitionFromAge(dinosaur.getBabyHealth(), dinosaur.getAdultHealth());

        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(newHealth);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(transitionFromAge(dinosaur
                .getBabySpeed(), dinosaur.getAdultSpeed()));
//        getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(transitionFromAge(dinosaur.getBabyKnockback(), dinosaur.getAdultKnockback())); TODO

        // adjustHitbox();

        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(transitionFromAge(dinosaur.getBabyStrength(), dinosaur.getAdultStrength()));

        // EntityLiving has a base of 16 the AI needs to have longer range for things like Herding
        // DO NOT CHANGE FOR NOW - Eventually we'll make the AI work in smaller increments and probably
        //                         have different ranges for different eyesights, but for now please keep it long.
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);

        if (prevHealth != newHealth)
        {
            heal((float) (newHealth - lastDamage));
        }
    }

    private void adjustHitbox()
    {
        setSize((float) transitionFromAge(dinosaur.getBabySizeX(), dinosaur.getAdultSizeX()), (float) transitionFromAge(dinosaur.getBabySizeY(), dinosaur.getAdultSizeY()));
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

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge length * 64 * renderDistanceWeight Args: distance
     */
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
        this.genetics = new GeneticsContainer(JCEntityRegistry.getDinosaurId(dinosaur), 0, 0, 0, 255, 255, 255);
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
        this.genetics = new GeneticsContainer(genetics);
    }

    public GeneticsContainer getGenetics()
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
                metabolism.decreaseFood((int) ((Math.min(growthSpeedOffset, 960) + 1) * 0.1));
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

        Vec3d eyes = getHeadPos();

        worldObj.spawnParticle(EnumParticleTypes.FLAME, eyes.xCoord, eyes.yCoord, eyes.zCoord, 0, 0, 0);

        if (!worldObj.isRemote)
        {
            dataWatcher.set(WATCHER_AGE, dinosaurAge);
            dataWatcher.set(WATCHER_GROWTH_OFFSET, growthSpeedOffset);
            dataWatcher.set(WATCHER_IS_SLEEPING, isSleeping);
            dataWatcher.set(WATCHER_IS_CARCASS, isCarcass);
            dataWatcher.set(WATCHER_HAS_TRACKER, hasTracker);
        }
        else
        {
            updateTailBuffer();

            dinosaurAge = dataWatcher.get(WATCHER_AGE);
            growthSpeedOffset = dataWatcher.get(WATCHER_GROWTH_OFFSET);
            isSleeping = dataWatcher.get(WATCHER_IS_SLEEPING);
            isCarcass = dataWatcher.get(WATCHER_IS_CARCASS);
            hasTracker = dataWatcher.get(WATCHER_HAS_TRACKER);
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
                AnimationHandler.INSTANCE.sendAnimationMessage(this, Animations.DYING.get());
            }
        }

        if (isSleeping)
        {
            if (getAnimation() != Animations.SLEEPING.get())
            {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, Animations.SLEEPING.get());
            }

            if (!shouldSleep() && !worldObj.isRemote)
            {
                isSleeping = false;
            }
        }

        if (!shouldSleep() && !isSleeping)
        {
            goBackToSleep = true;
        }

        if (getAnimation() != Animations.IDLE.get())
        {
            animTick++;
        }

        prevAge = dinosaurAge;
    }

    private void updateTailBuffer()
    {
        this.tailBuffer.calculateChainSwingBuffer(68.0F, 5, 4.0F, this);
    }

    @Override
    public boolean isMovementBlocked()
    {
        Animation animation = getAnimation();
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
                dropStackWithGenetics(new ItemStack(JCItemRegistry.fresh_fossils.get(bone), 1, JCEntityRegistry.getDinosaurId(dinosaur)));
            }
        }
    }

    private void dropStackWithGenetics(ItemStack stack)
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("DNAQuality", geneticsQuality);
        nbt.setString("Genetics", genetics.toString());
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
    public boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack)
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

                    player.addChatComponentMessage(new TextComponentString(msg + " is not old enough to hold items!")); //TODO translation
                }
            }
        }
        else
        {
            if (stack != null)
            {
                Item item = stack.getItem();

                if (item instanceof BluePrintItem)
                {
                    ((BluePrintItem) item).setDinosaur(stack, JCEntityRegistry.getDinosaurId(getDinosaur()));
                }
            }
        }

        return false;
    }

    // NOTE: This adds an attack target. Class should be the entity class for the target, lower prio get executed
    // earlier
    protected void addAIForAttackTargets(Class<? extends EntityLivingBase> entity, int prio)
    {
        tasks.addTask(0, new EntityAIAttackMelee(this, dinosaur.getAttackSpeed(), false));
        targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, entity, false));
    }

    @Override
    public boolean canBeLeashedTo(EntityPlayer player)
    {
        return !getLeashed() && (width < 1.5);
    }

    // NOTE: This registers which attackers to defend from. Class should be the entity class for the attacker, lower
    // prio get executed earlier (Should be based upon attacker's strength and health to decide whether to defend or
    // flee)
    protected void defendFromAttacker(Class entity, int prio)
    {
        // targetTasks.addTask(prio, new EntityAIJCShouldDefend(this, true, entity));
        targetTasks.addTask(prio, new EntityAIHurtByTarget(this, true, entity));
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
        JurassiCraft.instance.getLogger().debug("Setting anim id for entity " + getEntityId() + " to " + newAnimation);

        if (newAnimation != animation) // only process changes
        {
            animation = newAnimation;
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

    protected SoundEvent getSound(String sound)
    {
        return new SoundEvent(new ResourceLocation(JurassiCraft.MODID, dinosaur.getName().toLowerCase() + "_" + sound));
    }

    @Override
    public SoundEvent getAmbientSound()
    {
        // Living sounds don't need to be synced to animations, so let this method
        // return a sound
        JurassiCraft.instance.getLogger().info("getLivingSound for " + this.getDinosaur().getName());
        return getSoundForAnimation(Animations.IDLE.get());
    }

    @Override
    public SoundEvent getHurtSound()
    {
        // To better aid syncing animations to sounds, the getInjuredSound() method is used instead
        // called from JabelarAnimationHelper
        return getSoundForAnimation(Animations.INJURED.get());
    }

    public SoundEvent getSoundForAnimation(Animation animation)
    {
        // To better aid syncing animations to sounds, the getDyingSound() method is used instead
        // called from JabelarAnimationHelper
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

    public SoundEvent getBreathingSound()
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

    public int getOverlayR()
    {
        return genetics.getOverlayR();
    }

    public int getOverlayG()
    {
        return genetics.getOverlayG();
    }

    public int getOverlayB()
    {
        return genetics.getOverlayB();
    }

    public int getOverlay(int index)
    {
        switch (index)
        {
            case 0:
                return genetics.getOverlay1();
            case 1:
                return genetics.getOverlay2();
            case 2:
                return genetics.getOverlay3();
            default:
                return -1;
        }
    }

    public EnumGrowthStage getGrowthStage()
    {
        EnumGrowthStage stage = EnumGrowthStage.INFANT;

        int percent = getAgePercentage();

        if (percent > 75)
        {
            stage = EnumGrowthStage.ADULT;
        }
        else if (percent > 50)
        {
            stage = EnumGrowthStage.ADOLESCENT;
        }
        else if (percent > 25)
        {
            stage = EnumGrowthStage.JUVENILE;
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
        nbt.setString("Genetics", genetics.toString());
        nbt.setBoolean("IsMale", isMale);
        nbt.setInteger("GrowthSpeedOffset", growthSpeedOffset);
        nbt.setByte("RareVariant", (byte) rareVariant);

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
        genetics = new GeneticsContainer(nbt.getString("Genetics"));
        isMale = nbt.getBoolean("IsMale");
        growthSpeedOffset = nbt.getInteger("GrowthSpeedOffset");
        rareVariant = nbt.getByte("RareVariant");

        metabolism.readFromNBT(nbt);

        String ownerUUID = nbt.getString("OwnerUUID");

        if (ownerUUID != null && ownerUUID.length() > 0)
        {
            owner = UUID.fromString(ownerUUID);
        }

        inventory.readFromNBT(nbt);

        updateCreatureData();
        adjustHitbox();
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

        genetics = new GeneticsContainer(ByteBufUtils.readUTF8String(additionalData));

        updateCreatureData();
        adjustHitbox();
    }

    public MetabolismContainer getMetabolism()
    {
        return metabolism;
    }

    public boolean setSleepLocation(BlockPos sleepLocation, boolean moveTo)
    {
        if (moveTo)
        {
            int x = sleepLocation.getX();
            int y = sleepLocation.getY();
            int z = sleepLocation.getZ();

            return getNavigator().tryMoveToXYZ(x, y, z, 1.0);
        }

        return true;
    }

    public boolean isSleeping()
    {
        return isSleeping;
    }

    public boolean shouldGoBackToSleep()
    {
        return goBackToSleep;
    }

    public void dontGoBackToSleep()
    {
        this.goBackToSleep = false;
    }

    /**
     * Write all the stats about this dino to the log.  There are too many to write to chat.
     */
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
                ", goBackToSleep=" + goBackToSleep +
                "\n    " +
                ", dinosaurAge=" + dinosaurAge +
                ", prevAge=" + prevAge +
                ", maxAge" + dinosaur.getMaximumAge() +
                ", ticksExisted=" + ticksExisted +
                ", entityAge=" + entityAge +
                ", isMale=" + isMale +
                ", growthSpeedOffset=" + growthSpeedOffset +
                "\n    " +
                ", food=" + metabolism.getFood() + " / " + metabolism.getMaxFood() + " (" + metabolism.getMaxFood() * 0.875 + ")" +
                ", water=" + metabolism.getWater() + " / " + metabolism.getMaxWater() + " (" + metabolism.getMaxWater() * 0.875 + ")" +
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

    public Vec3d getHeadPos() //TODO not working correctly
    {
        double scale = transitionFromAge(dinosaur.getScaleInfant(), dinosaur.getScaleAdult());

        double[] headPos = dinosaur.getHeadPosition(getGrowthStage(), rotationYaw);

        double headX = ((headPos[0] * 0.0625F) - dinosaur.getOffsetX()) * scale;
        double headY = ((headPos[1] * 0.0625F) - dinosaur.getOffsetY()) * scale;
        double headZ = ((headPos[2] * 0.0625F) - dinosaur.getOffsetZ()) * scale;

        return new Vec3d(posX + headX, posY + headY, posZ + headZ);
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
}
