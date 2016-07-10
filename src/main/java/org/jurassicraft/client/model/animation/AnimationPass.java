package org.jurassicraft.client.model.animation;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.util.Map;

public class AnimationPass
{
    protected final Map<Animation, int[][]> poseSequences;
    protected final AdvancedModelRenderer[][] poses;
    protected float[][] rotationIncrements;
    protected float[][] positionIncrements;
    protected float[][] prevRotationIncrements;
    protected float[][] prevPositionIncrements;
    protected int posesInAnimation;
    protected int currentPoseIndex;
    protected int tweenLength;
    protected float tick;
    protected float prevTicks;
    protected AdvancedModelRenderer[] parts;
    protected AdvancedModelRenderer[] nextParts;
    protected Animation animation;

    protected boolean useInertialTweens;

    protected float inertiaFactor;

    protected float limbSwing;
    protected float limbSwingAmount;

    public AnimationPass(Map<Animation, int[][]> poseSequences, AdvancedModelRenderer[][] poses, boolean useInertialTweens)
    {
        this.poseSequences = poseSequences;
        this.poses = poses;
        this.useInertialTweens = useInertialTweens;
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
        this.initSequence(entity, getRequestedAnimation(entity));
        this.initTweenTicks(entity);

        this.initIncrements(entity);
    }

    public void initPoseModel()
    {
        int[][] pose = this.poseSequences.get(animation);

        if (pose != null)
        {
            this.posesInAnimation = pose.length;

            if (animation == DinosaurAnimation.DYING.get())
            {
                this.currentPoseIndex = this.posesInAnimation - 1;
            }
            else
            {
                this.currentPoseIndex = 0;
            }

            this.nextParts = this.poses[pose[this.currentPoseIndex][0]];
        }
    }

    protected void initIncrements(DinosaurEntity entity)
    {
        for (int partIndex = 0; partIndex < parts.length; partIndex++)
        {
            AdvancedModelRenderer part = parts[partIndex];
            AdvancedModelRenderer nextPart = nextParts[partIndex];

            float[] rotationIncrements = this.rotationIncrements[partIndex];
            float[] positionIncrements = this.positionIncrements[partIndex];

            float animationDegree = getAnimationDegree(entity);

            rotationIncrements[0] = (nextPart.rotateAngleX - (part.defaultRotationX + prevRotationIncrements[partIndex][0])) * animationDegree;
            rotationIncrements[1] = (nextPart.rotateAngleY - (part.defaultRotationY + prevRotationIncrements[partIndex][1])) * animationDegree;
            rotationIncrements[2] = (nextPart.rotateAngleZ - (part.defaultRotationZ + prevRotationIncrements[partIndex][2])) * animationDegree;

            positionIncrements[0] = (nextPart.rotationPointX - (part.defaultPositionX + prevPositionIncrements[partIndex][0])) * animationDegree;
            positionIncrements[1] = (nextPart.rotationPointY - (part.defaultPositionY + prevPositionIncrements[partIndex][1])) * animationDegree;
            positionIncrements[2] = (nextPart.rotationPointZ - (part.defaultPositionZ + prevPositionIncrements[partIndex][2])) * animationDegree;
        }
    }

    public void initSequence(DinosaurEntity entity, Animation animation)
    {
        this.animation = animation;

        if (isEntityAnimationDependent())
        {
            if (this.poseSequences.get(animation) == null)
            {
                this.animation = DinosaurAnimation.IDLE.get();
                entity.setAnimation(DinosaurAnimation.IDLE.get());
            }

            if (this.animation != DinosaurAnimation.IDLE.get() && this.animation == animation) // finished sequence but no new sequence set
            {
                this.animation = DinosaurAnimation.IDLE.get();
                entity.setAnimation(DinosaurAnimation.IDLE.get());
            }
        }
    }

    protected float calculateInertiaFactor()
    {
        float inertiaFactor = tick / tweenLength;

        if (useInertialTweens && DinosaurAnimation.getAnimation(animation).useInertia())
        {
            inertiaFactor = (float) (Math.sin(Math.PI * (inertiaFactor - 0.5D)) * 0.5D + 0.5D);
        }

        return inertiaFactor;
    }

    public void performAnimations(DinosaurEntity entity, float limbSwing, float limbSwingAmount, float ticks)
    {
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;

        Animation requestedAnimation = getRequestedAnimation(entity);

        if (requestedAnimation != animation && animation != DinosaurAnimation.DYING.get())
        {
            this.setNextSequence(entity, requestedAnimation);
        }

        this.performNextTween(entity, ticks);
    }

    public boolean incrementTweenTick(DinosaurEntity entity, float ticks)
    {
        float incrementAmount = (ticks - this.prevTicks) * getAnimationSpeed(entity);

        if (!(DinosaurAnimation.getAnimation(animation).shouldHold() && currentPoseIndex >= posesInAnimation - 1))
        {
            this.tick += incrementAmount;

            return tick >= tweenLength;
        }
        else
        {
            if (tick < tweenLength - 1)
            {
                this.tick += incrementAmount;
            }
            else
            {
                this.tick = tweenLength;
            }

            return false;
        }
    }

    protected void calculateTween()
    {
        this.inertiaFactor = this.calculateInertiaFactor();

        for (int partIndex = 0; partIndex < parts.length; partIndex++)
        {
            if (nextParts == null)
            {
                JurassiCraft.INSTANCE.getLogger().error("Trying to tween to a null next pose array");
            }
            else if (nextParts[partIndex] == null)
            {
                JurassiCraft.INSTANCE.getLogger().error("The part index " + partIndex + " in next pose is null");
            }
            else
            {
                this.applyTweenRotations(partIndex);
                this.applyTweenTranslations(partIndex);
            }
        }
    }

    protected void applyTweenRotations(int partIndex)
    {
        AdvancedModelRenderer part = this.parts[partIndex];

        float[] rotationIncrements = this.rotationIncrements[partIndex];

        part.rotateAngleX += (rotationIncrements[0] * inertiaFactor + prevRotationIncrements[partIndex][0]);
        part.rotateAngleY += (rotationIncrements[1] * inertiaFactor + prevRotationIncrements[partIndex][1]);
        part.rotateAngleZ += (rotationIncrements[2] * inertiaFactor + prevRotationIncrements[partIndex][2]);
    }

    protected void applyTweenTranslations(int partIndex)
    {
        AdvancedModelRenderer part = this.parts[partIndex];

        float[] translationIncrements = this.positionIncrements[partIndex];

        part.rotationPointX += (translationIncrements[0] * inertiaFactor + prevPositionIncrements[partIndex][0]);
        part.rotationPointY += (translationIncrements[1] * inertiaFactor + prevPositionIncrements[partIndex][1]);
        part.rotationPointZ += (translationIncrements[2] * inertiaFactor + prevPositionIncrements[partIndex][2]);
    }

    protected void setNextPoseModel(int poseIndex)
    {
        this.posesInAnimation = poseSequences.get(animation).length;
        this.currentPoseIndex = poseIndex;
        this.nextParts = poses[poseSequences.get(animation)[currentPoseIndex][0]];
    }

    protected void initTweenTicks(DinosaurEntity entity)
    {
        this.startNextTween(entity);

        if (DinosaurAnimation.getAnimation(animation).shouldHold())
        {
            this.tick = this.tweenLength - 1;
        }
        else
        {
            this.tick = 0;
        }
    }

    protected void startNextTween(DinosaurEntity entity)
    {
        int[][] pose = poseSequences.get(animation);

        if (pose != null)
        {
            this.tweenLength = pose[currentPoseIndex][1];

            if (this.tweenLength < 1)
            {
                JurassiCraft.INSTANCE.getLogger().error("Array of sequences has sequence with num ticks illegal value (< 1)");
                this.tweenLength = 1;
            }

            this.tick = 0;

            this.updateTween(entity);
        }
    }

    protected void setNextPoseModel(DinosaurEntity entity)
    {
        this.nextParts = poses[poseSequences.get(animation)[currentPoseIndex][0]];
        this.tweenLength = poseSequences.get(animation)[currentPoseIndex][1];
        this.tick = 0;
        this.prevTicks = 0;
        this.updateTween(entity);
    }

    protected void performNextTween(DinosaurEntity entity, float ticks)
    {
        this.calculateTween();

        if (this.incrementTweenTick(entity, ticks))
        {
            this.handleFinishedPose(entity);
        }

        this.prevTicks = ticks;
    }

    protected void handleFinishedPose(DinosaurEntity entity)
    {
        if (this.incrementCurrentPoseIndex())
        {
            this.setNextSequence(entity, getRequestedAnimation(entity));
        }
        else
        {
            this.updatePreviousPose();
        }

        this.setNextPoseModel(entity);

        this.playSound(entity);
    }

    protected void playSound(DinosaurEntity entity)
    {
        if (getRequestedAnimation(entity) == DinosaurAnimation.IDLE.get() || tick > 0)
        {
            return;
        }

        SoundEvent sound = entity.getSoundForAnimation(getRequestedAnimation(entity));

        if (sound != null)
        {
            entity.playSound(sound, entity.getSoundVolume(), entity.getSoundPitch());
        }
    }

    public boolean incrementCurrentPoseIndex()
    {
        boolean finishedSequence = false;

        this.currentPoseIndex++;

        if (currentPoseIndex >= posesInAnimation)
        {
            DinosaurAnimation animation = DinosaurAnimation.getAnimation(this.animation);

            if (animation != null && animation.shouldHold())
            {
                this.currentPoseIndex = 0;
            }
            else
            {
                this.currentPoseIndex = 0;
                finishedSequence = true;
            }
        }

        return finishedSequence;
    }

    protected void setNextSequence(DinosaurEntity entity, Animation requestedAnimation)
    {
        updatePreviousPose();

        if (poseSequences.get(requestedAnimation) != null && !(animation != DinosaurAnimation.IDLE.get() && animation == requestedAnimation && isEntityAnimationDependent()))
        {
            animation = requestedAnimation;
        }
        else
        {
            animation = DinosaurAnimation.IDLE.get();
        }

        if (isEntityAnimationDependent())
        {
            entity.setAnimation(animation);
        }

        this.setNextPoseModel(0);

        this.startNextTween(entity);
    }

    protected void updateTween(DinosaurEntity entity)
    {
        this.rotationIncrements = new float[parts.length][3];
        this.positionIncrements = new float[parts.length][3];

        this.initIncrements(entity);
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
}
