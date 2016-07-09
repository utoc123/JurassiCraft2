package org.jurassicraft.server.entity.dinosaur.disabled;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.client.sound.SoundHandler;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class AnkylosaurusEntity extends DinosaurEntity
{
    public AnkylosaurusEntity(World world)
    {
        super(world);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (DinosaurAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.ANKYLOSAURUS_LIVING;
            case DYING:
                return SoundHandler.ANKYLOSAURUS_HURT;
            case INJURED:
                return SoundHandler.ANKYLOSAURUS_HURT;
        }

        return null;
    }
}
