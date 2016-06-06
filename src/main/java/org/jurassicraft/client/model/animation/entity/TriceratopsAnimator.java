package org.jurassicraft.client.model.animation.entity;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.client.model.animation.DinosaurAnimator;
import org.jurassicraft.server.entity.dinosaur.TriceratopsEntity;

@SideOnly(Side.CLIENT)
public class TriceratopsAnimator extends DinosaurAnimator<TriceratopsEntity>
{
    @Override
    protected void performMowzieLandAnimations(DinosaurModel model, TriceratopsEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer head = model.getCube("Head");
        AdvancedModelRenderer neck3 = model.getCube("Neck 3");
        AdvancedModelRenderer neck2 = model.getCube("Neck 2");
        AdvancedModelRenderer neck1 = model.getCube("Neck 1");
        AdvancedModelRenderer shoulders = model.getCube("Body shoulders");
        AdvancedModelRenderer stomach = model.getCube("Body MAIN");
        AdvancedModelRenderer waist = model.getCube("Body hips");
        AdvancedModelRenderer tail1 = model.getCube("Tail 1");
        AdvancedModelRenderer tail2 = model.getCube("Tail 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail 5");
        AdvancedModelRenderer tail6 = model.getCube("Tail 6");
        AdvancedModelRenderer armUpperLeft = model.getCube("FrontLeg Upper Left");
        AdvancedModelRenderer armLowerLeft = model.getCube("FrontLeg MID Left");
        AdvancedModelRenderer handLeft = model.getCube("FrontLeg FOOT Left");
        AdvancedModelRenderer armUpperRight = model.getCube("FrontLeg Upper Right");
        AdvancedModelRenderer armLowerRight = model.getCube("FrontLeg MID Right");
        AdvancedModelRenderer handRight = model.getCube("FrontLeg FOOT Right");
        AdvancedModelRenderer thighLeft = model.getCube("RearLeg Upper Left");
        AdvancedModelRenderer calfLeft = model.getCube("RearLeg Middle Left");
        AdvancedModelRenderer footLeft = model.getCube("RearLeg Foot Left");
        AdvancedModelRenderer thighRight = model.getCube("RearLeg Upper Right");
        AdvancedModelRenderer calfRight = model.getCube("RearLeg Middle Right");
        AdvancedModelRenderer footRight = model.getCube("RearLeg Foot Right");
        AdvancedModelRenderer jaw = model.getCube("Jaw LOWER");

        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };

        // f = ticks;
        // f1 = 0.5F;

        float sprintModifier = (float) (1 / (1 + Math.exp(30 * (-f1 + 0.92))));
        float legOffsetModifier = 2.5F;
        float bobBase = 2F;
        if (sprintModifier >= 0.9)
        {
            bobBase = 1F;
        }

        float scaleFactor = 0.25F;
        float height = 1.0F;
        float frontOffset = -2F;
        float animationDegree = 2 - sprintModifier * 0.2F;

        // parModel.faceTarget(head, 2, rotationYaw, rotationPitch);
        // parModel.faceTarget(neck1, 2, rotationYaw, rotationPitch);

        model.bob(waist, bobBase * scaleFactor, height, false, f, f1);
        model.bob(thighLeft, bobBase * scaleFactor, height, false, f, f1);
        model.bob(thighRight, bobBase * scaleFactor, height, false, f, f1);
        model.walk(waist, bobBase * scaleFactor, 0.1F * height, true, -1.5F, 0F, f, f1);
        model.walk(head, bobBase * scaleFactor, 0.1F * height, false, -1.5F, 0F, f, f1);
        waist.rotateAngleX += 0.1 * sprintModifier;
        head.rotateAngleX += 0.6 * sprintModifier;

        model.walk(thighLeft, 1F * scaleFactor, 0.2F * animationDegree, false, 0F + sprintModifier * legOffsetModifier, 0F + sprintModifier * 0.2F, f, f1);
        model.walk(thighRight, 1F * scaleFactor, 0.2F * animationDegree - sprintModifier * 0.1F, true, 1F + sprintModifier * legOffsetModifier, 0F, f, f1);
        model.walk(footLeft, 1F * scaleFactor, 0.2F * animationDegree - sprintModifier * 0.1F, false, 1.5F + sprintModifier * legOffsetModifier, 0F, f, f1);

        model.walk(thighRight, 1F * scaleFactor, 0.2F * animationDegree, true, 0F, 0F + sprintModifier * 0.2F, f, f1);
        model.walk(calfRight, 1F * scaleFactor, 0.2F * animationDegree - sprintModifier * 0.1F, false, 1F, 0F, f, f1);
        model.walk(footRight, 1F * scaleFactor, 0.2F * animationDegree - sprintModifier * 0.1F, true, 1.5F, 0F, f, f1);

        model.walk(armUpperRight, 1F * scaleFactor, 0.2F * animationDegree, true, frontOffset + 0F, -0.1F + sprintModifier * 0.2F, f, f1);
        model.walk(armLowerRight, 1F * scaleFactor, 0.1F * animationDegree, true, frontOffset + 1F, -0.2F, f, f1);
        model.walk(handRight, 1F * scaleFactor, 0.2F * animationDegree - sprintModifier * 0.1F, false, frontOffset + 1.5F, 0F, f, f1);

        model.walk(armUpperLeft, 1F * scaleFactor, 0.2F * animationDegree, false, frontOffset + 0F + sprintModifier * legOffsetModifier, -0.1F + sprintModifier * 0.2F, f, f1);
        model.walk(armLowerLeft, 1F * scaleFactor, 0.1F * animationDegree, false, frontOffset + 1F + sprintModifier * legOffsetModifier, -0.2F, f, f1);
        model.walk(handLeft, 1F * scaleFactor, 0.2F * animationDegree - sprintModifier * 0.1F, true, frontOffset + 1.5F + sprintModifier * legOffsetModifier, 0F, f, f1);

        model.chainWave(tail, bobBase * scaleFactor, 0.03F, 1F, f, f1);

        model.walk(neck1, 0.1F, 0.07F, false, -1F, 0F, ticks, 1F);
        model.walk(head, 0.1F, 0.07F, true, 0F, 0F, ticks, 1F);
        model.walk(waist, 0.1F, 0.025F, false, 0F, 0F, ticks, 1F);

        float inverseKinematicsConstant = 0.3F;
        model.walk(armUpperRight, 0.1F, 0.1F * inverseKinematicsConstant, false, 0F, 0F, ticks, 0.25F);
        model.walk(armLowerRight, 0.1F, 0.3F * inverseKinematicsConstant, true, 0F, 0F, ticks, 0.25F);
        model.walk(handRight, 0.1F, 0.175F * inverseKinematicsConstant, false, 0F, 0F, ticks, 0.25F);
        model.walk(armUpperLeft, 0.1F, 0.1F * inverseKinematicsConstant, false, 0F, 0F, ticks, 0.25F);
        model.walk(armLowerLeft, 0.1F, 0.3F * inverseKinematicsConstant, true, 0F, 0F, ticks, 0.25F);
        model.walk(handLeft, 0.1F, 0.175F * inverseKinematicsConstant, false, 0F, 0F, ticks, 0.25F);
        armUpperRight.rotationPointZ -= 0.5 * Math.cos(ticks * 0.025F);
        armUpperLeft.rotationPointZ -= 0.5 * Math.cos(ticks * 0.025F);

        model.chainSwing(tail, 0.1F, 0.05F, 2, ticks, 0.25F);
        model.chainWave(tail, 0.1F, -0.05F, 1, ticks, 0.25F);

        // model.faceTarget(head, 4, rotationYaw, rotationPitch);
        // model.faceTarget(neck3, 4, rotationYaw, rotationPitch);
        // model.faceTarget(neck2, 4, rotationYaw, rotationPitch);
        // model.faceTarget(neck1, 4, rotationYaw, rotationPitch);
        //
        // model.bob(waist, 1 * globalSpeed, height, false, f, f1);
        // model.walk(waist, 1 * globalSpeed, 0.1F * height, true, -1.5F, 0.05F, f, f1);

        model.faceTarget(rotationYaw, rotationPitch, 1.0F, neck1, neck2, neck3, head);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
