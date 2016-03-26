package org.jurassicraft.client.model;

import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;
import net.minecraft.entity.Entity;

public class ResetControlModelJson extends TabulaModel
{
    private final ITabulaModelAnimator animator;
    private boolean resetAllowed;

    public ResetControlModelJson(TabulaModelContainer model, ITabulaModelAnimator animator)
    {
        super(model, animator);
        this.animator = animator;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float partialTicks, Entity entity)
    {
        if (resetAllowed)
        {
            super.setRotationAngles(limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, partialTicks, entity);
        }
        else
        {
            if (this.animator != null)
            {
                this.animator.setRotationAngles(this, entity, limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, partialTicks);
            }
        }
    }

    public void setResetEachFrame(boolean reset)
    {
        resetAllowed = reset;
    }

}
