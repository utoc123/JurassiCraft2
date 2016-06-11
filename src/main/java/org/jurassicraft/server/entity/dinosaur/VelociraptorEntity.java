package org.jurassicraft.server.entity.dinosaur;

import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class VelociraptorEntity extends DinosaurEntity
{
    public VelociraptorEntity(World world)
    {
        super(world);

        tasks.addTask(4, new EntityAIOpenDoor(this, true));
    }
}
