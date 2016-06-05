package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.ai.util.AIUtils;

/**
 * Copyright 2016 Andrew O. Mellinger
 */
public class EscapeBlockEntityAI extends EntityAIBase
{
    public EscapeBlockEntityAI(EntityLiving entitylivingIn)
    {
        _entity = entitylivingIn;
    }

    @Override
    public boolean shouldExecute()
    {
        World world = _entity.getEntityWorld();
        return world.isAirBlock(_entity.getPosition());
    }

    @Override
    public void startExecuting()
    {
        BlockPos surface = AIUtils.findSurface(_entity);
        if (surface != null)
        {
            _entity.getNavigator().tryMoveToXYZ(surface.getX(), surface.getY(), surface.getZ(), 1.0);
        }
    }

    @Override
    public boolean continueExecuting()
    {
        return (!_entity.getNavigator().noPath());
    }

    private final EntityLiving _entity;
}
