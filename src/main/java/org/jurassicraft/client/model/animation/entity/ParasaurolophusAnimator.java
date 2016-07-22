package org.jurassicraft.client.model.animation.entity;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.client.model.animation.DinosaurAnimator;
import org.jurassicraft.server.entity.dinosaur.ParasaurolophusEntity;

@SideOnly(Side.CLIENT)
public class ParasaurolophusAnimator extends DinosaurAnimator<ParasaurolophusEntity> {
    @Override
    protected void performAnimations(DinosaurModel model, ParasaurolophusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {
        AdvancedModelRenderer head = model.getCube("Head");

        AdvancedModelRenderer neck1 = model.getCube("Neck1");
        AdvancedModelRenderer neck2 = model.getCube("Neck2");

        AdvancedModelRenderer waist = model.getCube("Body1");

        AdvancedModelRenderer tail1 = model.getCube("Tail1");
        AdvancedModelRenderer tail2 = model.getCube("Tail2");
        AdvancedModelRenderer tail3 = model.getCube("Tail3");
        AdvancedModelRenderer tail4 = model.getCube("Tail4");
        AdvancedModelRenderer tail5 = model.getCube("Tail5");
        AdvancedModelRenderer tail6 = model.getCube("Tail6");

        AdvancedModelRenderer upperArmRight = model.getCube("Upper Arm Right");
        AdvancedModelRenderer lowerArmRight = model.getCube("Lower Arm Right");
        AdvancedModelRenderer rightHand = model.getCube("Right Hand");

        AdvancedModelRenderer upperArmLeft = model.getCube("Upper Arm Left");
        AdvancedModelRenderer lowerArmLeft = model.getCube("Lower Arm Left");
        AdvancedModelRenderer leftHand = model.getCube("Left Hand");

        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };

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
