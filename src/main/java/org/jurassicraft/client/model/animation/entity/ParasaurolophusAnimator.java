package org.jurassicraft.client.model.animation.entity;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.client.model.animation.DinosaurAnimator;
import org.jurassicraft.server.entity.dinosaur.ParasaurolophusEntity;

@SideOnly(Side.CLIENT)
public class ParasaurolophusAnimator extends DinosaurAnimator<ParasaurolophusEntity>
{
    @Override
    protected void performMowzieLandAnimations(DinosaurModel model, ParasaurolophusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer head = model.getCube("Head");

        AdvancedModelRenderer neck1 = model.getCube("Neck1");
        AdvancedModelRenderer neck2 = model.getCube("Neck2");

//         body parts
        AdvancedModelRenderer stomach = model.getCube("Body2");
        AdvancedModelRenderer shoulders = model.getCube("Body3");
        AdvancedModelRenderer waist = model.getCube("Body1");

//         tail parts
        AdvancedModelRenderer tail1 = model.getCube("Tail1");
        AdvancedModelRenderer tail2 = model.getCube("Tail2");
        AdvancedModelRenderer tail3 = model.getCube("Tail3");
        AdvancedModelRenderer tail4 = model.getCube("Tail4");
        AdvancedModelRenderer tail5 = model.getCube("Tail5");
        AdvancedModelRenderer tail6 = model.getCube("Tail6");

//         left foot
        AdvancedModelRenderer leftThigh = model.getCube("Thigh Left");
        AdvancedModelRenderer leftCalf = model.getCube("Left Calf 1");
        AdvancedModelRenderer leftUpperFoot = model.getCube("Left Calf 2");
        AdvancedModelRenderer leftFoot = model.getCube("Left Foot");

//         right foot
        AdvancedModelRenderer rightThigh = model.getCube("Thigh Right");
        AdvancedModelRenderer rightCalf = model.getCube("Right Calf 1");
        AdvancedModelRenderer rightUpperFoot = model.getCube("Right Calf 2");
        AdvancedModelRenderer rightFoot = model.getCube("Right Foot");

        // right arm
        AdvancedModelRenderer upperArmRight = model.getCube("Upper Arm Right");
        AdvancedModelRenderer lowerArmRight = model.getCube("Lower Arm Right");
        AdvancedModelRenderer rightHand = model.getCube("Right Hand");
        AdvancedModelRenderer rightFingers = model.getCube("Right Fingers");

        // left arm
        AdvancedModelRenderer upperArmLeft = model.getCube("Upper Arm Left");
        AdvancedModelRenderer lowerArmLeft = model.getCube("Lower Arm Left");
        AdvancedModelRenderer leftHand = model.getCube("Left Hand");
        AdvancedModelRenderer leftFingers = model.getCube("Left Fingers");

        AdvancedModelRenderer jaw = model.getCube("Jaw1");

        AdvancedModelRenderer[] body = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1, waist, stomach, shoulders, neck1, neck2 };
        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };

        float globalSpeed = 0.25F;
        float globalDegree = 0.4F;
        float globalHeight = 1.0F;

        model.bob(waist, globalSpeed * 1.0F, globalHeight * 2.0F, false, f, f1);
        model.bob(leftThigh, globalSpeed * 1.0F, globalHeight * 2.0F, false, f, f1);
        model.bob(rightThigh, globalSpeed * 1.0F, globalHeight * 2.0F, false, f, f1);

        model.walk(leftThigh, globalSpeed * 1.0F, globalDegree * 1.0F, false, 0.0F, -0.2F, f, f1);
        model.walk(leftCalf, globalSpeed * 1.0F, globalDegree * 1.0F, false, 0.5F, 0.2F, f, f1);
        model.walk(leftUpperFoot, globalSpeed * 1.0F, globalDegree * 1.0F, true, 0.4F, -0.5F, f, f1);
        model.walk(leftFoot, globalSpeed * 1.0F, globalDegree * 1.0F, false, 0.4F, 0.2F, f, f1);

        model.walk(rightThigh, globalSpeed * 1.0F, globalDegree * 1.0F, true, 0.0F, 0.2F, f, f1);
        model.walk(rightCalf, globalSpeed * 1.0F, globalDegree * 1.0F, true, 0.5F, -0.2F, f, f1);
        model.walk(rightUpperFoot, globalSpeed * 1.0F, globalDegree * 1.0F, false, 0.4F, 0.5F, f, f1);
        model.walk(rightFoot, globalSpeed * 1.0F, globalDegree * 1.0F, true, 0.4F, -0.2F, f, f1);

        model.walk(upperArmLeft, globalSpeed * 1.0F, globalDegree * 1.0F, true, 0.0F, -0.2F, f, f1);
        model.walk(lowerArmLeft, globalSpeed * 1.0F, globalDegree * 1.0F, true, 0.5F, 0.2F, f, f1);
        model.walk(leftHand, globalSpeed * 1.0F, globalDegree * 1.0F, false, 0.4F, -0.5F, f, f1);
        model.walk(leftFingers, globalSpeed * 1.0F, globalDegree * 1.0F, true, 0.4F, 0.2F, f, f1);

        model.walk(upperArmRight, globalSpeed * 1.0F, globalDegree * 1.0F, false, 0.0F, 0.2F, f, f1);
        model.walk(lowerArmRight, globalSpeed * 1.0F, globalDegree * 1.0F, false, 0.5F, -0.2F, f, f1);
        model.walk(rightHand, globalSpeed * 1.0F, globalDegree * 1.0F, true, 0.4F, 0.5F, f, f1);
        model.walk(rightFingers, globalSpeed * 1.0F, globalDegree * 1.0F, false, 0.4F, -0.2F, f, f1);

        model.chainWave(body, globalSpeed * 1.0F, globalHeight * 0.025F, -0.2F, f, f1);
        model.walk(jaw, globalSpeed * 1.0F, globalHeight * 0.25F, false, 0.0F, globalHeight * 0.25F, f, f1);

        model.walk(neck1, 0.1F, 0.07F, false, -1F, 0F, ticks, 0.25F);
        model.walk(head, 0.1F, 0.07F, true, 0F, 0F, ticks, 0.25F);
        model.walk(waist, 0.1F, 0.04F, false, 0F, 0F, ticks, 0.25F);
        model.walk(upperArmRight, 0.1F, 0.1F, false, -1F, 0F, ticks, 0.25F);
        model.walk(upperArmLeft, 0.1F, 0.1F, false, -1F, 0F, ticks, 0.25F);
        model.walk(lowerArmRight, 0.1F, 0.1F, true, -1.5F, 0F, ticks, 0.25F);
        model.walk(lowerArmLeft, 0.1F, 0.1F, true, -1.5F, 0F, ticks, 0.25F);
        model.walk(rightHand, 0.1F, 0.1F, false, -2F, 0F, ticks, 0.25F);
        model.walk(leftHand, 0.1F, 0.1F, false, -2F, 0F, ticks, 0.25F);

        model.chainWave(tail, 0.1F, -0.02F, 2, ticks, 1F);

        model.faceTarget(rotationYaw, rotationPitch, 2.0F, neck1, neck2);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
