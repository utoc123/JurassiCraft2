package org.jurassicraft.client.model.animation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.animation.DinosaurAnimator;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.EntityHandler;

@SideOnly(Side.CLIENT)
public class TyrannosaurusAnimator extends DinosaurAnimator
{
    public TyrannosaurusAnimator()
    {
        super(EntityHandler.INSTANCE.tyrannosaurus);
    }

    @Override
    protected void performMowzieLandAnimations(DinosaurModel model, float f, float f1, float rotation, float rotationYaw, float rotationPitch, float partialTicks, DinosaurEntity parEntity)
    {
//        // Walking-dependent animation
//        float globalSpeed = 0.45F;
//        float globalDegree = 0.5F;
//        float height = 1.0F;
//
//        AdvancedModelRenderer stomach = model.getCube("Body 2");
//        AdvancedModelRenderer chest = model.getCube("Body 3");
//        AdvancedModelRenderer head = model.getCube("Head");
//        AdvancedModelRenderer waist = model.getCube("Body 1");
//
//        AdvancedModelRenderer neck1 = model.getCube("Neck 1");
//        AdvancedModelRenderer neck2 = model.getCube("Neck 2");
//        AdvancedModelRenderer neck3 = model.getCube("Neck 3");
//        AdvancedModelRenderer neck4 = model.getCube("Neck 4");
//        AdvancedModelRenderer neck5 = model.getCube("Neck 5");
//
//        AdvancedModelRenderer tail1 = model.getCube("Tail 1");
//        AdvancedModelRenderer tail2 = model.getCube("Tail 2");
//        AdvancedModelRenderer tail3 = model.getCube("Tail 3");
//        AdvancedModelRenderer tail4 = model.getCube("Tail 4");
//        AdvancedModelRenderer tail5 = model.getCube("Tail 5");
//        AdvancedModelRenderer tail6 = model.getCube("Tail 6");
//
//        AdvancedModelRenderer throat1 = model.getCube("Throat 1");
//        AdvancedModelRenderer throat2 = model.getCube("Throat 2");
//        AdvancedModelRenderer throat3 = model.getCube("Throat 3");
//
//        AdvancedModelRenderer lowerJaw = model.getCube("Lower Jaw");
//
//        AdvancedModelRenderer handLeft = model.getCube("Hand Left");
//        AdvancedModelRenderer lowerArmLeft = model.getCube("Lower Arm Left");
//        AdvancedModelRenderer upperArmLeft = model.getCube("Upper Arm Left");
//
//        AdvancedModelRenderer handRight = model.getCube("Hand Right");
//        AdvancedModelRenderer lowerArmRight = model.getCube("Lower Arm Right");
//        AdvancedModelRenderer upperArmRight = model.getCube("Upper Arm Right");
//
//        AdvancedModelRenderer leftThigh = model.getCube("Left Thigh");
//        AdvancedModelRenderer rightThigh = model.getCube("Right Thigh");
//
//        AdvancedModelRenderer leftCalf1 = model.getCube("Left Calf 1");
//        AdvancedModelRenderer leftCalf2 = model.getCube("Left Calf 2");
//        AdvancedModelRenderer leftFoot = model.getCube("Foot Left");
//
//        AdvancedModelRenderer rightCalf1 = model.getCube("Right Calf 1");
//        AdvancedModelRenderer rightCalf2 = model.getCube("Right Calf 2");
//        AdvancedModelRenderer rightFoot = model.getCube("Foot Right");
//
//        AdvancedModelRenderer[] tailParts = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };
//        AdvancedModelRenderer[] bodyParts = new AdvancedModelRenderer[] { head, neck5, neck4, neck3, neck2, neck1, chest, stomach, waist };
//        AdvancedModelRenderer[] leftArmParts = new AdvancedModelRenderer[] { handLeft, lowerArmLeft, upperArmLeft };
//        AdvancedModelRenderer[] rightArmParts = new AdvancedModelRenderer[] { handRight, lowerArmRight, upperArmRight };

//        model.bob(waist, 1F * globalSpeed, height, false, f, f1);
//        model.bob(leftThigh, 1F * globalSpeed, height, false, f, f1);
//        model.bob(rightThigh, 1F * globalSpeed, height, false, f, f1);
//        leftThigh.rotationPointY -= -2 * f1 * Math.cos(f * 0.5 * globalSpeed);
//        rightThigh.rotationPointY -= 2 * f1 * Math.cos(f * 0.5 * globalSpeed);
//        model.walk(neck1, 1F * globalSpeed, 0.15F, false, 0F, 0.2F, f, f1);
//        model.walk(head, 1F * globalSpeed, 0.15F, true, 0F, -0.2F, f, f1);
//
//        model.walk(leftThigh, 0.5F * globalSpeed, 0.8F * globalDegree, false, 0F, 0.4F, f, f1);
//        model.walk(leftCalf1, 0.5F * globalSpeed, 1F * globalDegree, true, 1F, 0.4F, f, f1);
//        model.walk(leftCalf2, 0.5F * globalSpeed, 1F * globalDegree, false, 0F, 0F, f, f1);
//        model.walk(leftFoot, 0.5F * globalSpeed, 1.5F * globalDegree, true, 0.5F, 0.3F, f, f1);
//
//        model.walk(rightThigh, 0.5F * globalSpeed, 0.8F * globalDegree, true, 0F, 0.4F, f, f1);
//        model.walk(rightCalf1, 0.5F * globalSpeed, 1F * globalDegree, false, 1F, 0.4F, f, f1);
//        model.walk(rightCalf2, 0.5F * globalSpeed, 1F * globalDegree, true, 0F, 0F, f, f1);
//        model.walk(rightFoot, 0.5F * globalSpeed, 1.5F * globalDegree, false, 0.5F, 0.3F, f, f1);
//
//        model.chainWave(tailParts, 1F * globalSpeed, 0.05F, 2, f, f1);
//        model.chainWave(bodyParts, 1F * globalSpeed, 0.05F, 3, f, f1);
//        model.chainWave(leftArmParts, 1F * globalSpeed, 0.2F, 1, f, f1);
//        model.chainWave(rightArmParts, 1F * globalSpeed, 0.2F, 1, f, f1);
//
//         Idling
//        model.chainWave(bodyParts, 0.1F, -0.03F, 3, parEntity.ticksExisted, 0.25F);
//        model.chainWave(rightArmParts, -0.1F, 0.2F, 4, parEntity.ticksExisted, 0.25F);
//        model.chainWave(leftArmParts, -0.1F, 0.2F, 4, parEntity.ticksExisted, 0.25F);
//        model.chainSwing(tailParts, 0.1F, 0.05F - (0.05F), 1, parEntity.ticksExisted, 0.1F);
//        model.chainWave(tailParts, 0.1F, -0.1F, 2, parEntity.ticksExisted, 0.1F);
//
//        ((TyrannosaurusEntity) parEntity).tailBuffer.applyChainSwingBuffer(tailParts);
    }
}
