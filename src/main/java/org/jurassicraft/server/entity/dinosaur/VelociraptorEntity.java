package org.jurassicraft.server.entity.dinosaur;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class VelociraptorEntity extends DinosaurEntity
{
    public VelociraptorEntity(World world)
    {
        super(world);
        this.target(EntityPlayer.class, EntityAnimal.class, DilophosaurusEntity.class, GallimimusEntity.class, ParasaurolophusEntity.class, TriceratopsEntity.class);
    }
}
