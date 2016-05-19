package org.jurassicraft.client.animation;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.Animation;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.util.Map;

public class AnimationPass
{
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

    protected final Map<Animation, int[][]> poseSequences;
    protected final AdvancedModelRenderer[][] poses;

    protected Animation animation;

    protected boolean useInertialTweens;

    protected float inertiaFactor;

    public AnimationPass(Map<Animation, int[][]> poseSequences, AdvancedModelRenderer[][] poses, boolean useIntertialTweens)
    {
        this.poseSequences = poseSequences;
        this.poses = poses;
        this.useInertialTweens = useIntertialTweens;
    }

    public void init(AdvancedModelRenderer[] parts)
    {
        this.parts = parts;

        this.prevRotationIncrements = new float[parts.length][3];
        this.prevPositionIncrements = new float[parts.length][3];

        this.initPoseModel();
        this.initTweenTicks();

        this.updatePreviousPose();

        this.initIncrements();
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

    protected void initIncrements()
    {
        for (int partIndex = 0; partIndex < parts.length; partIndex++)
        {
            AdvancedModelRenderer part = parts[partIndex];
            AdvancedModelRenderer nextPart = nextParts[partIndex];

            float[] rotationIncrements = this.rotationIncrements[partIndex];
            float[] positionIncrements = this.positionIncrements[partIndex];

            rotationIncrements[0] = (nextPart.rotateAngleX - (part.defaultRotationX + prevRotationIncrements[partIndex][0]));
            rotationIncrements[1] = (nextPart.rotateAngleY - (part.defaultRotationY + prevRotationIncrements[partIndex][1]));
            rotationIncrements[2] = (nextPart.rotateAngleZ - (part.defaultRotationZ + prevRotationIncrements[partIndex][2]));

            positionIncrements[0] = (nextPart.rotationPointX - (part.defaultPositionX + prevPositionIncrements[partIndex][0]));
            positionIncrements[1] = (nextPart.rotationPointY - (part.defaultPositionY + prevPositionIncrements[partIndex][1]));
            positionIncrements[2] = (nextPart.rotationPointZ - (part.defaultPositionZ + prevPositionIncrements[partIndex][2]));
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

    protected float calculateInertiaFactor()
    {
        float inertiaFactor = tick / tweenLength;

        if (useInertialTweens)
        {
            inertiaFactor = (float) (Math.sin(Math.PI * (inertiaFactor - 0.5D)) * 0.5D + 0.5D);
        }

        return inertiaFactor;
    }

    public void performAnimations(DinosaurEntity entity, float ticks)
    {
        if (entity.getAnimation() != animation && animation != Animations.DYING.get() && this.doesUpdateEntityAnimations())
        {
            this.setNextSequence(entity, entity.getAnimation());
        }

        this.performNextTween(entity, ticks);
    }

    public boolean incrementTweenTick(float ticks)
    {
        float incrementAmount = ticks - this.prevTicks;

        if (!Animations.getAnimation(animation).shouldHold())
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

    protected void initTweenTicks()
    {
        this.startNextTween();

        if (Animations.getAnimation(animation).shouldHold())
        {
            this.tick = this.tweenLength - 1;
        }
        else
        {
            this.tick = 0;
        }
    }

    protected void startNextTween()
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

            this.updateTween();
        }
    }

    protected void setNextPoseModel()
    {
        this.nextParts = poses[poseSequences.get(animation)[currentPoseIndex][0]];
        this.tweenLength = poseSequences.get(animation)[currentPoseIndex][1];
        this.tick = 0;
        this.prevTicks = 0;
        this.updateTween();
    }

    protected void performNextTween(DinosaurEntity entity, float ticks)
    {
        this.calculateTween();

        if (this.incrementTweenTick(ticks))
        {
            this.handleFinishedPose(entity);
        }

        this.prevTicks = ticks;
    }

    protected void handleFinishedPose(DinosaurEntity entity)
    {
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
        if (entity.getAnimation() == Animations.IDLE.get() || tick > 0)
        {
            return;
        }

        String sound = entity.getSoundForAnimation(entity.getAnimation());

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
        this.prevRotationIncrements = new float[parts.length][3];
        this.prevPositionIncrements = new float[parts.length][3];

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
        this.rotationIncrements = new float[parts.length][3];
        this.positionIncrements = new float[parts.length][3];

        this.initIncrements();
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

    protected boolean doesUpdateEntityAnimations()
    {
        return true;
    }
}
