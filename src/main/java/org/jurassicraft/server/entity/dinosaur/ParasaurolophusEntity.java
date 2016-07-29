package org.jurassicraft.server.entity.dinosaur;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.client.sound.SoundHandler;
import org.jurassicraft.server.entity.DinosaurEntity;

public class ParasaurolophusEntity extends DinosaurEntity {
    public ParasaurolophusEntity(World world) {
        super(world);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (DinosaurAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.PARASAUROLOPHUS_LIVING;
            case CALLING:
                return SoundHandler.PARASAUROLOPHUS_CALL;
            case DYING:
                return SoundHandler.PARASAUROLOPHUS_DEATH;
            case INJURED:
                return SoundHandler.PARASAUROLOPHUS_HURT;
        }

        return null;
    }
}
