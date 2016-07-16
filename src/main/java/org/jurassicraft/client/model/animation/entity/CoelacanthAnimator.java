package org.jurassicraft.client.model.animation.entity;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.client.model.animation.DinosaurAnimator;
import org.jurassicraft.server.entity.dinosaur.disabled.CoelacanthEntity;

@SideOnly(Side.CLIENT)
public class CoelacanthAnimator extends DinosaurAnimator<CoelacanthEntity>
{
    @Override
    protected void performAnimations(DinosaurModel model, CoelacanthEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        // NOTES: Because the animation does not need to be synced to the ground, global variables are not needed.

        // NOTES: The thing about aquatic creatures is that they are literally tails. Their whole bodies, including their heads, are single tails. Treat them as such.
        // tail
        AdvancedModelRenderer head = model.getCube("headJoint"); // NOTES: Add in joint boxes to make sure rotations are level. You'll know when you need to!
        AdvancedModelRenderer neck = model.getCube("neckJoint");
        AdvancedModelRenderer body1 = model.getCube("Body Section 1");
        AdvancedModelRenderer body2 = model.getCube("Body Section 2");
        AdvancedModelRenderer body3 = model.getCube("Body Section 3");
        AdvancedModelRenderer tail1 = model.getCube("Tail Section 1");
        AdvancedModelRenderer tail2 = model.getCube("Tail Section 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail Section 3");
        // flipper
        AdvancedModelRenderer leftFlipper = model.getCube("Left Front Flipper");
        AdvancedModelRenderer rightFlipper = model.getCube("Right Front Flipper");

        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail3, tail2, tail1, body3, body2, body1, neck, head };

        // NOTES: A fish's movement involves moving its head side to side, which sends a wave impulse down its tail. Its fins move back in forth and up and down in a symmetrical rythm, too.
        head.rotationPointX -= -4 * f1 * Math.sin((f + 1) * 0.6); // Head moves side to side
        model.chainSwing(tail, 0.6F, 0.4F, 3.0D, f, f1); // and the tail follows with a delay.

        model.walk(leftFlipper, 0.6F, 0.6F, false, 0.0F, 0.8F, f, f1);
        model.walk(rightFlipper, 0.6F, 0.6F, false, 0.0F, 0.8F, f, f1);

        model.flap(leftFlipper, 0.6F, 0.6F, false, 0.0F, 0.8F, f, f1);
        model.flap(rightFlipper, 0.6F, 0.6F, true, 0.0F, -0.8F, f, f1);

        model.bob(head, 0.04F, 2.0F, false, ticks, 0.25F);
        model.walk(leftFlipper, 0.2F, 0.25F, false, 1.0F, 0.1F, ticks, 0.25F);
        model.walk(rightFlipper, 0.2F, 0.25F, false, 1.0F, 0.1F, ticks, 0.25F);
        model.chainSwing(tail, 0.05F, -0.075F, 1.5D, ticks, 0.25F);
        // ((CoelacanthEntity)entity).tailBuffer.applyChainSwingBuffer(this.bodyParts); //Tail buffer does not exist right now. Apply once added.
    }
}
