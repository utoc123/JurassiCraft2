package org.jurassicraft.server.entity.ai.util;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;

import java.util.ArrayList;

public class HuntingUtils
{

    private ArrayList<Entity> localEntities;

    public HuntingUtils()
    {
        localEntities = Lists.newArrayList();
    }

    public ArrayList<Entity> getLocalEntities()
    {
        return localEntities;
    }

    public void addEntity(Entity entity)
    {
        localEntities.add(entity);
    }

    public void removeEntity(Entity entity)
    {
        localEntities.remove(entity);
    }

    // TODO
    public Entity chooseEntity()
    {
        return null;
    }
}
