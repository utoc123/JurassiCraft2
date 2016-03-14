package org.jurassicraft.server.entity.base;

import net.minecraft.entity.monster.IMob;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.ai.HuntingEntityAI;
import org.jurassicraft.server.entity.ai.metabolism.DrinkEntityAI;

public abstract class AggressiveDinosaurEntity extends DinosaurEntity implements IMob
{
    public AggressiveDinosaurEntity(World world)
    {
        super(world);

        //tasks.addTask(1, new HuntingEntityAI(this));
    }
}
