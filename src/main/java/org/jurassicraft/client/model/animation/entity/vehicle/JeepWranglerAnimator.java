package org.jurassicraft.client.model.animation.entity.vehicle;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.entity.vehicle.JeepWranglerEntity;

@SideOnly(Side.CLIENT)
public class JeepWranglerAnimator implements ITabulaModelAnimator<JeepWranglerEntity>
{
    @Override
    public void setRotationAngles(TabulaModel model, JeepWranglerEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer wheelHolderFront = model.getCube("wheel holder front");
        AdvancedModelRenderer wheelHolderBack = model.getCube("wheel holder back");

        float partialTicks = LLibrary.PROXY.getPartialTicks();
        float wheelRotation = entity.prevWheelRotateAmount + (entity.wheelRotateAmount - entity.prevWheelRotateAmount) * partialTicks;
        float wheelRotationAmount = entity.wheelRotation - entity.wheelRotateAmount * (1.0F - partialTicks);

        if (entity.backward())
        {
            wheelRotationAmount = -wheelRotationAmount;
        }

        wheelHolderFront.rotateAngleX = wheelRotationAmount * 0.5F;
        wheelHolderBack.rotateAngleX = wheelRotationAmount * 0.5F;

        float steerAmount = (float) (Math.toRadians(entity.right() ? -40.0F : entity.left() ? 40.0F : 0.0F) * wheelRotation);

        model.getCube("steering wheel main").rotateAngleZ = steerAmount;
        wheelHolderFront.rotateAngleY = -steerAmount * 0.2F;
    }
}
