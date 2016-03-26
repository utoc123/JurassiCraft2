package org.jurassicraft.client.model.animation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.animation.DinosaurAnimator;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.server.entity.LeptictidiumEntity;
import org.jurassicraft.server.entity.base.EntityHandler;

@SideOnly(Side.CLIENT)
public class LeptictidiumAnimator extends DinosaurAnimator<LeptictidiumEntity>
{
    public LeptictidiumAnimator()
    {
        super(EntityHandler.INSTANCE.leptictidium);
    }

    @Override
    protected void performMowzieLandAnimations(DinosaurModel model, LeptictidiumEntity entity, float f, float f1, float rotation, float rotationYaw, float rotationPitch, float partialTicks)
    {
    }
}
