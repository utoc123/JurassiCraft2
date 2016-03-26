package org.jurassicraft.client.model.animation;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.animation.DinosaurAnimator;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.server.entity.BrachiosaurusEntity;
import org.jurassicraft.server.entity.base.JCEntityRegistry;

@SideOnly(Side.CLIENT)
public class BrachiosaurusAnimator extends DinosaurAnimator<BrachiosaurusEntity>
{
    public BrachiosaurusAnimator()
    {
        super(JCEntityRegistry.brachiosaurus);
    }

    @Override
    protected void performMowzieLandAnimations(DinosaurModel model, BrachiosaurusEntity entity, float f, float f1, float rotation, float rotationYaw, float rotationPitch, float partialTicks)
    {
        // f = entity.ticksExisted;
        // f1 = 0.5F;

        AdvancedModelRenderer head = model.getCube("head");

        AdvancedModelRenderer neck1 = model.getCube("Neck 1");
        AdvancedModelRenderer neck2 = model.getCube("neck2");
        AdvancedModelRenderer neck3 = model.getCube("neck3");
        AdvancedModelRenderer neck4 = model.getCube("neck4");
        AdvancedModelRenderer neck5 = model.getCube("neck5");
        AdvancedModelRenderer neck6 = model.getCube("neck6");

        AdvancedModelRenderer body_1 = model.getCube("hips");
        AdvancedModelRenderer neck7 = model.getCube("neck7");
        AdvancedModelRenderer tail1 = model.getCube("tail1");
        AdvancedModelRenderer tail2 = model.getCube("tail2");
        AdvancedModelRenderer tail3 = model.getCube("tail3");
        AdvancedModelRenderer tail4 = model.getCube("tail4");
        AdvancedModelRenderer tail5 = model.getCube("tail5");

        AdvancedModelRenderer top_leg_left = model.getCube("top leg left");
        AdvancedModelRenderer top_leg_right = model.getCube("top leg right");

        AdvancedModelRenderer bottom_leg_left = model.getCube("bottom front left leg");
        AdvancedModelRenderer bottom_leg_right = model.getCube("bottom front right leg");

        AdvancedModelRenderer left_back_foot = model.getCube("left back foot");
        AdvancedModelRenderer right_back_foot = model.getCube("right back foot");

        AdvancedModelRenderer front_right_top_leg = model.getCube("front right top leg");
        AdvancedModelRenderer front_left_top_leg = model.getCube("front left top leg");

        AdvancedModelRenderer bottom_front_right_leg = model.getCube("bottom front right leg");
        AdvancedModelRenderer bottom_front_left_leg = model.getCube("bottom front left leg");

        AdvancedModelRenderer front_right_foot = model.getCube("front right foot");
        AdvancedModelRenderer front_left_foot = model.getCube("front left foot");

        AdvancedModelRenderer[] neckParts = new AdvancedModelRenderer[] { head, neck7, neck6, neck5, neck4, neck3, neck2, neck1 };
        AdvancedModelRenderer[] tailParts = new AdvancedModelRenderer[] { tail5, tail4, tail3 };
        AdvancedModelRenderer[] tailParts2 = new AdvancedModelRenderer[] { tail5, tail4, tail3, tail2, tail1 };

        float globalSpeed = 0.4F;
        float globalHeight = 0.5F;
        float globalDegree = 0.5F;

        float frontOffset = 1.0F;

        model.bob(body_1, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);
        model.bob(top_leg_left, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);
        model.bob(top_leg_right, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);

        model.chainWave(tailParts, globalSpeed * 1.0F, globalHeight * 0.25F, 3, f, f1);
        model.chainWave(neckParts, globalSpeed * 1.0F, globalHeight * 0.125F, -3, f, f1);

        model.walk(top_leg_left, 1F * globalSpeed, 0.7F * globalDegree, false, 0F, -0.4F, f, f1);
        model.walk(bottom_leg_left, 1F * globalSpeed, 0.6F * globalDegree, true, 1F, 0.5F, f, f1);
        model.walk(left_back_foot, 1F * globalSpeed, 0.6F * globalDegree, false, -1.5F, 0.85F, f, f1);

        model.walk(top_leg_right, 1F * globalSpeed, 0.7F * globalDegree, true, 0F, -0.4F, f, f1);
        model.walk(bottom_leg_right, 1F * globalSpeed, 0.6F * globalDegree, false, 1F, 0.5F, f, f1);
        model.walk(right_back_foot, 1F * globalSpeed, 0.6F * globalDegree, true, -1.5F, 0.85F, f, f1);

        model.walk(front_left_top_leg, 1F * globalSpeed, 0.7F * globalDegree, true, frontOffset + 0F, -0.2F, f, f1);
        model.walk(bottom_front_left_leg, 1F * globalSpeed, 0.6F * globalDegree, true, frontOffset + 1F, -0.2F, f, f1);
        model.walk(front_left_foot, 1F * globalSpeed, 0.6F * globalDegree, false, frontOffset + 2F, 0.8F, f, f1);

        model.walk(front_right_top_leg, 1F * globalSpeed, 0.7F * globalDegree, false, frontOffset + 0F, -0.2F, f, f1);
        model.walk(bottom_front_right_leg, 1F * globalSpeed, 0.6F * globalDegree, false, frontOffset + 1F, -0.2F, f, f1);
        model.walk(front_right_foot, 1F * globalSpeed, 0.6F * globalDegree, true, frontOffset + 2F, 0.8F, f, f1);

        int ticksExisted = entity.ticksExisted;

        model.chainWave(tailParts, globalSpeed * 0.25F, globalHeight * 1.0F, 3, ticksExisted, 0.025F);
        model.chainWave(neckParts, globalSpeed * 0.25F, globalHeight * 0.25F, -3, ticksExisted, 0.025F);

        entity.tailBuffer.applyChainSwingBuffer(tailParts2);
    }
}
