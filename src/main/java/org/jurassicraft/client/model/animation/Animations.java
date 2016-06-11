package org.jurassicraft.client.model.animation;

import net.ilexiconn.llibrary.server.animation.Animation;

public enum Animations
{
    IDLE(false, false), ATTACKING(false, false), INJURED(false, false), HEAD_COCKING, CALLING(false, true), HISSING, POUNCING(false, false), SNIFFING, EATING, DRINKING, MATING(false, false), SLEEPING(true, false), RESTING(true, true), ROARING, SPEAK(false, false),
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
            animation = Animation.create(-1);
        }

        return animation;
    }

    public static Animation[] getAnimations()
    {
        Animation[] animations = new Animation[values().length];

        for (int i = 0; i < animations.length; i++)
        {
            animations[i] = values()[i].get();
        }

        return animations;
    }

    public static Animations getAnimation(Animation animation)
    {
        for (Animations animations : values())
        {
            if (animation.equals(animations.animation))
            {
                return animations;
            }
        }

        return Animations.IDLE;
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
