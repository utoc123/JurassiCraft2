package org.jurassicraft.server.entity.dinosaur.disabled;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.client.sound.SoundHandler;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class HypsilophodonEntity extends DinosaurEntity
{
    public HypsilophodonEntity(World world)
    {
        super(world);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (DinosaurAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.HYPSILOPHODON_LIVING;
            case DYING:
                return SoundHandler.HYPSILOPHODON_HURT;
            case INJURED:
                return SoundHandler.HYPSILOPHODON_HURT;
        }

        return null;
    }
}
