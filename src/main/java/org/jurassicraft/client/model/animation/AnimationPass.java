package org.jurassicraft.client.model.animation;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.Animation;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.util.Map;

public class AnimationPass
{
    protected final Map<Animation, int[][]> animations;
    protected final PosedCuboid[][] poses;
    protected float[][] rotationIncrements;
    protected float[][] positionIncrements;
    protected float[][] prevRotationIncrements;
    protected float[][] prevPositionIncrements;
    protected int poseCount;
    protected int poseIndex;
    protected int poseLength;

    protected float animationTick;
    protected float prevTicks;

    protected AdvancedModelRenderer[] parts;
    protected PosedCuboid[] pose;

    protected Animation animation;

    protected boolean useInertia;

    protected float inertiaFactor;

    protected float limbSwing;
    protected float limbSwingAmount;

    public AnimationPass(Map<Animation, int[][]> animations, PosedCuboid[][] poses, boolean useInertia)
    {
        this.animations = animations;
        this.poses = poses;
        this.useInertia = useInertia;
    }

    public void init(AdvancedModelRenderer[] parts, DinosaurEntity entity)
    {
        this.parts = parts;

        this.prevRotationIncrements = new float[parts.length][3];
        this.prevPositionIncrements = new float[parts.length][3];
        this.rotationIncrements = new float[parts.length][3];
        this.positionIncrements = new float[parts.length][3];

        this.animation = DinosaurAnimation.IDLE.get();
        this.initPoseModel();
        this.initAnimation(entity, getRequestedAnimation(entity));
        this.initAnimationTicks(entity);

        this.initIncrements(entity);
    }

    public void initPoseModel()
    {
        int[][] pose = this.animations.get(animation);

        if (pose != null)
        {
            this.poseCount = pose.length;

            this.poseIndex = 0;

            this.pose = this.poses[pose[this.poseIndex][0]];
        }
    }

    protected void initIncrements(DinosaurEntity entity)
    {
        for (int partIndex = 0; partIndex < parts.length; partIndex++)
        {
            AdvancedModelRenderer part = parts[partIndex];
            PosedCuboid nextPose = this.pose[partIndex];

            float[] rotationIncrements = this.rotationIncrements[partIndex];
            float[] positionIncrements = this.positionIncrements[partIndex];

            float animationDegree = getAnimationDegree(entity);

            rotationIncrements[0] = (nextPose.rotationX - (part.defaultRotationX + prevRotationIncrements[partIndex][0])) * animationDegree;
            rotationIncrements[1] = (nextPose.rotationY - (part.defaultRotationY + prevRotationIncrements[partIndex][1])) * animationDegree;
            rotationIncrements[2] = (nextPose.rotationZ - (part.defaultRotationZ + prevRotationIncrements[partIndex][2])) * animationDegree;

            positionIncrements[0] = (nextPose.positionX - (part.defaultPositionX + prevPositionIncrements[partIndex][0])) * animationDegree;
            positionIncrements[1] = (nextPose.positionY - (part.defaultPositionY + prevPositionIncrements[partIndex][1])) * animationDegree;
            positionIncrements[2] = (nextPose.positionZ - (part.defaultPositionZ + prevPositionIncrements[partIndex][2])) * animationDegree;
        }
    }

    public void initAnimation(DinosaurEntity entity, Animation animation)
    {
        this.animation = animation;

        if (this.animations.get(animation) == null)
        {
            this.animation = DinosaurAnimation.IDLE.get();
        }

        if (this.animation != DinosaurAnimation.IDLE.get() && this.animation == animation)
        {
            this.animation = DinosaurAnimation.IDLE.get();
        }
    }

    protected float calculateInertiaFactor()
    {
        float inertiaFactor = animationTick / poseLength;

        if (useInertia && DinosaurAnimation.getAnimation(animation).useInertia())
        {
            inertiaFactor = (float) (Math.sin(Math.PI * (inertiaFactor - 0.5D)) * 0.5D + 0.5D);
        }

        return Math.min(1.0F, Math.max(0.0F, inertiaFactor));
    }

    public void performAnimations(DinosaurEntity entity, float limbSwing, float limbSwingAmount, float ticks)
    {
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;

        Animation requestedAnimation = getRequestedAnimation(entity);

        if (requestedAnimation != animation)
        {
            this.setAnimation(entity, requestedAnimation);
        }

        if (poseIndex >= poseCount)
        {
            poseIndex = poseCount - 1;
        }

        this.inertiaFactor = this.calculateInertiaFactor();

        for (int partIndex = 0; partIndex < parts.length; partIndex++)
        {
            if (pose == null)
            {
                JurassiCraft.INSTANCE.getLogger().error("Trying to animate to a null pose array");
            }
            else if (pose[partIndex] == null)
            {
                JurassiCraft.INSTANCE.getLogger().error("The part index " + partIndex + " in next pose is null");
            }
            else
            {
                this.applyRotations(partIndex);
                this.applyTranslations(partIndex);
            }
        }

        if (this.updateAnimationTick(entity, ticks))
        {
            this.handleFinishedPose(entity, ticks);
        }

        this.prevTicks = ticks;
    }

    public boolean updateAnimationTick(DinosaurEntity entity, float ticks)
    {
        float incrementAmount = (ticks - this.prevTicks) * getAnimationSpeed(entity);

        if (!DinosaurAnimation.getAnimation(animation).shouldHold() || poseIndex < poseCount)
        {
            this.animationTick += incrementAmount;

            if (animationTick >= poseLength)
            {
                animationTick = poseLength;

                return true;
            }

            return false;
        }
        else
        {
            if (animationTick < poseLength)
            {
                this.animationTick += incrementAmount;

                if (animationTick >= poseLength)
                {
                    animationTick = poseLength;
                }
            }
            else
            {
                this.animationTick = poseLength;
            }

            return false;
        }
    }

    protected void applyRotations(int partIndex)
    {
        AdvancedModelRenderer part = this.parts[partIndex];

        float[] rotationIncrements = this.rotationIncrements[partIndex];

        part.rotateAngleX += (rotationIncrements[0] * inertiaFactor + prevRotationIncrements[partIndex][0]);
        part.rotateAngleY += (rotationIncrements[1] * inertiaFactor + prevRotationIncrements[partIndex][1]);
        part.rotateAngleZ += (rotationIncrements[2] * inertiaFactor + prevRotationIncrements[partIndex][2]);
    }

    protected void applyTranslations(int partIndex)
    {
        AdvancedModelRenderer part = this.parts[partIndex];

        float[] translationIncrements = this.positionIncrements[partIndex];

        part.rotationPointX += (translationIncrements[0] * inertiaFactor + prevPositionIncrements[partIndex][0]);
        part.rotationPointY += (translationIncrements[1] * inertiaFactor + prevPositionIncrements[partIndex][1]);
        part.rotationPointZ += (translationIncrements[2] * inertiaFactor + prevPositionIncrements[partIndex][2]);
    }

    protected void setPose(int poseIndex)
    {
        this.poseCount = animations.get(animation).length;
        this.poseIndex = poseIndex;
        this.pose = poses[animations.get(animation)[this.poseIndex][0]];
    }

    protected void initAnimationTicks(DinosaurEntity entity)
    {
        this.startAnimation(entity);

        if (DinosaurAnimation.getAnimation(animation).shouldHold())
        {
            this.animationTick = this.poseLength;
        }
        else
        {
            this.animationTick = 0;
        }
    }

    protected void startAnimation(DinosaurEntity entity)
    {
        int[][] pose = animations.get(animation);

        if (pose != null)
        {
            this.poseLength = Math.max(1, pose[poseIndex][1]);

            this.animationTick = 0;

            this.initIncrements(entity);
        }
    }

    protected void setPose(DinosaurEntity entity, float ticks)
    {
        this.pose = poses[animations.get(animation)[poseIndex][0]];
        this.poseLength = animations.get(animation)[poseIndex][1];
        this.animationTick = 0;
        this.prevTicks = ticks;
        this.initIncrements(entity);
    }

    protected void handleFinishedPose(DinosaurEntity entity, float ticks)
    {
        if (this.incrementCurrentPoseIndex())
        {
            this.setAnimation(entity, isEntityAnimationDependent() ? DinosaurAnimation.IDLE.get() : getRequestedAnimation(entity));
        }
        else
        {
            this.updatePreviousPose();
        }

        this.setPose(entity, ticks);
    }

    public boolean incrementCurrentPoseIndex()
    {
        this.poseIndex++;

        if (poseIndex >= poseCount)
        {
            DinosaurAnimation animation = DinosaurAnimation.getAnimation(this.animation);

            if (animation != null && animation.shouldHold())
            {
                this.poseIndex = poseCount - 1;
            }
            else
            {
                this.poseIndex = 0;
                return true;
            }
        }

        return false;
    }

    protected void setAnimation(DinosaurEntity entity, Animation requestedAnimation)
    {
        this.updatePreviousPose();

        if (animations.get(requestedAnimation) != null && !(animation != DinosaurAnimation.IDLE.get() && animation == requestedAnimation && !this.isLooping()))
        {
            animation = requestedAnimation;
        }
        else
        {
            animation = DinosaurAnimation.IDLE.get();
        }

        this.setPose(0);

        this.startAnimation(entity);
    }

    protected void updatePreviousPose()
    {
        for (int partIndex = 0; partIndex < parts.length; partIndex++)
        {
            prevRotationIncrements[partIndex][0] += rotationIncrements[partIndex][0] * inertiaFactor;
            prevRotationIncrements[partIndex][1] += rotationIncrements[partIndex][1] * inertiaFactor;
            prevRotationIncrements[partIndex][2] += rotationIncrements[partIndex][2] * inertiaFactor;

            prevPositionIncrements[partIndex][0] += positionIncrements[partIndex][0] * inertiaFactor;
            prevPositionIncrements[partIndex][1] += positionIncrements[partIndex][1] * inertiaFactor;
            prevPositionIncrements[partIndex][2] += positionIncrements[partIndex][2] * inertiaFactor;
        }
    }

    protected float getAnimationSpeed(DinosaurEntity entity)
    {
        return 1.0F;
    }

    protected float getAnimationDegree(DinosaurEntity entity)
    {
        return 1.0F;
    }

    protected Animation getRequestedAnimation(DinosaurEntity entity)
    {
        return entity.getAnimation();
    }

    protected boolean isEntityAnimationDependent()
    {
        return true;
    }

    public boolean isLooping()
    {
        return false;
    }
}
