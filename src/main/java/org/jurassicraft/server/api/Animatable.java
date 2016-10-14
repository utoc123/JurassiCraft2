package org.jurassicraft.server.api;

import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityLivingBase;
import org.jurassicraft.client.model.animation.PoseHandler;
import org.jurassicraft.server.entity.GrowthStage;

public interface Animatable extends IAnimatedEntity {
    boolean isCarcass();
    boolean isMoving();
    boolean isClimbing();
    boolean isInWater();
    boolean isSwimming();
    boolean isRunning();
    boolean isInLava();
    boolean canUseGrowthStage(GrowthStage growthStage);
    boolean isMarineCreature();
    boolean shouldUseInertia();
    boolean isSleeping();
    GrowthStage getGrowthStage();
    <ENTITY extends EntityLivingBase & Animatable> PoseHandler<ENTITY> getPoseHandler();
}
