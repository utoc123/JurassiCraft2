package org.jurassicraft.server.entity.dinosaur;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.animation.DinosaurAnimation;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class TyrannosaurusEntity extends DinosaurEntity
{
    private int stepCount = 0;

    public TyrannosaurusEntity(World world)
    {
        super(world);
        this.target(EntityPlayer.class, EntityAnimal.class, EntityVillager.class, EntityMob.class, DilophosaurusEntity.class, GallimimusEntity.class, TriceratopsEntity.class, ParasaurolophusEntity.class, VelociraptorEntity.class);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        if (animation == DinosaurAnimation.CALLING.get())
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
