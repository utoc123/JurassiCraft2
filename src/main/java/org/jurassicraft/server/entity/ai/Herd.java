package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Herd implements Iterable<DinosaurEntity>
{
    public List<DinosaurEntity> members = new LinkedList<>();
    public DinosaurEntity leader;

    private Vec3d center;

    private float moveX;
    private float moveZ;

    public State state = State.STATIC;
    public int stateTicks;

    private Random random = new Random();

    public List<EntityLivingBase> enemies = new ArrayList<>();

    public boolean fleeing;

    private Dinosaur herdType;

    public Herd(DinosaurEntity leader)
    {
        this.herdType = leader.getDinosaur();
        this.members.add(leader);
        this.leader = leader;
        this.resetStateTicks();
    }

    public void onUpdate()
    {
        if (leader == null || leader.isCarcass() || leader.isDead)
        {
            updateLeader();
        }

        if (stateTicks > 0)
        {
            stateTicks--;
        }
        else
        {
            state = state == State.MOVING ? State.STATIC : State.MOVING;
            resetStateTicks();
        }

        if (leader != null)
        {
            if (leader.shouldSleep())
            {
                state = State.STATIC;
                resetStateTicks();
            }

            this.center = getCenterPosition();

            if (enemies.size() > 0)
            {
                if (fleeing)
                {
                    state = State.MOVING;

                    float angle = 0.0F;

                    for (EntityLivingBase attacker : enemies)
                    {
                        angle += Math.atan2(center.zCoord - attacker.posZ, center.xCoord - attacker.posX);
                    }

                    angle /= enemies.size();

                    moveX = (float) -Math.cos(angle);
                    moveZ = (float) Math.sin(angle);

                    normalizeMovement();
                }
                else
                {
                    state = State.STATIC;
                }
            }
            else
            {
                fleeing = false;
            }

            List<DinosaurEntity> remove = new LinkedList<>();

            for (DinosaurEntity entity : this)
            {
                double distance = entity.getDistanceSq(center.xCoord, center.yCoord, center.zCoord);

                if (distance > 2048)
                {
                    remove.add(entity);
                }
            }

            for (DinosaurEntity entity : remove)
            {
                members.remove(entity);
                entity.herd = null;

                if (entity == leader)
                {
                    updateLeader();
                }
            }

            if (leader == null)
            {
                return;
            }

            for (DinosaurEntity entity : this)
            {
                if (enemies.size() == 0 || fleeing)
                {
                    if (!entity.isMovementBlocked() && !entity.isInWater() && (fleeing || entity.getNavigator().noPath()) && (state == State.MOVING || random.nextInt(50) == 0))
                    {
                        float entityMoveX = moveX * 2.0F;
                        float entityMoveZ = moveZ * 2.0F;

                        float centerDistance = (float) Math.abs(entity.getDistance(center.xCoord, entity.posY, center.zCoord));

                        if (fleeing)
                        {
                            centerDistance *= 4.0F;
                        }

                        if (centerDistance > 0)
                        {
                            entityMoveX += (center.xCoord - entity.posX) / centerDistance;
                            entityMoveZ += (center.zCoord - entity.posZ) / centerDistance;
                        }

                        for (DinosaurEntity other : this)
                        {
                            if (other != entity)
                            {
                                float distance = Math.abs(entity.getDistanceToEntity(other));

                                float separation = (entity.width * 1.5F) + 1.5F;

                                if (distance < separation)
                                {
                                    float scale = distance / separation;
                                    entityMoveX += (entity.posX - other.posX) / scale;
                                    entityMoveZ += (entity.posZ - other.posZ) / scale;
                                }
                            }
                        }

                        double navigateX = entity.posX + entityMoveX;
                        double navigateZ = entity.posZ + entityMoveZ;

                        double speed = state == State.STATIC ? 0.8 : entity.getDinosaur().getFlockSpeed();

                        if (fleeing)
                        {
                            if (entity.getDinosaur().getAttackSpeed() > speed)
                            {
                                speed = entity.getDinosaur().getAttackSpeed();
                            }
                        }

                        entity.getNavigator().tryMoveToXYZ(navigateX, entity.worldObj.getHeight(new BlockPos(navigateX, 0, navigateZ)).getY() + 1, navigateZ, speed);
                    }
                }
                else if (!fleeing && entity.getAttackTarget() == null && enemies.size() > 0)
                {
                    if (entity.getAgePercentage() > 50)
                    {
                        entity.setAttackTarget(enemies.get(random.nextInt(enemies.size())));
                    }
                }
            }

            List<EntityLivingBase> removeAttackers = new LinkedList<>();

            for (EntityLivingBase attacker : this.enemies)
            {
                if (attacker.isDead || (attacker instanceof DinosaurEntity && ((DinosaurEntity) attacker).isCarcass()) || attacker.getDistanceSq(center.xCoord, center.yCoord, center.zCoord) > 2048)
                {
                    removeAttackers.add(attacker);
                }
            }

            this.enemies.removeAll(removeAttackers);

            if (state == State.STATIC)
            {
                moveX = 0.0F;
                moveZ = 0.0F;
            }
            else
            {
                moveX += (random.nextFloat() - 0.5F) * 0.1F;
                moveZ += (random.nextFloat() - 0.5F) * 0.1F;

                normalizeMovement();
            }

            this.refreshMembers();
        }
    }

    private void resetStateTicks()
    {
        stateTicks = random.nextInt(state == State.MOVING ? 2000 : 4000) + 1000;
    }

    public void refreshMembers()
    {
        List<DinosaurEntity> remove = new LinkedList<>();

        for (DinosaurEntity entity : this)
        {
            if (entity.isCarcass() || entity.isDead || entity.getMetabolism().isStarving() || entity.getMetabolism().isDehydrated())
            {
                remove.add(entity);
            }
        }

        members.removeAll(remove);

        AxisAlignedBB searchBounds = new AxisAlignedBB(center.xCoord - 16, center.yCoord - 5, center.zCoord - 16, center.xCoord + 16, center.yCoord + 5, center.zCoord + 16);

        List<Herd> otherHerds = new LinkedList<>();

        for (DinosaurEntity entity : leader.worldObj.getEntitiesWithinAABB(DinosaurEntity.class, searchBounds))
        {
            if (leader.getClass().isAssignableFrom(entity.getClass()))
            {
                if (!entity.isCarcass() && !entity.isDead && !(entity.getMetabolism().isStarving() || entity.getMetabolism().isDehydrated()))
                {
                    Herd otherHerd = entity.herd;

                    if (otherHerd == null)
                    {
                        if (size() >= herdType.getMaxHerdSize())
                        {
                            if (herdType.getDiet().isCarnivorous() && !enemies.contains(entity))
                            {
                                enemies.add(entity);
                            }

                            return;
                        }

                        this.addMember(entity);
                    }
                    else if (otherHerd != this && !otherHerds.contains(otherHerd))
                    {
                        otherHerds.add(otherHerd);
                    }
                }
            }
        }

        for (Herd otherHerd : otherHerds)
        {
            if (otherHerd.size() <= this.size() && otherHerd.size() + this.size() < herdType.getMaxHerdSize())
            {
                for (DinosaurEntity member : otherHerd)
                {
                    this.members.add(member);
                    member.herd = this;
                }

                otherHerd.disband();
            }
            else if (this.size() + 1 >= herdType.getMaxHerdSize())
            {
                if (herdType.getDiet().isCarnivorous())
                {
                    for (DinosaurEntity entity : otherHerd)
                    {
                        if (!enemies.contains(entity))
                        {
                            enemies.add(entity);
                        }
                    }
                }
            }
        }
    }

    public void updateLeader()
    {
        if (members.size() > 0)
        {
            leader = members.get(new Random().nextInt(members.size()));
        }
        else
        {
            leader = null;
        }
    }

    public Vec3d getCenterPosition()
    {
        double x = 0.0;
        double z = 0.0;

        int count = 0;

        for (DinosaurEntity member : members)
        {
            if (!member.isCarcass() && !member.isInWater())
            {
                x += member.posX;
                z += member.posZ;

                count++;
            }
        }

        x /= count;
        z /= count;

        return new Vec3d(x, leader.worldObj.getHeight(new BlockPos(x, 0, z)).getY(), z);
    }

    public void addMember(DinosaurEntity entity)
    {
        if (entity.herd != null)
        {
            entity.herd.members.remove(entity);

            if (entity.herd.leader == entity)
            {
                entity.herd.updateLeader();
            }
        }

        entity.herd = this;
        members.add(entity);
    }

    public void disband()
    {
        leader = null;
        members.clear();
    }

    public int size()
    {
        return members.size();
    }

    @Override
    public Iterator<DinosaurEntity> iterator()
    {
        return members.iterator();
    }

    public void normalizeMovement()
    {
        float length = (float) Math.sqrt(Math.pow(moveX, 2) + Math.pow(moveZ, 2));
        moveX = moveX / length;
        moveZ = moveZ / length;
    }

    public boolean shouldDefend(List<EntityLivingBase> entities)
    {
        return this.getScore(this) + herdType.getAttackBias() > this.getScore(entities);
    }

    public double getScore(Iterable<? extends EntityLivingBase> entities)
    {
        double score = 0.0F;

        for (EntityLivingBase entity : entities)
        {
            score += entity.getHealth() * entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
        }

        return score;
    }

    public enum State
    {
        MOVING,
        STATIC,
    }
}