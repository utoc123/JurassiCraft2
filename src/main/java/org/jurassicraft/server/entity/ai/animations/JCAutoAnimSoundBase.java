package org.jurassicraft.server.entity.ai.animations;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationAI;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundEvent;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class JCAutoAnimSoundBase<T extends Entity & IAnimatedEntity> extends AnimationAI<T>
{
    protected DinosaurEntity animatingEntity;
    protected Animation animation;
    protected SoundEvent sound;
    protected float volumeOffset;

    public JCAutoAnimSoundBase(DinosaurEntity entity, Animation animation, SoundEvent sound, float volumeOffset)
    {
        super((T) entity);
        this.animatingEntity = entity;
        this.animation = animation;
        this.sound = sound;
        this.volumeOffset = volumeOffset;
    }

    public JCAutoAnimSoundBase(DinosaurEntity entity, Animation animation, SoundEvent sound)
    {
        this(entity, animation, sound, 0.0F);
    }

    @Override
    public Animation getAnimation()
    {
        return animation;
    }

    @Override
    public boolean isAutomatic()
    {
        return true;
    }

    @Override
    public void startExecuting()
    {
        super.startExecuting();
        animatingEntity.playSound(sound, animatingEntity.getSoundVolume() + volumeOffset, animatingEntity.getSoundPitch());
    }
}
