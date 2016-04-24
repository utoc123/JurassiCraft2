package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.ProvokableDinosaurEntity;

public class AnkylosaurusEntity extends ProvokableDinosaurEntity
{
    public AnkylosaurusEntity(World world)
    {
        super(world);
    }

    @Override
    public int getTailBoxCount()
    {
        return 5;
    }
}
