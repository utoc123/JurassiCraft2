package org.jurassicraft.server.entity.dinosaur;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.client.sound.SoundHandler;
import org.jurassicraft.server.entity.DinosaurEntity;

public class BrachiosaurusEntity extends DinosaurEntity {
    private int stepCount = 0;

    public BrachiosaurusEntity(World world) {
        super(world);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.onGround && !this.isSwimming()) {
            if (this.moveForward > 0 && this.stepCount <= 0) {
                this.playSound(SoundHandler.STOMP, (float) this.interpolate(0.1F, 1.0F), this.getSoundPitch());
                this.stepCount = 50;
            }

            this.stepCount -= this.moveForward * 9.5;
        }
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (DinosaurAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.BRACHIOSAURUS_LIVING;
            case CALLING:
                return SoundHandler.BRACHIOSAURUS_LIVING;
            case DYING:
                return SoundHandler.BRACHIOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.BRACHIOSAURUS_HURT;
        }

        return null;
    }
}
