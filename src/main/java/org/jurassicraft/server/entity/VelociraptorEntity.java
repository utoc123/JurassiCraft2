package org.jurassicraft.server.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.jurassicraft.client.animation.Animations;
import org.jurassicraft.server.animation.ControlledAnimation;
import org.jurassicraft.server.entity.ai.animations.JCAutoAnimBase;
import org.jurassicraft.server.entity.ai.animations.JCNonAutoAnimBase;
import org.jurassicraft.server.entity.base.AggressiveDinosaurEntity;

import java.util.Random;

public class VelociraptorEntity extends AggressiveDinosaurEntity // implements ICarnivore, IEntityAICreature
{
    private static final Class[] targets = { CompsognathusEntity.class, EntityPlayer.class, DilophosaurusEntity.class, DimorphodonEntity.class, DodoEntity.class, LeaellynasauraEntity.class, HypsilophodonEntity.class, SegisaurusEntity.class, ProtoceratopsEntity.class, OthnieliaEntity.class, MicroceratusEntity.class };

    private static final Class[] deftargets = { EntityPlayer.class, TyrannosaurusEntity.class, GiganotosaurusEntity.class, SpinosaurusEntity.class };

    public ControlledAnimation dontLean = new ControlledAnimation(5);

    public VelociraptorEntity(World world)
    {
        super(world);

        animationTasks.addTask(2, new JCAutoAnimBase(this, Animations.IDLE.get())); // Call
        tasks.addTask(4, new EntityAIOpenDoor(this, true));

        // tasks.addTask(2, new JCAutoAnimBase(this, 25, 2)); //Attack
        // tasks.addTask(2, new JCAutoAnimBase(this, 25, 3)); //Die
        // tasks.addTask(2, new JCAutoAnimBase(this, 6, 4)); //Hurt
        animationTasks.addTask(2, new JCNonAutoAnimBase(this, Animations.LOOKING_RIGHT.get(), 100)); // Head twitch right
        animationTasks.addTask(2, new JCNonAutoAnimBase(this, Animations.LOOKING_LEFT.get(), 100)); // Head twitch left
        animationTasks.addTask(2, new JCNonAutoAnimBase(this, Animations.SNIFFING.get(), 150)); // Sniff

        for (Class target : targets)
        {
            this.addAIForAttackTargets(target, new Random().nextInt(3) + 1);
        }

        for (Class deftarget : deftargets)
        {
            this.defendFromAttacker(deftarget, new Random().nextInt(3) + 1);
        }
    }

    // NOTE: This adds an attack target. Class should be the entity class for the target, lower prio get executed
    // earlier
    @Override
    protected void addAIForAttackTargets(Class<? extends EntityLivingBase> entity, int prio)
    {
        this.tasks.addTask(0, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(1, new EntityAILeapAtTarget(this, 0.5F));
        this.targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, entity, false));
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        // if (getAttackTarget() != null)
        // circleEntity(getAttackTarget(), 7, 1.0f, true, 0);

        if (getAnimation() == Animations.RESTING.get() || getAnimation() == Animations.ATTACKING.get())
        {
            dontLean.decreaseTimer();
        }
        else
        {
            dontLean.increaseTimer();
        }
    }

    // public void circleEntity(Entity target, float radius, float speed, boolean direction, float offset)
    // {
    // VelociraptorEntity[] pack;
    // int directionInt = direction ? 1 : -1;
    //
    // if (getDistanceSqToEntity(target) > radius - 1)
    // {
    // getNavigator().tryMoveToXYZ(target.posX + radius * Math.cos(directionInt * (ticksExisted + offset) * 0.5 * speed / radius), target.posY, target.posZ + radius * Math.sin(directionInt * (ticksExisted + offset) * 0.5 * speed / radius), speed);
    // }
    // }
}
