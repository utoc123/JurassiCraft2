package org.jurassicraft.server.entity.base;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.client.model.tools.ChainBuffer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.server.damage.DinosaurDamageSource;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.ai.AdvancedSwimEntityAI;
import org.jurassicraft.server.entity.ai.AssistOwnerEntityAI;
import org.jurassicraft.server.entity.ai.DefendOwnerEntityAI;
import org.jurassicraft.server.entity.ai.DinosaurAttackMeleeEntityAI;
import org.jurassicraft.server.entity.ai.DinosaurWanderEntityAI;
import org.jurassicraft.server.entity.ai.FleeEntityAI;
import org.jurassicraft.server.entity.ai.FollowOwnerEntityAI;
import org.jurassicraft.server.entity.ai.Herd;
import org.jurassicraft.server.entity.ai.MateEntityAI;
import org.jurassicraft.server.entity.ai.ProtectInfantEntityAI;
import org.jurassicraft.server.entity.ai.RespondToAttackEntityAI;
import org.jurassicraft.server.entity.ai.SelectTargetEntityAI;
import org.jurassicraft.server.entity.ai.SleepEntityAI;
import org.jurassicraft.server.entity.ai.TargetCarcassEntityAI;
import org.jurassicraft.server.entity.ai.TemptNonAdultEntityAI;
import org.jurassicraft.server.entity.ai.animations.CallAnimationAI;
import org.jurassicraft.server.entity.ai.animations.HeadCockAnimationAI;
import org.jurassicraft.server.entity.ai.animations.LookAnimationAI;
import org.jurassicraft.server.entity.ai.animations.RoarAnimationAI;
import org.jurassicraft.server.entity.ai.metabolism.DrinkEntityAI;
import org.jurassicraft.server.entity.ai.metabolism.EatFoodItemEntityAI;
import org.jurassicraft.server.entity.ai.metabolism.FeederEntityAI;
import org.jurassicraft.server.entity.ai.metabolism.GrazeEntityAI;
import org.jurassicraft.server.entity.ai.util.AIUtils;
import org.jurassicraft.server.food.FoodHelper;
import org.jurassicraft.server.genetics.GeneticsHelper;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.lang.LangHelper;
import org.jurassicraft.server.message.SetOrderMessage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public abstract class DinosaurEntity extends EntityCreature implements IEntityAdditionalSpawnData, IAnimatedEntity
{
    private static final DataParameter<Boolean> WATCHER_IS_CARCASS = EntityDataManager.createKey(DinosaurEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> WATCHER_AGE = EntityDataManager.createKey(DinosaurEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> WATCHER_IS_SLEEPING = EntityDataManager.createKey(DinosaurEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> WATCHER_HAS_TRACKER = EntityDataManager.createKey(DinosaurEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<String> WATCHER_OWNER = EntityDataManager.createKey(DinosaurEntity.class, DataSerializers.STRING);
    private static final DataParameter<Order> WATCHER_ORDER = EntityDataManager.createKey(DinosaurEntity.class, DinosaurSerializers.ORDER);

    private static final Logger LOGGER = LogManager.getLogger();
    private final InventoryDinosaur inventory;
    private final MetabolismContainer metabolism;
    protected Dinosaur dinosaur;
    protected int dinosaurAge;
    protected int prevAge;
    protected EntityAITasks animationTasks;
    protected Order order = Order.WANDER;
    private int growthSpeedOffset;
    private boolean isCarcass;
    private int carcassHealth;
    private String genetics;
    private int geneticsQuality;
    private boolean isMale;
    private boolean hasTracker;
    private UUID owner;
    private boolean isSleeping;
    private int stayAwakeTime;
    private boolean useInertialTweens;
    private List<Class<? extends EntityLivingBase>> attackTargets = new ArrayList<>();

    private int attackCooldown;

    @SideOnly(Side.CLIENT)
    public ChainBuffer tailBuffer;

    public Herd herd;

    private boolean isSittingNaturally;

    private Animation animation;
    private int animationTick;
    private int animationLength;

    public DinosaurEntity(World world)
    {
        super(world);

        this.setFullyGrown();

        this.metabolism = new MetabolismContainer(this);
        this.inventory = new InventoryDinosaur(this);

        this.genetics = GeneticsHelper.randomGenetics(rand);
        this.isMale = rand.nextBoolean();

        this.resetAttackCooldown();

        this.animationTick = 0;
        this.setAnimation(DinosaurAnimation.IDLE.get());

        this.setUseInertialTweens(true);

        this.animationTasks = new EntityAITasks(world.theProfiler);

        //tasks.addTask(0, new EscapeBlockEntityAI(this));

        if (!dinosaur.isMarineAnimal())
        {
//            this.tasks.addTask(0, new EntityAISwimming(this));
            this.tasks.addTask(0, new AdvancedSwimEntityAI(this));
            //this.setPathPriority(PathNodeType.WATER, 0.0F);
        }

        if (dinosaur.getDiet().isHerbivorous())
        {
            this.tasks.addTask(1, new GrazeEntityAI(this));
        }

        if (dinosaur.getDiet().isCarnivorous())
        {
            this.tasks.addTask(1, new TargetCarcassEntityAI(this));
        }

        this.tasks.addTask(1, new RespondToAttackEntityAI(this));

        this.tasks.addTask(1, new TemptNonAdultEntityAI(this, 0.6));

        if (dinosaur.shouldDefendOwner())
        {
            this.tasks.addTask(2, new DefendOwnerEntityAI(this));
            this.tasks.addTask(2, new AssistOwnerEntityAI(this));
        }

        if (dinosaur.shouldFlee())
        {
            this.tasks.addTask(2, new FleeEntityAI(this));
        }

        this.tasks.addTask(2, new ProtectInfantEntityAI<>(this));

        this.tasks.addTask(3, new DinosaurWanderEntityAI(this, 0.8F, 20));
        this.tasks.addTask(3, new FollowOwnerEntityAI(this));

        this.tasks.addTask(3, getAttackAI());

        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityLivingBase.class, 6.0F));

        this.animationTasks.addTask(0, new SleepEntityAI(this));

        this.animationTasks.addTask(1, new DrinkEntityAI(this));
        this.animationTasks.addTask(1, new MateEntityAI(this));
        this.animationTasks.addTask(1, new EatFoodItemEntityAI(this));
        this.animationTasks.addTask(1, new FeederEntityAI(this));

        this.animationTasks.addTask(3, new CallAnimationAI(this));
        this.animationTasks.addTask(3, new RoarAnimationAI(this));
        this.animationTasks.addTask(3, new LookAnimationAI(this));
        this.animationTasks.addTask(3, new HeadCockAnimationAI(this));

        if (world.isRemote)
        {
            this.initClient();
        }

        ignoreFrustumCheck = true;
    }

    private void initClient()
    {
        this.tailBuffer = new ChainBuffer();
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
        if (dinosaur.isImprintable())
        {
            UUID prevOwner = this.owner;
            this.owner = player.getUniqueID();

            if (!owner.equals(prevOwner))
            {
                player.addChatComponentMessage(new TextComponentString(new LangHelper("message.tame.name").withProperty("dinosaur", "entity.jurassicraft." + dinosaur.getName().toLowerCase() + ".name").build()));
            }
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity)
    {
        if (entity instanceof DinosaurEntity && ((DinosaurEntity) entity).isCarcass())
        {
            this.setAnimation(DinosaurAnimation.EATING.get());
        }
        else
        {
            this.setAnimation(DinosaurAnimation.ATTACKING.get());
        }

        float damage = (float) getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int knockback = 0;

        if (entity instanceof EntityLivingBase)
        {
            damage += EnchantmentHelper.getModifierForCreature(getHeldItemMainhand(), ((EntityLivingBase) entity).getCreatureAttribute());
            knockback += EnchantmentHelper.getKnockbackModifier(this);
        }

        if (entity.attackEntityFrom(new DinosaurDamageSource("mob", this), damage))
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
                if (getAnimation() == DinosaurAnimation.RESTING.get())
                {
                    this.setAnimation(DinosaurAnimation.IDLE.get());
                    this.isSittingNaturally = false;
                }

                if (!worldObj.isRemote && getAnimation() == DinosaurAnimation.IDLE.get())
                {
                    this.setAnimation(DinosaurAnimation.INJURED.get());
                }

                if (shouldSleep())
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

            if (this.hurtResistantTime <= this.maxHurtResistantTime / 2.0F)
            {
                this.hurtTime = this.maxHurtTime = 10;
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
            int meta = EntityHandler.getDinosaurId(dinosaur);

            if (burning)
            {
                entityDropItem(new ItemStack(ItemHandler.DINOSAUR_STEAK, 1, meta), 0.0F);
            }
            else
            {
                dropStackWithGenetics(new ItemStack(ItemHandler.DINOSAUR_MEAT, 1, meta));
            }
        }
    }

    @Override
    public boolean canBePushed()
    {
        return super.canBePushed() && !this.isCarcass() && !this.isSleeping();
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
        if (rand.nextDouble() >= getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue())
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
    public void onDeath(DamageSource cause)
    {
        super.onDeath(cause);

        if (herd != null)
        {
            if (herd.leader == this)
            {
                herd.updateLeader();
            }

            herd.members.remove(this);
        }

        if (cause.getSourceOfDamage() instanceof EntityLivingBase)
        {
            this.respondToAttack((EntityLivingBase) cause.getSourceOfDamage());
        }
    }

    @Override
    public void playLivingSound()
    {
        if (getAnimation() == DinosaurAnimation.IDLE.get())
        {
            this.setAnimation(DinosaurAnimation.SPEAK.get());
            super.playLivingSound();
        }
    }

    @Override
    public void entityInit()
    {
        super.entityInit();

        this.dataManager.register(WATCHER_IS_CARCASS, false);
        this.dataManager.register(WATCHER_AGE, 0);
        this.dataManager.register(WATCHER_IS_SLEEPING, false);
        this.dataManager.register(WATCHER_HAS_TRACKER, false);
        this.dataManager.register(WATCHER_OWNER, "");
        this.dataManager.register(WATCHER_ORDER, Order.WANDER);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();

        dinosaur = EntityHandler.getDinosaurByClass(getClass());

        getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        updateCreatureData();
        adjustHitbox();
    }

    public void updateCreatureData()
    {
        double prevHealth = getMaxHealth();
        double newHealth = transitionFromAge(dinosaur.getBabyHealth(), dinosaur.getAdultHealth());

        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(newHealth);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(transitionFromAge(dinosaur.getBabySpeed(), dinosaur.getAdultSpeed()));
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
        float width = (float) transitionFromAge(dinosaur.getBabySizeX(), dinosaur.getAdultSizeX());
        float height = (float) transitionFromAge(dinosaur.getBabySizeY(), dinosaur.getAdultSizeY());

        //this.stepHeight = Math.max(0.6F, (float) (Math.ceil(height / 2.0F) / 2.0F));
        this.stepHeight = 1;
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
        int dinosaurAge = this.dinosaurAge;

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
        return (float) transitionFromAge(2.5F, 1.0F) + ((rand.nextFloat() - 0.5F) * 0.125F);
    }

    @Override
    public float getSoundVolume()
    {
        return (isCarcass() || isSleeping) ? 0.0F : (2.0F * ((float) transitionFromAge(0.2F, 1.0F)));
    }

    public String getGenetics()
    {
        return genetics;
    }

    public void setGenetics(String genetics)
    {
        this.genetics = genetics;
    }

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (!worldObj.isRemote)
        {
            if (!this.dinosaur.isMarineAnimal())
            {
                int depth = 0;

                if (this.isSwimming())
                {
                    depth = AIUtils.getWaterDepth(this);
                }

                if (this.isInsideOfMaterial(Material.WATER) || (this.isSwimming() && depth <= Math.ceil(height) && depth > 1))
                {
                    this.getJumpHelper().setJumping();
                }
            }

            if (herd == null)
            {
                herd = new Herd(this);
            }

            if (order == Order.WANDER)
            {
                if (herd.state == Herd.State.STATIC && this.getAttackTarget() == null)
                {
                    if (!this.isSleeping && !this.isSwimming() && this.getAnimation() == DinosaurAnimation.IDLE.get() && rand.nextInt(400) == 0)
                    {
                        this.setAnimation(DinosaurAnimation.RESTING.get());
                        this.isSittingNaturally = true;
                    }
                }
                else if (this.getAnimation() == DinosaurAnimation.RESTING.get())
                {
                    this.setAnimation(DinosaurAnimation.IDLE.get());
                    this.isSittingNaturally = false;
                }
            }

            if (this == herd.leader)
            {
                this.herd.onUpdate();
            }
        }

        if (!isCarcass)
        {
            if (firstUpdate)
            {
                updateCreatureData();
            }

            updateGrowth();

            if (!worldObj.isRemote)
            {
                metabolism.update();
            }

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

        if (attackCooldown > 0)
        {
            attackCooldown--;
        }

        if (animation != null && animation != DinosaurAnimation.IDLE.get())
        {
            boolean shouldHold = DinosaurAnimation.getAnimation(animation).shouldHold();

            if (animationTick < animationLength)
            {
                if (!shouldHold || animationTick < animationLength - 1)
                {
                    animationTick++;
                }
            }
            else if (!shouldHold)
            {
                animationTick = 0;
                animation = DinosaurAnimation.IDLE.get();
            }
            else
            {
                animationTick = animationLength - 1;
            }
        }

        if (!worldObj.isRemote)
        {
            dataManager.set(WATCHER_AGE, dinosaurAge);
            dataManager.set(WATCHER_IS_SLEEPING, isSleeping);
            dataManager.set(WATCHER_IS_CARCASS, isCarcass);
            dataManager.set(WATCHER_HAS_TRACKER, hasTracker);
            dataManager.set(WATCHER_ORDER, order);
            dataManager.set(WATCHER_OWNER, owner != null ? owner.toString() : "");
        }
        else
        {
            updateTailBuffer();

            dinosaurAge = dataManager.get(WATCHER_AGE);
            isSleeping = dataManager.get(WATCHER_IS_SLEEPING);
            isCarcass = dataManager.get(WATCHER_IS_CARCASS);
            hasTracker = dataManager.get(WATCHER_HAS_TRACKER);
            order = dataManager.get(WATCHER_ORDER);

            String owner = dataManager.get(WATCHER_OWNER);

            if (owner.length() > 0 && (this.owner == null || !owner.equals(this.owner.toString())))
            {
                this.owner = UUID.fromString(owner);
            }
            else if (owner.length() == 0)
            {
                this.owner = null;
            }
        }

        if (ticksExisted % 16 == 0)
        {
            updateCreatureData();
            adjustHitbox();
        }

        if (!worldObj.isRemote)
        {
            if (isCarcass)
            {
                this.renderYawOffset = this.rotationYaw;
                this.rotationYawHead = this.rotationYaw;

                if (getAnimation() != DinosaurAnimation.DYING.get())
                {
                    this.setAnimation(DinosaurAnimation.DYING.get());
                }

                if (ticksExisted % 1000 == 0)
                {
                    this.attackEntityFrom(DamageSource.generic, 1.0F);
                }
            }
            else
            {
                if (isSleeping)
                {
                    if (getAnimation() != DinosaurAnimation.SLEEPING.get())
                    {
                        this.setAnimation(DinosaurAnimation.SLEEPING.get());
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
                else if (getAnimation() == DinosaurAnimation.SLEEPING.get())
                {
                    this.setAnimation(DinosaurAnimation.IDLE.get());
                }

                if (!isSleeping && !worldObj.isRemote)
                {
                    if (order == Order.SIT)
                    {
                        if (getAnimation() != DinosaurAnimation.RESTING.get())
                        {
                            this.setAnimation(DinosaurAnimation.RESTING.get());
                        }
                    }
                    else if (!this.isSittingNaturally && getAnimation() == DinosaurAnimation.RESTING.get())
                    {
                        this.setAnimation(DinosaurAnimation.IDLE.get());
                    }
                }
            }
        }

        if (!shouldSleep() && !isSleeping && stayAwakeTime > 0)
        {
            stayAwakeTime = 0;
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

    private void updateTailBuffer()
    {
        this.tailBuffer.calculateChainSwingBuffer(68.0F, 5, 4.0F, this);
    }

    @Override
    public boolean isMovementBlocked()
    {
        return isCarcass() || isSleeping() || (animation != null && DinosaurAnimation.getAnimation(animation).doesBlockMovement());
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
                dropStackWithGenetics(new ItemStack(ItemHandler.FRESH_FOSSILS.get(bone), 1, EntityHandler.getDinosaurId(dinosaur)));
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

    public boolean isCarcass()
    {
        return isCarcass;
    }

    public void setCarcass(boolean carcass)
    {
        isCarcass = carcass;

        if (carcass)
        {
            carcassHealth = (int) Math.sqrt(width * height) * 2;
            ticksExisted = 0;
            inventory.dropItems(worldObj, rand);
        }
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack)
    {
        if (player.isSneaking() && hand == EnumHand.MAIN_HAND)
        {
            if (isOwner(player))
            {
                if (getAgePercentage() > 75)
                {
                    player.displayGUIChest(inventory);
                }
                else
                {
                    if (worldObj.isRemote)
                    {
                        player.addChatComponentMessage(new TextComponentTranslation("message.too_young.name"));
                    }
                }
            }
            else
            {
                if (worldObj.isRemote)
                {
                    player.addChatComponentMessage(new TextComponentTranslation("message.not_owned.name"));
                }
            }
        }
        else
        {
            if (stack == null && hand == EnumHand.MAIN_HAND && worldObj.isRemote)
            {
                if (isOwner(player))
                {
                    JurassiCraft.PROXY.openOrderGui(this);
                }
                else
                {
                    player.addChatComponentMessage(new TextComponentTranslation("message.not_owned.name"));
                }
            }
            else if (stack != null && metabolism.isHungry() && FoodHelper.isEdible(dinosaur.getDiet(), stack.getItem()))
            {
                if (isOwner(player) && !worldObj.isRemote)
                {
                    Item item = stack.getItem();
                    metabolism.eat(FoodHelper.getHealAmount(item));
                    FoodHelper.applyEatEffects(this, item);
                    stack.stackSize--;
                }
                else if (worldObj.isRemote)
                {
                    player.addChatComponentMessage(new TextComponentTranslation("message.not_owned.name"));
                }
            }
        }

        return false;
    }

    public boolean isOwner(EntityPlayer player)
    {
        return player.getUniqueID().equals(getOwner());
    }

    @Override
    public boolean canBeLeashedTo(EntityPlayer player)
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
    public Animation[] getAnimations()
    {
        return DinosaurAnimation.getAnimations();
    }

    @Override
    public Animation getAnimation()
    {
        return animation;
    }

    @Override
    public void setAnimation(Animation newAnimation)
    {
        if (isSleeping())
        {
            newAnimation = DinosaurAnimation.SLEEPING.get();
        }

        if (isCarcass())
        {
            newAnimation = DinosaurAnimation.DYING.get();
        }

        Animation oldAnimation = animation;

        this.animation = newAnimation;

        if (oldAnimation != newAnimation)
        {
            this.animationLength = dinosaur.getPoseHandler().getAnimationLength(animation, this.getGrowthStage());

            AnimationHandler.INSTANCE.sendAnimationMessage(this, newAnimation);
        }
    }

    @Override
    public int getAnimationTick()
    {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick)
    {
        animationTick = tick;
    }

    @Override
    public SoundEvent getAmbientSound()
    {
        return getSoundForAnimation(DinosaurAnimation.SPEAK.get());
    }

    @Override
    public SoundEvent getHurtSound()
    {
        return getSoundForAnimation(DinosaurAnimation.INJURED.get());
    }

    @Override
    public SoundEvent getDeathSound()
    {
        return getSoundForAnimation(DinosaurAnimation.DYING.get());
    }

    public SoundEvent getSoundForAnimation(Animation animation)
    {
        return null;
    }

    public SoundEvent getBreathingSound()
    {
        return null;
    }

    public double getAttackDamage()
    {
        return transitionFromAge(dinosaur.getBabyStrength(), dinosaur.getAdultStrength());
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
        int percent = getAgePercentage();

        return percent > 75 ? GrowthStage.ADULT : percent > 50 ? GrowthStage.ADOLESCENT : percent > 25 ? GrowthStage.JUVENILE : GrowthStage.INFANT;
    }

    public void increaseGrowthSpeed()
    {
        growthSpeedOffset += 240;
    }

    public boolean isSwimming()
    {
        return (isInWater() || isInLava()) && !onGround;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt = super.writeToNBT(nbt);

        nbt.setInteger("DinosaurAge", dinosaurAge);
        nbt.setBoolean("IsCarcass", isCarcass);
        nbt.setInteger("DNAQuality", geneticsQuality);
        nbt.setString("Genetics", genetics);
        nbt.setBoolean("IsMale", isMale);
        nbt.setInteger("GrowthSpeedOffset", growthSpeedOffset);
        nbt.setInteger("StayAwakeTime", stayAwakeTime);
        nbt.setBoolean("IsSleeping", isSleeping);
        nbt.setByte("Order", (byte) order.ordinal());
        nbt.setInteger("CarcassHealth", carcassHealth);

        metabolism.writeToNBT(nbt);

        if (owner != null)
        {
            nbt.setString("OwnerUUID", owner.toString());
        }

        inventory.writeToNBT(nbt);

        return nbt;
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
        stayAwakeTime = nbt.getInteger("StayAwakeTime");
        isSleeping = nbt.getBoolean("IsSleeping");
        carcassHealth = nbt.getInteger("CarcassHealth");
        order = Order.values()[nbt.getByte("Order")];

        metabolism.readFromNBT(nbt);

        String ownerUUID = nbt.getString("OwnerUUID");

        if (ownerUUID.length() > 0)
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
    }

    @Override
    public void readSpawnData(ByteBuf additionalData)
    {
        dinosaurAge = additionalData.readInt();
        isCarcass = additionalData.readBoolean();
        geneticsQuality = additionalData.readInt();
        isMale = additionalData.readBoolean();
        growthSpeedOffset = additionalData.readInt();

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

    public void setSleeping(boolean sleeping)
    {
        this.isSleeping = sleeping;
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

    public Vec3d getHeadPos()
    {
        double scale = transitionFromAge(dinosaur.getScaleInfant(), dinosaur.getScaleAdult());

        double[] headPos = dinosaur.getHeadPosition(getGrowthStage(), ((360 - rotationYawHead)) % 360 - 180);

        double headX = ((headPos[0] * 0.0625F) - dinosaur.getOffsetX()) * scale;
        double headY = (((24 - headPos[1]) * 0.0625F) - dinosaur.getOffsetY()) * scale;
        double headZ = ((headPos[2] * 0.0625F) - dinosaur.getOffsetZ()) * scale;

        return new Vec3d(posX + headX, posY + (headY), posZ + headZ);
    }

    public boolean areEyelidsClosed()
    {
        return (isCarcass || isSleeping) || ticksExisted % 100 < 4;
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
    public ItemStack getPickedResult(RayTraceResult target)
    {
        return new ItemStack(ItemHandler.SPAWN_EGG, 1, EntityHandler.getDinosaurId(dinosaur));
    }

    @Override
    public void collideWithEntity(Entity entity)
    {
        super.collideWithEntity(entity);

        if (this.isSleeping && !this.isRidingSameEntity(entity))
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

    @Override
    protected void setSize(float width, float height)
    {
        if (width != this.width || height != this.height)
        {
            float prevWidth = this.width;

            this.width = width;
            this.height = height;

            AxisAlignedBB bounds = this.getEntityBoundingBox();
            this.setEntityBoundingBox(new AxisAlignedBB(bounds.minX, bounds.minY, bounds.minZ, bounds.minX + (double) this.width, bounds.minY + (double) this.height, bounds.minZ + (double) this.width));

            if (this.width > prevWidth && !this.firstUpdate && !this.worldObj.isRemote)
            {
                this.moveEntity(prevWidth - this.width, 0.0, prevWidth - this.width);
            }
        }
    }

    public Order getOrder()
    {
        return order;
    }

    public void setOrder(Order order)
    {
        this.order = order;

        if (worldObj.isRemote)
        {
            if (owner != null)
            {
                EntityPlayer player = worldObj.getPlayerEntityByUUID(owner);

                if (player != null)
                {
                    player.addChatComponentMessage(new TextComponentString(new LangHelper("message.set_order.name").withProperty("order", "order." + order.name().toLowerCase() + ".name").build()));
                }
            }

            JurassiCraft.NETWORK_WRAPPER.sendToServer(new SetOrderMessage(this));
        }
    }

    @SafeVarargs
    public final void target(Class<? extends EntityLivingBase>... targets)
    {
        for (Class<? extends EntityLivingBase> target : targets)
        {
            targetTasks.addTask(1, new SelectTargetEntityAI<>(this, target));
        }

        attackTargets.addAll(Lists.newArrayList(targets));
    }

    public EntityAIBase getAttackAI()
    {
        return new DinosaurAttackMeleeEntityAI(this, dinosaur.getAttackSpeed(), false);
    }

    public List<Class<? extends EntityLivingBase>> getAttackTargets()
    {
        return attackTargets;
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData data)
    {
        metabolism.setEnergy(metabolism.getMaxEnergy());
        metabolism.setWater(metabolism.getMaxWater());
        genetics = GeneticsHelper.randomGenetics(rand);
        setFullyGrown();
        setMale(rand.nextBoolean());
        setDNAQuality(100);
        return data;
    }

    public int getAttackCooldown()
    {
        return attackCooldown;
    }

    public void resetAttackCooldown()
    {
        attackCooldown = 100 + getRNG().nextInt(20);
    }

    public void respondToAttack(EntityLivingBase attacker)
    {
        if (attacker != null && !attacker.isDead && !(attacker instanceof EntityPlayer && ((EntityPlayer) attacker).capabilities.isCreativeMode))
        {
            List<EntityLivingBase> enemies = new LinkedList<>();

            if (attacker instanceof DinosaurEntity)
            {
                DinosaurEntity enemyDinosaur = (DinosaurEntity) attacker;

                if (enemyDinosaur.herd != null)
                {
                    enemies.addAll(enemyDinosaur.herd.members);
                }
            }
            else
            {
                enemies.add(attacker);
            }

            if (enemies.size() > 0)
            {
                Herd herd = this.herd;

                if (herd != null)
                {
                    herd.fleeing = !herd.shouldDefend(enemies) || dinosaur.shouldFlee();

                    for (EntityLivingBase entity : enemies)
                    {
                        if (!herd.enemies.contains(entity))
                        {
                            herd.enemies.add(entity);
                        }
                    }
                }
                else
                {
                    this.setAttackTarget(enemies.get(this.getRNG().nextInt(enemies.size())));
                }
            }
        }
    }

    public int getAnimationLength()
    {
        return animationLength;
    }

    public enum Order
    {
        WANDER, FOLLOW, SIT
    }
}
