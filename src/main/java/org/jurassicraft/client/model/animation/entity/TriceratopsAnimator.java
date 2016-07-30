package org.jurassicraft.client.model.animation.entity;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.client.model.animation.DinosaurAnimator;
import org.jurassicraft.server.entity.dinosaur.TriceratopsEntity;

@SideOnly(Side.CLIENT)
public class TriceratopsAnimator extends DinosaurAnimator<TriceratopsEntity> {
    @Override
    protected void performAnimations(DinosaurModel model, TriceratopsEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {
        AdvancedModelRenderer head = model.getCube("Head");
        AdvancedModelRenderer neck3 = model.getCube("Neck 3");
        AdvancedModelRenderer neck2 = model.getCube("Neck 2");
        AdvancedModelRenderer neck1 = model.getCube("Neck 1");
        AdvancedModelRenderer shoulders = model.getCube("Body shoulders");
        AdvancedModelRenderer main = model.getCube("Body MAIN");
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
        AdvancedModelRenderer leftThigh = model.getCube("RearLeg Upper Left");
        AdvancedModelRenderer rightThigh = model.getCube("RearLeg Upper Right");

        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelRenderer[] body = new AdvancedModelRenderer[] { head, neck3, neck2, neck1, shoulders, main, waist };

        float globalSpeed = 1.0F;
        float globalDegree = 0.8F;

        model.bob(waist, globalSpeed * 0.5F, globalDegree * 1.5F, false, f, f1);
        model.bob(rightThigh, globalSpeed * 0.5F, globalDegree * 1.5F, false, f, f1);
        model.bob(leftThigh, globalSpeed * 0.5F, globalDegree * 1.5F, false, f, f1);

        model.chainWave(tail, globalSpeed * 0.25F, globalDegree * 0.1F, 1, f, f1);
        model.chainSwing(tail, globalSpeed * 0.25F, globalDegree * 0.25F, 2, f, f1);
        model.chainWave(body, globalSpeed * 0.25F, globalDegree * 0.05F, 3, f, f1);

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

        model.faceTarget(rotationYaw, rotationPitch, 1.0F, neck1, neck2, neck3, head);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
