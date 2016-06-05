package org.jurassicraft.server.entity.dinosaur;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.jurassicraft.JurassiCraft;
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
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        if (animation == Animations.ATTACKING.get())
        {
            return getSound("roar");
        }

        return super.getSoundForAnimation(animation);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (this.moveForward > 0 && this.stepCount <= 0)
        {
            this.playSound(new SoundEvent(new ResourceLocation(JurassiCraft.MODID, "stomp")), (float) transitionFromAge(0.1F, 1.0F), this.getSoundPitch());
            this.stepCount = 65;
        }

        this.stepCount -= this.moveForward * 9.5;
    }
}
