package org.jurassicraft.client.model.animation.vehicle;

import net.ilexiconn.llibrary.client.model.entity.animation.IModelAnimator;
import net.ilexiconn.llibrary.client.model.modelbase.MowzieModelRenderer;
import net.ilexiconn.llibrary.client.model.tabula.ModelJson;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.vehicles.helicopter.HelicopterBaseEntity;

@SideOnly(Side.CLIENT)
public class HelicopterAnimator implements IModelAnimator
{
    @Override
    public void setRotationAngles(ModelJson model, float f, float f1, float rotation, float rotationYaw, float rotationPitch, float partialTicks, Entity entity)
    {
        HelicopterBaseEntity helicopter = (HelicopterBaseEntity) entity;
        MowzieModelRenderer rotor = model.getCube("rotorbase_rotatehere");
        MowzieModelRenderer tailrotor = model.getCube("tailrotor_rotatehere");
        rotor.rotateAngleY = helicopter.getRotorRotation();
        tailrotor.rotateAngleX = helicopter.getRotorRotation();
    }

}
