package org.jurassicraft.client.model.animation.entity;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.client.model.animation.DinosaurAnimator;
import org.jurassicraft.server.entity.dinosaur.LeptictidiumEntity;

@SideOnly(Side.CLIENT)
public class LeptictidiumAnimator extends DinosaurAnimator<LeptictidiumEntity>
{
    @Override
    protected void performMowzieLandAnimations(DinosaurModel model, LeptictidiumEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
    }
}
