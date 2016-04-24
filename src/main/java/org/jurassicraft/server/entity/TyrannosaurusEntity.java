package org.jurassicraft.server.entity;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.world.World;
import org.jurassicraft.client.animation.Animations;
import org.jurassicraft.server.entity.base.AggressiveDinosaurEntity;

public class TyrannosaurusEntity extends AggressiveDinosaurEntity
{
    private int stepCount = 0;

    public TyrannosaurusEntity(World world)
    {
        super(world);

        this.setUseInertialTweens(true);
    }

    @Override
    public String getSoundForAnimation(Animation animation)
    {
        if (animation == Animations.ATTACKING.get())
        {
            return getSound("roar");
        }

        return super.getSoundForAnimation(animation);
    }

    @Override
    public int getTailBoxCount()
    {
        return 6;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (this.moveForward > 0 && this.stepCount <= 0)
        {
            this.playSound("jurassicraft:stomp", (float) transitionFromAge(0.1F, 1.0F), this.getSoundPitch());
            this.stepCount = 65;
        }

        this.stepCount -= this.moveForward * 9.5;
    }
}
