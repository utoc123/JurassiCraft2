package org.jurassicraft.client.model.animation;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.animation.DinosaurAnimator;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.server.entity.GallimimusEntity;
import org.jurassicraft.server.entity.base.EntityHandler;

@SideOnly(Side.CLIENT)
public class GallimimusAnimator<ENTITY extends GallimimusEntity> extends DinosaurAnimator<ENTITY>
{
    public GallimimusAnimator()
    {
        super(EntityHandler.INSTANCE.gallimimus);
    }

    @Override
    protected void performMowzieLandAnimations(DinosaurModel model, float f, float f1, float rotation, float rotationYaw, float rotationPitch, float partialTicks, GallimimusEntity entity)
    {
        float globalSpeed = 0.8F;
        float globalDegree = 1.0F;
        float globalHeight = 1.0F;

        AdvancedModelRenderer neck1 = model.getCube("neck1");
        AdvancedModelRenderer neck2 = model.getCube("neck2");
        AdvancedModelRenderer neck3 = model.getCube("neck3");
        AdvancedModelRenderer neck4 = model.getCube("neck4");
        AdvancedModelRenderer neck5 = model.getCube("neck5");

        AdvancedModelRenderer throat = model.getCube("Throat");

        AdvancedModelRenderer tail1 = model.getCube("tail1");
        AdvancedModelRenderer tail2 = model.getCube("tail2");
        AdvancedModelRenderer tail3 = model.getCube("tail3");
        AdvancedModelRenderer tail4 = model.getCube("tail4");
        AdvancedModelRenderer tail5 = model.getCube("tail5");
        AdvancedModelRenderer tail6 = model.getCube("tail6");

        AdvancedModelRenderer body1 = model.getCube("body1");
        AdvancedModelRenderer body2 = model.getCube("body2");
        AdvancedModelRenderer body3 = model.getCube("body3");

        AdvancedModelRenderer head = model.getCube("Head Base");

        AdvancedModelRenderer rightThigh = model.getCube("thigh right");
        AdvancedModelRenderer leftThigh = model.getCube("thigh left");

        AdvancedModelRenderer rightCalf1 = model.getCube("leg right");
        AdvancedModelRenderer leftCalf1 = model.getCube("leg left");

        AdvancedModelRenderer rightCalf2 = model.getCube("upperfoot right");
        AdvancedModelRenderer leftCalf2 = model.getCube("upperfoot left");

        AdvancedModelRenderer rightFoot = model.getCube("foot right");
        AdvancedModelRenderer leftFoot = model.getCube("foot left");

        AdvancedModelRenderer upperArmLeft = model.getCube("Arm UPPER Left");
        AdvancedModelRenderer upperArmRight = model.getCube("Arm UPPER Right");

        AdvancedModelRenderer lowerArmRight = model.getCube("Arm Mid Right");
        AdvancedModelRenderer lowerArmLeft = model.getCube("Arm Mid Left");

        AdvancedModelRenderer handRight = model.getCube("Hand RIGHT");
        AdvancedModelRenderer handLeft = model.getCube("Hand LEFT");

        AdvancedModelRenderer[] body = new AdvancedModelRenderer[] { head, neck5, neck4, neck3, neck2, neck1, body1, body2, body3 };

        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };

        AdvancedModelRenderer[] armLeft = new AdvancedModelRenderer[] { handLeft, lowerArmLeft, upperArmLeft };
        AdvancedModelRenderer[] armRight = new AdvancedModelRenderer[] { handRight, lowerArmRight, upperArmRight };

        model.bob(body3, 1F * globalSpeed, globalHeight, false, f, f1);
        model.bob(leftThigh, 1F * globalSpeed, globalHeight, false, f, f1);
        model.bob(rightThigh, 1F * globalSpeed, globalHeight, false, f, f1);
        body3.rotationPointX += -f1 * globalHeight * Math.cos(f * 0.5 * globalSpeed);
        rightThigh.rotationPointX += -f1 * globalHeight * Math.cos(f * 0.5 * globalSpeed);
        leftThigh.rotationPointX += -f1 * globalHeight * Math.cos(f * 0.5 * globalSpeed);

        body1.rotateAngleZ += f1 * 0.1 * globalHeight * Math.cos(f * 0.5 * globalSpeed);
        head.rotateAngleZ -= f1 * 0.1 * globalHeight * Math.cos(f * 0.5 * globalSpeed);

        model.walk(leftThigh, 0.5F * globalSpeed, 0.8F * globalDegree, false, 0F + 0.1F, 0.2F, f, f1);
        model.walk(leftCalf1, 0.5F * globalSpeed, 0.7F * globalDegree, true, 2F + 0.1F, 0F, f, f1);
        model.walk(leftCalf2, 0.5F * globalSpeed, 0.5F * globalDegree, false, 3F + 0.1F, 0F, f, f1);
        model.walk(leftFoot, 0.5F * globalSpeed, 0.5F * globalDegree, true, 1.5F + 0.1F, 1F, f, f1);

        model.walk(rightThigh, 0.5F * globalSpeed, 0.8F * globalDegree, true, 0F + 0.1F, 0.2F, f, f1);
        model.walk(rightCalf1, 0.5F * globalSpeed, 0.7F * globalDegree, false, 2F + 0.1F, 0F, f, f1);
        model.walk(rightCalf2, 0.5F * globalSpeed, 0.5F * globalDegree, true, 3F + 0.1F, 0F, f, f1);
        model.walk(rightFoot, 0.5F * globalSpeed, 0.5F * globalDegree, false, 1.5F + 0.1F, 1F, f, f1);

        model.walk(upperArmRight, 1 * globalSpeed, 0.3F, true, 0.3F, -0.3F, f, f1);
        model.walk(upperArmLeft, 1 * globalSpeed, 0.3F, true, 0.3F, -0.3F, f, f1);
        model.walk(lowerArmRight, 1 * globalSpeed, 0.3F, true, 0.6F, -0.7F, f, f1);
        model.walk(lowerArmLeft, 1 * globalSpeed, 0.3F, true, 0.6F, -0.7F, f, f1);
        model.walk(handLeft, 1 * globalSpeed, 0.3F, true, 0.9F, 1F, f, f1);
        model.walk(handRight, 1 * globalSpeed, 0.3F, true, 0.9F, 1F, f, f1);

        model.walk(body1, globalSpeed, 0.1F, true, 1.5F, -0.2F, f, f1);
        model.walk(neck1, globalSpeed, 0.1F, true, 1.5F, -0.4F, f, f1);
        model.walk(neck2, globalSpeed, 0.1F, true, 1.5F, -0.5F, f, f1);
        model.walk(neck3, globalSpeed, 0.1F, false, 1.5F, 0.4F, f, f1);
        model.walk(neck4, globalSpeed, 0.1F, false, 1.5F, 0.3F, f, f1);
        model.walk(neck5, globalSpeed, 0.1F, false, 1.5F, 0.3F, f, f1);
        body1.rotationPointZ += 1.1 * f1;
        neck1.rotationPointZ += 1.6 * f1;
        neck2.rotationPointZ += 1.4 * f1;
        throat.rotationPointZ -= 3 * f1;

        model.chainWave(tail, 1 * globalSpeed, -0.05F, 1, f, f1);
        model.chainSwing(tail, 0.5F * globalSpeed, 0.1F, 2, f, f1);

        int frame = entity.ticksExisted;

        model.chainWave(tail, 0.1F, 0.05F, 1, frame, 0.25F);
        model.chainWave(body, 0.1F, -0.05F, 4, frame, 0.25F);
        model.chainWave(armRight, 0.1F, -0.15F, 4, frame, 0.25F);
        model.chainWave(armLeft, 0.1F, -0.15F, 4, frame, 0.25F);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
