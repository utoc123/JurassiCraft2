package org.jurassicraft.server.entity.ai;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Herd implements Iterable<DinosaurEntity>
{
    public static final int MAX_HERD_SIZE = 32;

    public List<DinosaurEntity> members = new LinkedList<>();
    public DinosaurEntity leader;

    private Vec3d center;

    private float moveX;
    private float moveZ;

    public State state = State.STATIC;
    public int stateTicks;

    private Random random = new Random();

    public Herd(DinosaurEntity leader)
    {
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

            for (DinosaurEntity entity : this)
            {
                if (!entity.isMovementBlocked() && entity.getNavigator().noPath() && (state == State.MOVING || random.nextInt(50) == 0))
                {
                    float entityMoveX = moveX * 2.0F;
                    float entityMoveZ = moveZ * 2.0F;

                    float centerDistance = (float) Math.abs(entity.getDistance(center.xCoord, entity.posY, center.zCoord));

                    entityMoveX += (center.xCoord - entity.posX) / centerDistance;
                    entityMoveZ += (center.zCoord - entity.posZ) / centerDistance;

                    for (DinosaurEntity other : this)
                    {
                        if (other != entity)
                        {
                            float distance = Math.abs(entity.getDistanceToEntity(other));

                            float separation = entity.width + 2.0F;

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

                    entity.getNavigator().tryMoveToXYZ(navigateX, entity.worldObj.getHeight(new BlockPos(navigateX, 0, navigateZ)).getY() + 1, navigateZ, state == State.STATIC ? 0.8 : entity.getDinosaur().getFlockSpeed());
                }
            }

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

        for (DinosaurEntity entity : remove)
        {
            members.remove(entity);
        }

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
                        if (size() > MAX_HERD_SIZE)
                        {
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
            if (otherHerd.size() <= this.size() && otherHerd.size() + this.size() < MAX_HERD_SIZE)
            {
                for (DinosaurEntity member : otherHerd)
                {
                    this.members.add(member);
                    member.herd = this;
                }

                otherHerd.disband();
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

        for (DinosaurEntity member : members)
        {
            x += member.posX;
            z += member.posZ;
        }

        x /= members.size();
        z /= members.size();

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

    public enum State
    {
        MOVING,
        STATIC
    }
}