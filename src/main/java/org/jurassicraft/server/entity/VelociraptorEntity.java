package org.jurassicraft.server.entity;

import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.AggressiveDinosaurEntity;

public class VelociraptorEntity extends AggressiveDinosaurEntity
{
    public VelociraptorEntity(World world)
    {
        super(world);

        tasks.addTask(4, new EntityAIOpenDoor(this, true));
    }

    @Override
    public int getTailBoxCount()
    {
        return 6;
    }
}
