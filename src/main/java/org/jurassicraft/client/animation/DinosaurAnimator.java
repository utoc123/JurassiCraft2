package org.jurassicraft.client.animation;

import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.GrowthStage;

import java.util.EnumMap;
import java.util.Map;
import java.util.WeakHashMap;

@SideOnly(Side.CLIENT)
public abstract class DinosaurAnimator<ENTITY extends DinosaurEntity> implements ITabulaModelAnimator<ENTITY>
{
    protected Map<DinosaurEntity, Map<GrowthStage, JabelarAnimationHandler>> animationHandlers = new WeakHashMap<>();

    private JabelarAnimationHandler getAnimationHelper(DinosaurEntity entity, DinosaurModel model, boolean useInertialTweens)
    {
        GrowthStage growth = entity.getGrowthStage();
        Map<GrowthStage, JabelarAnimationHandler> growthToRender = animationHandlers.get(entity);

        if (growthToRender == null)
        {
            growthToRender = new EnumMap<>(GrowthStage.class);
            animationHandlers.put(entity, growthToRender);
        }

        JabelarAnimationHandler render = growthToRender.get(growth);

        if (render == null)
        {
            render = entity.getDinosaur().getPoseHandler().createAnimationHandler(entity, model, growth, useInertialTweens);
            growthToRender.put(growth, render);
        }

        return render;
    }

    @Override
    public final void setRotationAngles(TabulaModel model, ENTITY entity, float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float partialTicks)
    {
        getAnimationHelper(entity, (DinosaurModel) model, entity.getUseInertialTweens()).performAnimations(partialTicks);

        if (entity.getAnimation() != Animations.DYING.get()) // still alive
        {
            if (entity.isSwimming())
            {
                performMowzieSwimmingAnimations((DinosaurModel) model, entity, limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, partialTicks);
            }
            else
            {
                performMowzieLandAnimations((DinosaurModel) model, entity, limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, partialTicks);
            }
        }
    }

    /*
     * @Override this if you want dino to have cyclical animations.
     */
    protected void performMowzieLandAnimations(DinosaurModel parModel, ENTITY entity, float parLimbSwing, float parLimbSwingAmount, float parRotation, float parRotationYaw, float parRotationPitch, float parPartialTicks)
    {
    }

    /*
     * @Override this if you want swimming dino to have different cyclical animations.
     */
    protected void performMowzieSwimmingAnimations(DinosaurModel parModel, ENTITY entity, float parLimbSwing, float parLimbSwingAmount, float parRotation, float parRotationYaw, float parRotationPitch, float parPartialTicks)
    {
        performMowzieLandAnimations(parModel, entity, parLimbSwing, parLimbSwingAmount, parRotation, parRotationYaw, parRotationPitch, parPartialTicks);
    }
}
