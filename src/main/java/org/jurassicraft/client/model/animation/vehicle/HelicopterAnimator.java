package org.jurassicraft.client.model.animation.vehicle;

import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.vehicles.helicopter.HelicopterBaseEntity;

@SideOnly(Side.CLIENT)
public class HelicopterAnimator implements ITabulaModelAnimator<HelicopterBaseEntity>
{
    @Override
    public void setRotationAngles(TabulaModel model, HelicopterBaseEntity entity, float v, float v1, float v2, float v3, float v4, float v5)
    {
        AdvancedModelRenderer rotor = model.getCube("rotorbase_rotatehere");
        AdvancedModelRenderer tailrotor = model.getCube("tailrotor_rotatehere");
        rotor.rotateAngleY = entity.getRotorRotation();
        tailrotor.rotateAngleX = entity.getRotorRotation();
    }
}
