package org.jurassicraft.server.entity.dinosaur.disabled;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.client.sound.SoundHandler;
import org.jurassicraft.server.entity.base.FlyingDinosaurEntity;

public class PteranodonEntity extends FlyingDinosaurEntity
{
    public PteranodonEntity(World world)
    {
        super(world);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (DinosaurAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.PTERANODON_LIVING;
            case CALLING:
                return SoundHandler.PTERANODON_CALL;
            case DYING:
                return SoundHandler.PTERANODON_DEATH;
            case INJURED:
                return SoundHandler.PTERANODON_HURT;
        }

        return null;
    }
}
