package org.jurassicraft.server.entity.ai;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class HerdObj
{
    public List<DinosaurEntity> members = new LinkedList<>();
    public DinosaurEntity leader;
    public Vec3d center;

    public static int random(int max, int min)
    {
        return new Random().nextInt(max - min) + min;
    }

    public void createHerd(DinosaurEntity creator)
    {
        members.add(creator);
        leader = creator;
    }

    public void onUpdate()
    {
        if (leader == null || leader.isDead)
        {
            updateLeader();
        }

        List<DinosaurEntity> farMembers = new LinkedList<>();

        center = getCenterPosition();

        for (DinosaurEntity member : members)
        {
            if (member != null && leader != null)
            {
                double distance = member.getDistanceSq(center.xCoord, center.yCoord, center.zCoord);

                if (distance > 64)
                {
                    farMembers.add(member);
                }
                else if (distance > 4 && member.getNavigator().noPath() && member != this.leader)
                {
                    this.moveMember(member);
                }
            }
        }

        for (DinosaurEntity member : farMembers)
        {
            members.remove(member);
            member.herd = null;
        }

        if (leader != null)
        {
            DinosaurEntity newMember = this.findNewMember(leader.worldObj, leader.getEntityBoundingBox().expand(64, 16, 64), leader);

            if (newMember != null)
            {
                HerdObj oldHerd = newMember.herd;

                if (oldHerd != null)
                {
                    for (DinosaurEntity member : oldHerd.members)
                    {
                        member.herd = this;
                        this.members.add(member);
                    }

                    oldHerd.disband();
                }
                else
                {
                    this.members.add(newMember);
                }
            }

//            if (!leader.isMovementBlocked() && leader.getNavigator().noPath())
//            {
//                Vec3d vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(leader, 16, 7, center);
//
//                if (vec3 != null)
//                {
//                    PathNavigate navigator = this.leader.getNavigator();
//                    Path path = navigator.getPathToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord);
//                    navigator.setPath(path, 1);
//                }
//            }
        }
    }

    private void moveMember(DinosaurEntity member)
    {
        double x = center.xCoord + random(2, -2);
        double z = center.zCoord + random(2, -2);
        member.getNavigator().tryMoveToXYZ(x, leader.worldObj.getHeight(new BlockPos(x, 0, z)).getY(), z, 0.8);
    }

    public DinosaurEntity findNewMember(World world, AxisAlignedBB bounds, DinosaurEntity leader)
    {
        List<DinosaurEntity> nearby = world.getEntitiesWithinAABB(DinosaurEntity.class, bounds);

        DinosaurEntity closest = null;
        double lowestDistance = Double.MAX_VALUE;

        for (DinosaurEntity dinosaur : nearby)
        {
            if (dinosaur.herd != this)
            {
                double distance = leader.getDistanceSqToEntity(dinosaur);

                if (distance <= lowestDistance)
                {
                    closest = dinosaur;
                    lowestDistance = distance;
                }
            }
        }

        return closest;
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

    public void disband()
    {
        members.clear();
    }
}