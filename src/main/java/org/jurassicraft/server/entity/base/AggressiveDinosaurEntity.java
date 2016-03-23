package org.jurassicraft.server.entity.base;

import net.minecraft.entity.monster.IMob;
import net.minecraft.world.World;

public abstract class AggressiveDinosaurEntity extends DinosaurEntity implements IMob
{
    public AggressiveDinosaurEntity(World world)
    {
        super(world);

        //tasks.addTask(1, new HuntingEntityAI(this));
    }
}
