package org.jurassicraft.server.entity.ai.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.AxisAlignedBB;
import org.jurassicraft.server.entity.base.AggressiveSwimmingDinosaurEntity;
import org.jurassicraft.server.entity.base.DefensiveDinosaurEntity;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.util.*;

public class HuntingUtils
{

    private DinosaurEntity entity;

    private ArrayList<EntityLiving> localEntities;

    public HuntingUtils(DinosaurEntity entity)
    {
        localEntities = Lists.newArrayList();
        this.entity = entity;
    }

    public ArrayList<EntityLiving> getLocalEntities()
    {
        return localEntities;
    }

    public void addEntity(EntityLiving entity)
    {
        localEntities.add(entity);
    }

    public void addEntity(List list)
    {
        localEntities.addAll(list);
    }

    public void removeEntity(Entity entity)
    {
        localEntities.remove(entity);
    }

    // TODO: STILL A W.I.P!!!
    public EntityLiving chooseEntity()
    {
        Map<Double, EntityLiving> entitySize = Maps.newHashMap();
        if (localEntities.isEmpty())
        {
            return null;
        }
        for (EntityLiving entity : localEntities)
        {
            if (entity instanceof AggressiveSwimmingDinosaurEntity)
            {
                removeEntity(entity);
            } else
            {
                entitySize.put(getEntitySize(entity), entity);
            }
        }
        double max = Collections.max(entitySize.keySet());
        EntityLiving maxEntity = entitySize.get(max);
        if (maxEntity instanceof DefensiveDinosaurEntity)
        {
            if (getEntitySize(entity) * 2 > getEntitySize(maxEntity))
            {
                return maxEntity;
            }
            return null;
        }
        if (getEntitySize(this.entity) > getEntitySize(maxEntity))
        {
            return maxEntity;
        }
        return null;
    }

    public List<Entity> getEntitiesWithinDistance(EntityLiving e, double xz, double y)
    {
        return e.worldObj.getEntitiesWithinAABBExcludingEntity(e, AxisAlignedBB.fromBounds(e.posX - xz, e.posY - y, e.posZ - xz, e.posX + xz, e.posY + y, e.posZ + xz));
    }

    public double getEntitySize(EntityLiving entity)
    {
        return entity.getEntityBoundingBox().maxX * entity.getEntityBoundingBox().minX
                * entity.getEntityBoundingBox().maxY * entity.getEntityBoundingBox().minY
                * entity.getEntityBoundingBox().maxZ * entity.getEntityBoundingBox().maxZ;
    }

    /**
     * Taken from: http://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map)
    {
        List<Map.Entry<K, V>> list =
                new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>()
        {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
