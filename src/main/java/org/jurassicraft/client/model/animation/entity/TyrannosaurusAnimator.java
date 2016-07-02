package org.jurassicraft.client.model.animation.entity;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.client.model.animation.DinosaurAnimator;
import org.jurassicraft.server.entity.dinosaur.TyrannosaurusEntity;

@SideOnly(Side.CLIENT)
public class TyrannosaurusAnimator extends DinosaurAnimator<TyrannosaurusEntity>
{
    @Override
    protected void performMowzieLandAnimations(DinosaurModel model, TyrannosaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
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
//        AdvancedModelRenderer handLeft = model.getCube("Hand Left");
//        AdvancedModelRenderer lowerArmLeft = model.getCube("Lower Arm Left");
//        AdvancedModelRenderer upperArmLeft = model.getCube("Upper Arm Left");
//
//        AdvancedModelRenderer handRight = model.getCube("Hand Right");
//        AdvancedModelRenderer lowerArmRight = model.getCube("Lower Arm Right");
//        AdvancedModelRenderer upperArmRight = model.getCube("Upper Arm Right");
//
//        AdvancedModelRenderer[] tailParts = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };
//        AdvancedModelRenderer[] bodyParts = new AdvancedModelRenderer[] { head, neck5, neck4, neck3, neck2, neck1, chest, stomach, waist };
//        AdvancedModelRenderer[] leftArmParts = new AdvancedModelRenderer[] { handLeft, lowerArmLeft, upperArmLeft };
//        AdvancedModelRenderer[] rightArmParts = new AdvancedModelRenderer[] { handRight, lowerArmRight, upperArmRight };
//
//        model.chainWave(bodyParts, 0.1F, -0.03F, 3, ticks, 0.25F);
//        model.chainWave(rightArmParts, -0.1F, 0.2F, 4, ticks, 0.25F);
//        model.chainWave(leftArmParts, -0.1F, 0.2F, 4, ticks, 0.25F);
//        model.chainSwing(tailParts, 0.1F, 0.05F - (0.05F), 1, ticks, 0.1F);
//        model.chainWave(tailParts, 0.1F, -0.1F, 2, ticks, 0.1F);
//
//        entity.tailBuffer.applyChainSwingBuffer(tailParts);
    }
}
