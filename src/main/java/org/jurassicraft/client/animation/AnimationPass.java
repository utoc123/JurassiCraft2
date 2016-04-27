package org.jurassicraft.client.animation;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.util.SoundEvent;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.util.Map;

public class AnimationPass
{
    protected float[][][] rotationIncrements;
    protected float[][][] positionIncrements;
    protected float[][][] offsetIncrements;

    protected float[][] prevRotationIncrements;
    protected float[][] prevPositionIncrements;
    protected float[][] prevOffsetIncrements;

    protected int posesInAnimation;
    protected int currentPoseIndex;
    protected int ticksInTween;
    protected int tweenTick;

    protected int prevTicksExisted;

    protected AdvancedModelRenderer[] parts;
    protected AdvancedModelRenderer[] nextParts;

    protected final Map<Animation, int[][]> poseSequences;
    protected final AdvancedModelRenderer[][] poses;

    protected Animation animation;

    protected boolean useInertialTweens;

    public AnimationPass(Map<Animation, int[][]> poseSequences, AdvancedModelRenderer[][] poses, boolean useIntertialTweens)
    {
        this.poseSequences = poseSequences;
        this.poses = poses;
        this.useInertialTweens = useIntertialTweens;
    }

    public void init(DinosaurEntity entity, AdvancedModelRenderer[] parts)
    {
        this.prevTicksExisted = entity.ticksExisted;

        this.parts = parts;

        this.initPoseModel();
        this.initTweenTicks();

        this.prevRotationIncrements = new float[parts.length][3];
        this.prevPositionIncrements = new float[parts.length][3];
        this.prevOffsetIncrements = new float[parts.length][3];

        this.updatePreviousPose();

        this.initIncrements(0.0F);
    }

    public void initPoseModel()
    {
        int[][] pose = this.poseSequences.get(animation);

        if (pose != null)
        {
            this.posesInAnimation = pose.length;

            if (animation == Animations.DYING.get())
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

    protected void initIncrements(float partialTicks)
    {
        float inertiaFactor = calculateInertiaFactor(partialTicks);

        for (int partIndex = 0; partIndex < parts.length; partIndex++)
        {
            AdvancedModelRenderer part = parts[partIndex];
            AdvancedModelRenderer nextPart = nextParts[partIndex];

            float[] rotationIncrements = this.rotationIncrements[tweenTick][partIndex];
            float[] positionIncrements = this.positionIncrements[tweenTick][partIndex];
            float[] offsetIncrements = this.offsetIncrements[tweenTick][partIndex];

            rotationIncrements[0] = (nextPart.rotateAngleX - (part.defaultRotationX + prevRotationIncrements[partIndex][0])) * inertiaFactor;
            rotationIncrements[1] = (nextPart.rotateAngleY - (part.defaultRotationY + prevRotationIncrements[partIndex][1])) * inertiaFactor;
            rotationIncrements[2] = (nextPart.rotateAngleZ - (part.defaultRotationZ + prevRotationIncrements[partIndex][2])) * inertiaFactor;

            positionIncrements[0] = (nextPart.rotationPointX - (part.defaultPositionX + prevPositionIncrements[partIndex][0])) * inertiaFactor;
            positionIncrements[1] = (nextPart.rotationPointY - (part.defaultPositionY + prevPositionIncrements[partIndex][1])) * inertiaFactor;
            positionIncrements[2] = (nextPart.rotationPointZ - (part.defaultPositionZ + prevPositionIncrements[partIndex][2])) * inertiaFactor;

            offsetIncrements[0] = (nextPart.offsetX - (part.defaultOffsetX + prevOffsetIncrements[partIndex][0])) * inertiaFactor;
            offsetIncrements[1] = (nextPart.offsetY - (part.defaultOffsetY + prevOffsetIncrements[partIndex][1])) * inertiaFactor;
            offsetIncrements[2] = (nextPart.offsetZ - (part.defaultOffsetZ + prevOffsetIncrements[partIndex][2])) * inertiaFactor;
        }
    }

    public void initSequence(DinosaurEntity entity, Animation animation)
    {
        /**
         * TODO:
         * Should control here which animations are interruptible, in which priority
         * I.E. could reject certain changes depending on what current animation is playing
         */

        this.animation = animation;

        if (this.doesUpdateEntityAnimations())
        {
            if (this.poseSequences.get(animation) == null)
            {
                this.animation = Animations.IDLE.get();
                entity.setAnimation(Animations.IDLE.get());
            }
            else if (this.animation != Animations.IDLE.get() && this.animation == animation) // finished sequence but no new sequence set
            {
                this.animation = Animations.IDLE.get();
                entity.setAnimation(Animations.IDLE.get());
            }
            else if (entity.isCarcass())
            {
                this.animation = Animations.DYING.get();
            }
        }
    }

    protected float calculateInertiaFactor(float partialTicks)
    {
        double inertiaFactor = (tweenTick + partialTicks) / ticksInTween;

        if (useInertialTweens)
        {
            inertiaFactor = 0.5D + 0.5D * Math.sin((Math.PI * ((inertiaFactor) - 0.5D)));
        }

        return (float) inertiaFactor;
    }

    public void performAnimations(DinosaurEntity entity, float partialTicks)
    {
        if (entity.getAnimation() != animation && animation != Animations.DYING.get() && this.doesUpdateEntityAnimations())
        {
            this.setNextSequence(entity, entity.getAnimation());
        }

        this.performNextTween(entity, partialTicks);
    }

    /**
     * Increments the current tween tick.
     *
     * @return whether this animation has completed
     */
    public boolean incrementTweenTick()
    {
        if (!Animations.getAnimation(animation).shouldHold())
        {
            tweenTick++;

            return tweenTick >= ticksInTween;
        }
        else
        {
            if (tweenTick < ticksInTween - 1)
            {
                tweenTick++;
            }

            return false;
        }
    }

    protected void calculateTween(float partialTicks)
    {
        this.initIncrements(partialTicks);

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
                this.applyTweenOffsets(partIndex);
            }
        }
    }

    protected void applyTweenRotations(int partIndex)
    {
        AdvancedModelRenderer part = this.parts[partIndex];

        float[] rotationIncrements = this.rotationIncrements[tweenTick][partIndex];

        part.rotateAngleX += (rotationIncrements[0] + prevRotationIncrements[partIndex][0]);
        part.rotateAngleY += (rotationIncrements[1] + prevRotationIncrements[partIndex][1]);
        part.rotateAngleZ += (rotationIncrements[2] + prevRotationIncrements[partIndex][2]);
    }

    protected void applyTweenTranslations(int partIndex)
    {
        AdvancedModelRenderer part = this.parts[partIndex];

        float[] translationIncrements = this.positionIncrements[tweenTick][partIndex];

        part.rotationPointX += (translationIncrements[0] + prevPositionIncrements[partIndex][0]);
        part.rotationPointY += (translationIncrements[1] + prevPositionIncrements[partIndex][1]);
        part.rotationPointZ += (translationIncrements[2] + prevPositionIncrements[partIndex][2]);
    }

    protected void applyTweenOffsets(int partIndex)
    {
        AdvancedModelRenderer part = this.parts[partIndex];

        float[] offsetIncrements = this.offsetIncrements[tweenTick][partIndex];

        part.offsetX += (offsetIncrements[0] + prevOffsetIncrements[partIndex][0]);
        part.offsetY += (offsetIncrements[1] + prevOffsetIncrements[partIndex][1]);
        part.offsetZ += (offsetIncrements[2] + prevOffsetIncrements[partIndex][2]);
    }

    protected void setNextPoseModel(int poseIndex)
    {
        this.prevRotationIncrements = new float[parts.length][3];
        this.prevPositionIncrements = new float[parts.length][3];
        this.prevOffsetIncrements = new float[parts.length][3];

        this.posesInAnimation = poseSequences.get(animation).length;
        this.currentPoseIndex = poseIndex;
        this.nextParts = poses[poseSequences.get(animation)[currentPoseIndex][0]];
    }

    protected void initTweenTicks()
    {
        this.startNextTween();

        if (Animations.getAnimation(animation).shouldHold())
        {
            this.tweenTick = this.ticksInTween - 1;
        }
        else
        {
            this.tweenTick = 0;
        }
    }

    protected void startNextTween()
    {
        int[][] pose = poseSequences.get(animation);

        if (pose != null)
        {
            this.ticksInTween = pose[currentPoseIndex][1];

            if (this.ticksInTween < 1)
            {
                JurassiCraft.INSTANCE.getLogger().error("Array of sequences has sequence with num ticks illegal value (< 1)");
                this.ticksInTween = 1;
            }

            this.tweenTick = 0;

            this.updateTween();
        }
    }

    protected void setNextPoseModel()
    {
        this.nextParts = poses[poseSequences.get(animation)[currentPoseIndex][0]];
        this.ticksInTween = poseSequences.get(animation)[currentPoseIndex][1];
        this.tweenTick = 0;
        this.updateTween();
    }

    protected void performNextTween(DinosaurEntity entity, float partialTicks)
    {
        this.calculateTween(partialTicks);

        for (int i = 0; i < entity.ticksExisted - prevTicksExisted; i++)
        {
            if (this.incrementTweenTick())
            {
                this.handleFinishedPose(entity);
            }
        }

        this.prevTicksExisted = entity.ticksExisted;
    }

    protected void handleFinishedPose(DinosaurEntity entity)
    {
//        this.initIncrements(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

        this.updatePreviousPose();

        if (this.incrementCurrentPoseIndex())
        {
            this.setNextSequence(entity, entity.getAnimation());
        }

        this.setNextPoseModel();

        this.playSound(entity);
    }

    protected void playSound(DinosaurEntity entity)
    {
        if (entity.getAnimation() == Animations.IDLE.get() || tweenTick > 0)
        {
            return;
        }

        SoundEvent sound = entity.getSoundForAnimation(entity.getAnimation());

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
            Animations animation = Animations.getAnimation(this.animation);

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
        /**
         * TODO:
         * Should control here which animations are interruptible, in which priority
         * I.E. could reject certain changes depending on what current animation is playing
         */
        if (this.doesUpdateEntityAnimations())
        {
            if (poseSequences.get(requestedAnimation) != null && !(animation != Animations.IDLE.get() && animation == requestedAnimation))
            {
                animation = requestedAnimation;
            }
            else
            {
                animation = Animations.IDLE.get();
            }

            entity.setAnimation(animation);
        }

        this.setNextPoseModel(0);

        this.startNextTween();
    }

    protected void updateTween()
    {
        this.rotationIncrements = new float[ticksInTween][parts.length][3];
        this.positionIncrements = new float[ticksInTween][parts.length][3];
        this.offsetIncrements = new float[ticksInTween][parts.length][3];
    }

    protected void updatePreviousPose()
    {
        int tween = ticksInTween - 1;

        for (int partIndex = 0; partIndex < parts.length; partIndex++)
        {
            prevRotationIncrements[partIndex][0] += rotationIncrements[tween][partIndex][0];
            prevRotationIncrements[partIndex][1] += rotationIncrements[tween][partIndex][1];
            prevRotationIncrements[partIndex][2] += rotationIncrements[tween][partIndex][2];

            prevPositionIncrements[partIndex][0] += positionIncrements[tween][partIndex][0];
            prevPositionIncrements[partIndex][1] += positionIncrements[tween][partIndex][1];
            prevPositionIncrements[partIndex][2] += positionIncrements[tween][partIndex][2];

            prevOffsetIncrements[partIndex][0] += offsetIncrements[tween][partIndex][0];
            prevOffsetIncrements[partIndex][1] += offsetIncrements[tween][partIndex][1];
            prevOffsetIncrements[partIndex][2] += offsetIncrements[tween][partIndex][2];
        }
    }

    protected boolean doesUpdateEntityAnimations()
    {
        return true;
    }
}
