package org.jurassicraft.server.entity.dinosaur.disabled;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.client.sound.SoundHandler;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class HerrerasaurusEntity extends DinosaurEntity
{
    public HerrerasaurusEntity(World world)
    {
        super(world);
    }

    @Override
    public float getSoundVolume()
    {
        return (float) transitionFromAge(1.3F, 2.0F);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (DinosaurAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.HERRERASAURUS_LIVING;
            case DYING:
                return SoundHandler.HERRERASAURUS_DEATH;
            case INJURED:
                return SoundHandler.HERRERASAURUS_DEATH;
        }

        return null;
    }
}
