package org.jurassicraft.server.entity.dinosaur.disabled;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.client.sound.SoundHandler;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class CarnotaurusEntity extends DinosaurEntity
{
    public CarnotaurusEntity(World world)
    {
        super(world);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (DinosaurAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.CARNOTAURUS_LIVING;
            case CALLING:
                return SoundHandler.CARNOTAURUS_LIVING;
            case DYING:
                return SoundHandler.CARNOTAURUS_DEATH;
            case INJURED:
                return SoundHandler.CARNOTAURUS_HURT;
        }

        return null;
    }
}
