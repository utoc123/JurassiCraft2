package org.jurassicraft.client.animation;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;

/**
 * @author jabelar
 */
public enum Animations
{
    IDLE(false, false), ATTACKING(false, false), INJURED(false, false), HEAD_COCKING, CALLING(false, true), HISSING, POUNCING(false, false), SNIFFING, EATING, DRINKING, MATING(false, false), SLEEPING(true, false), RESTING(true, false), ROARING, SPEAK(false, false),
    SCRATCHING,
    LOOKING_LEFT, LOOKING_RIGHT, BEGGING,
    DYING(true, false),
    WALKING(false, false), SWIMMING(false, false), FLYING(false, false);

    private Animation animation;
    private boolean hold;
    private boolean doesBlockMovement;

    Animations(boolean hold, boolean blockMovement)
    {
        this.hold = hold;
        this.doesBlockMovement = blockMovement;
    }

    Animations()
    {
        this(false, true);
    }

    public Animation get()
    {
        if (animation == null)
        {
            animation = Animation.create(ordinal(), -1);
        }
        return animation;
    }

    public static Animation[] getAnimations()
    {
        Animation[] animations = new Animation[values().length + 1];
        for (int i = 1; i < animations.length; i++)
        {
            animations[i] = values()[i - 1].get();
        }
        animations[0] = IAnimatedEntity.NO_ANIMATION;
        return animations;
    }

    public static Animations getAnimation(Animation animation)
    {
        for (Animations anim : values())
        {
            if (animation.getID() == anim.get().getID())
            {
                return anim;
            }
        }

        return null;
    }

    public boolean shouldHold()
    {
        return hold;
    }

    public boolean doesBlockMovement()
    {
        return doesBlockMovement;
    }
}
