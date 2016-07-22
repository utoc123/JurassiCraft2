package org.jurassicraft.client.model.animation;

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
public abstract class DinosaurAnimator<ENTITY extends DinosaurEntity> implements ITabulaModelAnimator<ENTITY> {
    protected EnumMap<GrowthStage, Map<DinosaurEntity, JabelarAnimationHandler>> animationHandlers = new EnumMap<>(GrowthStage.class);

    private JabelarAnimationHandler getAnimationHelper(DinosaurEntity entity, DinosaurModel model, boolean useInertialTweens) {
        GrowthStage growth = entity.getGrowthStage();
        Map<DinosaurEntity, JabelarAnimationHandler> growthToRender = this.animationHandlers.get(growth);

        if (growthToRender == null) {
            growthToRender = new WeakHashMap<>();
            this.animationHandlers.put(growth, growthToRender);
        }

        JabelarAnimationHandler render = growthToRender.get(entity);

        if (render == null) {
            render = entity.getDinosaur().getPoseHandler().createAnimationHandler(entity, model, growth, useInertialTweens);
            growthToRender.put(entity, render);
        }

        return render;
    }

    @Override
    public final void setRotationAngles(TabulaModel model, ENTITY entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        this.getAnimationHelper(entity, (DinosaurModel) model, entity.getUseInertialTweens()).performAnimations(entity, limbSwing, limbSwingAmount, ticks);

        this.performAnimations((DinosaurModel) model, entity, limbSwing, limbSwingAmount, ticks, rotationYaw, rotationPitch, scale);
    }

    protected void performAnimations(DinosaurModel parModel, ENTITY entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
    }
}
