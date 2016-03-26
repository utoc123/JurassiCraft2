package org.jurassicraft.client.model.animation;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.animation.DinosaurAnimator;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.server.entity.DunkleosteusEntity;
import org.jurassicraft.server.entity.base.EntityHandler;

@SideOnly(Side.CLIENT)
public class DunkleosteusAnimator<ENTITY extends DunkleosteusEntity> extends DinosaurAnimator<ENTITY>
{
    public DunkleosteusAnimator()
    {
        super(EntityHandler.INSTANCE.dunkleosteus);
    }

    @Override
    protected void performMowzieLandAnimations(DinosaurModel model, float f, float f1, float rotation, float rotationYaw, float rotationPitch, float partialTicks, DunkleosteusEntity entity)
    {
        // tail
        AdvancedModelRenderer tail1 = model.getCube("Tail Section 1");
        AdvancedModelRenderer tail2 = model.getCube("Tail Section 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail Section 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail Section 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail Section 5");
        AdvancedModelRenderer tail6 = model.getCube("Tail Section 6");

        // head stoof
        AdvancedModelRenderer head = model.getCube("Main head");

        // flipper
        AdvancedModelRenderer rightFlipper = model.getCube("Right Front Flipper");
        AdvancedModelRenderer leftFlipper = model.getCube("Left Front Flipper");

        // body
        AdvancedModelRenderer body2 = model.getCube("Body Section 2");
        AdvancedModelRenderer body3 = model.getCube("Body Section 3");

        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1, body3, body2, head };

        head.rotationPointX -= -1 * f1 * Math.sin((f + 1) * 0.6);
        model.chainSwing(tail, 0.3F, 0.2F, 3.0D, f, f1);

        model.walk(leftFlipper, 0.6F, 0.6F, false, 0.0F, 0.8F, f, f1);
        model.walk(rightFlipper, 0.6F, 0.6F, false, 0.0F, 0.8F, f, f1);

        model.flap(leftFlipper, 0.6F, 0.6F, false, 0.0F, 0.8F, f, f1);
        model.flap(rightFlipper, 0.6F, 0.6F, true, 0.0F, -0.8F, f, f1);

        int ticksExisted = entity.ticksExisted;

        model.bob(head, 0.04F, 2.0F, false, ticksExisted, 0.25F);
        model.walk(leftFlipper, 0.2F, 0.25F, false, 1.0F, 0.1F, ticksExisted, 0.25F);
        model.walk(rightFlipper, 0.2F, 0.25F, false, 1.0F, 0.1F, ticksExisted, 0.25F);
        model.chainSwing(tail, 0.05F, -0.075F, 1.5D, ticksExisted, 0.25F);
    }
}
