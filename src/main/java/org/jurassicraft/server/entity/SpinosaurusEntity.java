package org.jurassicraft.server.entity;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.entity.base.AggressiveDinosaurEntity;

public class SpinosaurusEntity extends AggressiveDinosaurEntity
{
    private int stepCount = 0;

    public SpinosaurusEntity(World world)
    {
        super(world);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (this.moveForward > 0 && this.stepCount <= 0)
        {
            this.playSound(new SoundEvent(new ResourceLocation(JurassiCraft.MODID, "stomp")), (float) transitionFromAge(0.1F, 1.0F), this.getSoundPitch());
            stepCount = 65;
        }

        this.stepCount -= this.moveForward * 9.5;
    }
}
