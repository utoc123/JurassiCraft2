package org.jurassicraft.server.entity.vehicle;

import net.minecraft.world.World;
import org.jurassicraft.server.item.ItemHandler;

public class JeepWranglerEntity extends CarEntity
{
    public JeepWranglerEntity(World world)
    {
        super(world);
    }

    @Override
    public void dropItems()
    {
        dropItem(ItemHandler.JEEP_WRANGLER, 1);
    }
}
