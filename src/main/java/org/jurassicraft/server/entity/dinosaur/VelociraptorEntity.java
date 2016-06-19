package org.jurassicraft.server.entity.dinosaur;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.server.entity.ai.VelociraptorLeapEntityAI;
import org.jurassicraft.server.entity.ai.VelociraptorMeleeEntityAI;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class VelociraptorEntity extends DinosaurEntity
{
    public VelociraptorEntity(World world)
    {
        super(world);
        this.target(EntityPlayer.class, EntityAnimal.class, EntityVillager.class, DilophosaurusEntity.class, GallimimusEntity.class, ParasaurolophusEntity.class, TriceratopsEntity.class);
        this.tasks.addTask(1, new VelociraptorMeleeEntityAI(this, dinosaur.getAttackSpeed()));
    }

    @Override
    public EntityAIBase getAttackAI()
    {
        return new VelociraptorLeapEntityAI(this);
    }

    @Override
    public void fall(float distance, float damageMultiplier)
    {
        if (getAnimation() != DinosaurAnimation.VELOCIRAPTOR_LAND.get())
        {
            super.fall(distance, damageMultiplier);
        }
    }
}
