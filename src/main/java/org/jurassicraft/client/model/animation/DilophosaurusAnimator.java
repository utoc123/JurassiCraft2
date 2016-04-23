package org.jurassicraft.client.model.animation;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.animation.DinosaurAnimator;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.server.entity.DilophosaurusEntity;
import org.jurassicraft.server.entity.base.EntityHandler;

@SideOnly(Side.CLIENT)
public class DilophosaurusAnimator extends DinosaurAnimator<DilophosaurusEntity>
{
    public DilophosaurusAnimator()
    {
        super(EntityHandler.INSTANCE.dilophosaurus);
    }

    @Override
    protected void performMowzieLandAnimations(DinosaurModel model, DilophosaurusEntity entity, float f, float f1, float rotation, float rotationYaw, float rotationPitch, float partialTicks)
    {
        boolean scary = false;

        AdvancedModelRenderer frillLeftBottom = model.getCube("Frill Lower Left");
        AdvancedModelRenderer frillLeftTop = model.getCube("Frill Upper Left");

        AdvancedModelRenderer frillRightBottom = model.getCube("Frill Lower Right");
        AdvancedModelRenderer frillRightTop = model.getCube("Frill Upper Right");

        frillLeftBottom.showModel = scary;
        frillLeftTop.showModel = scary;
        frillRightBottom.showModel = scary;
        frillRightTop.showModel = scary;

        frillLeftTop.rotateAngleY = (float) Math.toRadians(180);
        frillLeftTop.rotationPointX += 10F;

        frillLeftBottom.rotateAngleY = (float) Math.toRadians(180);
        frillLeftBottom.rotationPointX += 10F;

        float globalSpeed = 0.6F;
        float globalDegree = 0.77F;
        float globalHeight = 2F;

        // f = entity.ticksExisted;
        // f1 = 1F;

        AdvancedModelRenderer head = model.getCube("Head");

        AdvancedModelRenderer neck1 = model.getCube("Neck Base");
        AdvancedModelRenderer neck2 = model.getCube("Neck 2");
        AdvancedModelRenderer neck3 = model.getCube("Neck 3");
        AdvancedModelRenderer neck4 = model.getCube("Neck 4");
        AdvancedModelRenderer neck5 = model.getCube("Neck 5");
        AdvancedModelRenderer neck6 = model.getCube("Neck 6");

        AdvancedModelRenderer body1 = model.getCube("Body FRONT");
        AdvancedModelRenderer body2 = model.getCube("Body MIDDLE");
        AdvancedModelRenderer body3 = model.getCube("Body REAR");

        AdvancedModelRenderer tail1 = model.getCube("Tail BASE");
        AdvancedModelRenderer tail2 = model.getCube("Tail 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail 5");
        AdvancedModelRenderer tail6 = model.getCube("Tail 6");

        AdvancedModelRenderer rightThigh = model.getCube("Leg Right UPPER");
        AdvancedModelRenderer leftThigh = model.getCube("Leg Left UPPER");

        AdvancedModelRenderer lowerRightLeg = model.getCube("Leg Right MID");
        AdvancedModelRenderer lowerLeftLeg = model.getCube("Leg Left MID");

        AdvancedModelRenderer upperRightFoot = model.getCube("Leg Right BOTTOM");
        AdvancedModelRenderer upperLeftFoot = model.getCube("Leg Left BOTTOM");

        AdvancedModelRenderer rightFoot = model.getCube("Foot Right");
        AdvancedModelRenderer leftFoot = model.getCube("Foot Left");

        AdvancedModelRenderer upperArmRight = model.getCube("Right arm");
        AdvancedModelRenderer upperArmLeft = model.getCube("Left arm");

        AdvancedModelRenderer lowerArmRight = model.getCube("Right forearm");
        AdvancedModelRenderer lowerArmLeft = model.getCube("Left forearm");

        AdvancedModelRenderer handRight = model.getCube("Right hand");
        AdvancedModelRenderer handLeft = model.getCube("Left hand");

        // AdvancedModelRenderer upperJaw = model.getCube("upperjaw");
        // AdvancedModelRenderer lowerJaw = model.getCube("down_jaw");

        AdvancedModelRenderer[] body = new AdvancedModelRenderer[] { head, neck6, neck5, neck4, neck3, neck2, neck1, body1, body2, body3 };
        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };

        AdvancedModelRenderer[] armRight = new AdvancedModelRenderer[] { handRight, lowerArmRight, upperArmRight };
        AdvancedModelRenderer[] armLeft = new AdvancedModelRenderer[] { handLeft, lowerArmLeft, upperArmLeft };

        neck4.rotateAngleZ += (rotationYaw / (180f / (float) Math.PI)) / 5;
        neck3.rotateAngleZ += (rotationYaw / (180f / (float) Math.PI)) / 5;
        head.rotateAngleZ += (rotationYaw / (180f / (float) Math.PI)) / 5;

        model.bob(body3, 1F * globalSpeed, globalHeight * 0.7F, false, f, f1);
        model.bob(leftThigh, 1F * globalSpeed, globalHeight * 0.7F, false, f, f1);
        model.bob(rightThigh, 1F * globalSpeed, globalHeight * 0.7F, false, f, f1);
        model.walk(body3, 1F * globalSpeed, globalHeight * 0.05F, true, 0.1F, 0F, f, f1);
        model.chainWave(body, 1F * globalSpeed, -0.03F * globalHeight, 3.5F, f, f1);

        model.walk(leftThigh, 0.5F * globalSpeed, 0.8F * globalDegree, false, 0F, 0.4F, f, f1);
        model.walk(lowerLeftLeg, 0.5F * globalSpeed, 0.8F * globalDegree, true, 1F, 0F, f, f1);
        model.walk(upperLeftFoot, 0.5F * globalSpeed, 0.5F * globalDegree, false, 0F, 0F, f, f1);
        model.walk(leftFoot, 0.5F * globalSpeed, 1.5F * globalDegree, true, 0.5F, 0.7F, f, f1);

        model.walk(rightThigh, 0.5F * globalSpeed, 0.8F * globalDegree, true, 0F, 0.4F, f, f1);
        model.walk(lowerRightLeg, 0.5F * globalSpeed, 0.8F * globalDegree, false, 1F, 0F, f, f1);
        model.walk(upperRightFoot, 0.5F * globalSpeed, 0.5F * globalDegree, true, 0F, 0F, f, f1);
        model.walk(rightFoot, 0.5F * globalSpeed, 1.5F * globalDegree, false, 0.5F, 0.7F, f, f1);

        model.chainSwing(tail, 0.5F * globalSpeed, -0.1F, 2, f, f1);
        model.chainWave(tail, 1F * globalSpeed, -0.05F, 2, f, f1);
        model.chainWave(armRight, 1F * globalSpeed, 0.2F, 3, f, f1);
        model.chainWave(armLeft, 1F * globalSpeed, 0.2F, 3, f, f1);

        int ticksExisted = entity.ticksExisted;

        model.chainWave(tail, 0.15F, -0.03F, 2, ticksExisted, 0.25F);
        model.chainWave(body, 0.15F, 0.03F, 3.5F, ticksExisted, 0.25F);
        model.chainWave(armRight, 0.15F, -0.1F, 4, ticksExisted, 0.25F);
        model.chainWave(armLeft, 0.15F, -0.1F, 4, ticksExisted, 0.25F);
        model.chainSwing(tail, 0.15F, -0.1F, 3, ticksExisted, 0.25F);

        model.faceTarget(rotationYaw, rotationPitch, 1.0F, neck1, neck2, neck3, neck4, head);
        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
