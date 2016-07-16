package org.jurassicraft.client.model.animation.entity;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.client.model.animation.DinosaurAnimator;
import org.jurassicraft.server.entity.dinosaur.DilophosaurusEntity;

@SideOnly(Side.CLIENT)
public class DilophosaurusAnimator extends DinosaurAnimator<DilophosaurusEntity>
{
    @Override
    protected void performAnimations(DinosaurModel model, DilophosaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer frillLeftBottom = model.getCube("Frill Lower Left");
        AdvancedModelRenderer frillLeftTop = model.getCube("Frill Upper Left");

        AdvancedModelRenderer frillRightBottom = model.getCube("Frill Lower Right");
        AdvancedModelRenderer frillRightTop = model.getCube("Frill Upper Right");

        boolean hasTarget = entity.hasTarget();

        frillLeftBottom.showModel = hasTarget;
        frillLeftTop.showModel = hasTarget;
        frillRightBottom.showModel = hasTarget;
        frillRightTop.showModel = hasTarget;

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

        AdvancedModelRenderer upperArmRight = model.getCube("Right arm");
        AdvancedModelRenderer upperArmLeft = model.getCube("Left arm");

        AdvancedModelRenderer lowerArmRight = model.getCube("Right forearm");
        AdvancedModelRenderer lowerArmLeft = model.getCube("Left forearm");

        AdvancedModelRenderer handRight = model.getCube("Right hand");
        AdvancedModelRenderer handLeft = model.getCube("Left hand");

        AdvancedModelRenderer[] body = new AdvancedModelRenderer[] { head, neck6, neck5, neck4, neck3, neck2, neck1, body1, body2, body3 };
        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };

        AdvancedModelRenderer[] armRight = new AdvancedModelRenderer[] { handRight, lowerArmRight, upperArmRight };
        AdvancedModelRenderer[] armLeft = new AdvancedModelRenderer[] { handLeft, lowerArmLeft, upperArmLeft };

        model.chainWave(tail, 0.15F, -0.03F, 2, ticks, 0.25F);
        model.chainWave(body, 0.15F, 0.03F, 3.5F, ticks, 0.25F);
        model.chainWave(armRight, 0.15F, -0.1F, 4, ticks, 0.25F);
        model.chainWave(armLeft, 0.15F, -0.1F, 4, ticks, 0.25F);
        model.chainSwing(tail, 0.15F, -0.1F, 3, ticks, 0.25F);

        model.faceTarget(rotationYaw, rotationPitch, 1.0F, neck1, head);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
