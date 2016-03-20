package org.jurassicraft.client.animation;

import java.util.Map;

import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.fx.BloodEntityFX;
import org.jurassicraft.server.tabula.TabulaModelHelper;

import net.ilexiconn.llibrary.client.model.modelbase.MowzieModelRenderer;
import net.ilexiconn.llibrary.common.animation.Animation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author jabelar This class is used to hold per-entity animation variables for use with Jabelar's animation tweening system.
 */
@SideOnly(Side.CLIENT)
public class JabelarAnimationHelper
{
    private final DinosaurEntity animatedEntity;

    private final Minecraft mc;

    private final Map<Animation, int[][]> mapOfPoseSequences;

    private final MowzieModelRenderer[][] arrayOfPoses;
    private MowzieModelRenderer[] theModelRendererArray;
    private MowzieModelRenderer[] nextModelRendererArray;

    private float[][] currentRotationArray;
    private float[][] currentPositionArray;
    private float[][] currentOffsetArray;

    private float[][] rotationIncrementArray;
    private float[][] positionIncrementArray;
    private float[][] offsetIncrementArray;

    private final int numParts;

    private Animation currentPoseSequence;
    private int numPosesInSequence;
    private int currentPoseIndex;
    private int numTicksInTween;
    private int currentTickInTween;
    private float partialTicks;

    private int lastTicksExisted;

    // This enables or disables all inertial tweening. For a living entity like dinosaur
    // you generally want this true. For a non-living entity like a machine, you can make this
    // false for a more robotic movement.
    private final boolean useInertialTweens;
    /**
     * @param parEntity         the entity to animate from
     * @param parModel          the model to animate
     * @param parNumParts
     * @param parArrayOfPoses   for each pose(-index) an array of posed Renderers
     * @param parMapOfSequences maps from an {@link Animations} to the sequence of (pose-index, tween-length)
     * @param parUseInertialTweens
     */
    public JabelarAnimationHelper(
            DinosaurEntity parEntity,
            DinosaurModel parModel,
            int parNumParts,
            MowzieModelRenderer[][] parArrayOfPoses,
            Map<Animation, int[][]> parMapOfSequences,
            boolean parUseInertialTweens
    )
    {
        // transfer static animation info from constructor parameters to instance
        animatedEntity = parEntity;
        numParts = parNumParts;
        arrayOfPoses = parArrayOfPoses;
        mapOfPoseSequences = parMapOfSequences;
        useInertialTweens = parUseInertialTweens;

        lastTicksExisted = animatedEntity.ticksExisted;

        partialTicks = 0.0F;
        mc = Minecraft.getMinecraft();

        init(parModel);

//        JurassiCraft.instance.getLogger().debug("Finished JabelarAnimation constructor");
    }

    public void performJabelarAnimations(float parPartialTicks)
    {

//        JurassiCraft.instance.getLogger().debug("FPS = " + Minecraft.getDebugFPS() + " and current sequence = " + currentSequence + " and current pose = " + this.currentPose + " and current tick = " + this.currentTickInTween + " out of " + numTicksInTween + " and entity ticks existed = " + animatedEntity.ticksExisted + " and partial ticks = " + partialTicks);

        performBloodSpurt();

        // Allow interruption of the animation if it is a new animation and not currently dying
        if (animatedEntity.getAnimation() != currentPoseSequence && currentPoseSequence != Animations.DYING.get())
        {
            setNextSequence(animatedEntity.getAnimation());
        }
        performNextTweenTick();

        partialTicks = parPartialTicks; // need to update this after the call because entity ticks are updated one call after partial ticks
    }

    private void init(DinosaurModel parModel)
    {
        initSequence(animatedEntity.getAnimation());
//        JurassiCraft.instance.getLogger().info("Initializing to animation sequence = " + currentSequence);
        initPoseModel();
        initTweenTicks();

        // copy passed in model into a model renderer array
        // NOTE: this is the array you will actually animate
        theModelRendererArray = convertPassedInModelToModelRendererArray(parModel);

        // initialize the current pose arrays to match the model renderer array
        currentRotationArray = new float[numParts][3];
        currentPositionArray = new float[numParts][3];
        currentOffsetArray = new float[numParts][3];
        updateCurrentPoseArrays();

        // initialize the increment arrays to match difference between current and next pose
        rotationIncrementArray = new float[numParts][3];
        positionIncrementArray = new float[numParts][3];
        offsetIncrementArray = new float[numParts][3];
        updateIncrementArrays();
    }

    private void initSequence(Animation parSequenceIndex)
    {
        // TODO
        // Should control here which animations are interruptible, in which priority
        // I.e. could reject certain changes depending on what current animation is playing

        // handle case where animation sequence isn't available
        if (mapOfPoseSequences.get(parSequenceIndex) == null)
        {
            JurassiCraft.instance.getLogger().error("Requested an anim id " + parSequenceIndex.toString() + " that doesn't have animation sequence in map for entity " + animatedEntity.getEntityId());
            currentPoseSequence = Animations.IDLE.get();
            animatedEntity.setAnimation(Animations.IDLE.get());
        }
        else if (currentPoseSequence != Animations.IDLE.get() && currentPoseSequence == parSequenceIndex) // finished sequence but no new sequence set
        {
//            JurassiCraft.instance.getLogger().debug("Intializing to idle sequence");
            currentPoseSequence = Animations.IDLE.get();
            animatedEntity.setAnimation(Animations.IDLE.get());
        }
        else if (animatedEntity.isCarcass())
        {
            currentPoseSequence = Animations.DYING.get();
        }
        else
        {
            currentPoseSequence = parSequenceIndex;
        }
    }

    private void initPoseModel()
    {
        numPosesInSequence = mapOfPoseSequences.get(currentPoseSequence).length;

        // initialize first pose
        // carcass should init to last pose in dying sequence
        if (animatedEntity.isCarcass())
        {
            currentPoseIndex = numPosesInSequence - 1;
        }
        else
        {
            currentPoseIndex = 0;
        }
        nextModelRendererArray = arrayOfPoses[mapOfPoseSequences.get(currentPoseSequence)[currentPoseIndex][0]];
    }

    private void setNextPoseModel(int parPose)
    {
        numPosesInSequence = mapOfPoseSequences.get(currentPoseSequence).length;

        // initialize first pose
        currentPoseIndex = parPose;
        nextModelRendererArray = arrayOfPoses[mapOfPoseSequences.get(currentPoseSequence)[currentPoseIndex][0]];
    }

    private void initTweenTicks()
    {
        numTicksInTween = mapOfPoseSequences.get(currentPoseSequence)[currentPoseIndex][1];
        // filter out illegal values in array
        if (numTicksInTween < 1)
        {
            JurassiCraft.instance.getLogger().error("Array of sequences has sequence with num ticks illegal value (< 1)");
            numTicksInTween = 1;
        }

        if (animatedEntity.isCarcass())
        {
            currentTickInTween = numTicksInTween - 1;
        }
        else
        {
            currentTickInTween = 0;
        }
    }

    private void startNextTween()
    {
        numTicksInTween = mapOfPoseSequences.get(currentPoseSequence)[currentPoseIndex][1];
        // filter out illegal values in array
        if (numTicksInTween < 1)
        {
            JurassiCraft.instance.getLogger().error("Array of sequences has sequence with num ticks illegal value (< 1)");
            numTicksInTween = 1;
        }
        currentTickInTween = 0;
    }

    private void updateIncrementArrays()
    {
        float inertiaFactor = calculateInertiaFactor();

        for (int partIndex = 0; partIndex < numParts; partIndex++)
        {

            rotationIncrementArray[partIndex][0] = (nextModelRendererArray[partIndex].rotateAngleX - currentRotationArray[partIndex][0]) * inertiaFactor;
            rotationIncrementArray[partIndex][1] = (nextModelRendererArray[partIndex].rotateAngleY - currentRotationArray[partIndex][1]) * inertiaFactor;
            rotationIncrementArray[partIndex][2] = (nextModelRendererArray[partIndex].rotateAngleZ - currentRotationArray[partIndex][2]) * inertiaFactor;
            positionIncrementArray[partIndex][0] = (nextModelRendererArray[partIndex].rotationPointX - currentPositionArray[partIndex][0]) * inertiaFactor;
            positionIncrementArray[partIndex][1] = (nextModelRendererArray[partIndex].rotationPointY - currentPositionArray[partIndex][1]) * inertiaFactor;
            positionIncrementArray[partIndex][2] = (nextModelRendererArray[partIndex].rotationPointZ - currentPositionArray[partIndex][2]) * inertiaFactor;
            offsetIncrementArray[partIndex][0] = (nextModelRendererArray[partIndex].offsetX - currentOffsetArray[partIndex][0]) * inertiaFactor;
            offsetIncrementArray[partIndex][1] = (nextModelRendererArray[partIndex].offsetY - currentOffsetArray[partIndex][1]) * inertiaFactor;
            offsetIncrementArray[partIndex][2] = (nextModelRendererArray[partIndex].offsetZ - currentOffsetArray[partIndex][2]) * inertiaFactor;
        }
    }

    private void updateCurrentPoseArrays()
    {
        for (int partIndex = 0; partIndex < numParts; partIndex++)
        {
            currentRotationArray[partIndex][0] = theModelRendererArray[partIndex].rotateAngleX;
            currentRotationArray[partIndex][1] = theModelRendererArray[partIndex].rotateAngleY;
            currentRotationArray[partIndex][2] = theModelRendererArray[partIndex].rotateAngleZ;
            currentPositionArray[partIndex][0] = theModelRendererArray[partIndex].rotationPointX;
            currentPositionArray[partIndex][1] = theModelRendererArray[partIndex].rotationPointY;
            currentPositionArray[partIndex][2] = theModelRendererArray[partIndex].rotationPointZ;
            currentOffsetArray[partIndex][0] = theModelRendererArray[partIndex].offsetX;
            currentOffsetArray[partIndex][1] = theModelRendererArray[partIndex].offsetY;
            currentOffsetArray[partIndex][2] = theModelRendererArray[partIndex].offsetZ;
        }
    }

    private MowzieModelRenderer[] convertPassedInModelToModelRendererArray(DinosaurModel parModel)
    {
        String[] partNameArray = parModel.getCubeNamesArray();

        MowzieModelRenderer[] modelRendererArray = new MowzieModelRenderer[numParts];

        for (int i = 0; i < numParts; i++)
        {
            modelRendererArray[i] = parModel.getCube(partNameArray[i]);
        }

        return modelRendererArray;
    }

    private void setNextPoseModel()
    {
        nextModelRendererArray = arrayOfPoses[mapOfPoseSequences.get(currentPoseSequence)[currentPoseIndex][0]];
        numTicksInTween = mapOfPoseSequences.get(currentPoseSequence)[currentPoseIndex][1];
        currentTickInTween = 0;
    }

    private void performNextTweenTick()
    {
        // update the passed in model
        tween();

        // since the method is called at rate of twice the display refresh rate
        // need to slow it down to only increment per tick.
        if (animatedEntity.ticksExisted > lastTicksExisted)
        {
            lastTicksExisted = animatedEntity.ticksExisted;

            if (incrementTweenTick()) // increments tween tick and returns true if finished pose
            {
                handleFinishedPose();
            }
        }
    }

    private void tween()
    {
//        JurassiCraft.instance.getLogger().debug("current tween tick +  partial ticks = " + (currentTickInTween + partialTicks));

        for (int partIndex = 0; partIndex < numParts; partIndex++)
        {
            if (nextModelRendererArray == null)
            {
                JurassiCraft.instance.getLogger().error("Trying to tween to a null next pose array");
            }
            else if (nextModelRendererArray[partIndex] == null)
            {
                JurassiCraft.instance.getLogger().error("The part index " + partIndex + " in next pose is null");
            }
            else if (currentRotationArray == null)
            {
                JurassiCraft.instance.getLogger().error("Trying to tween from a null current rotation array");
            }
            else
            {
                isTweenInertial();
                updateIncrementArrays();
                nextTweenRotations(partIndex);
                nextTweenPositions(partIndex);
                nextTweenOffsets(partIndex);
            }
        }
    }

    private boolean isTweenInertial()
    {
        return true;
    }

    private void nextTweenRotations(int parPartIndex)
    {
        theModelRendererArray[parPartIndex].rotateAngleX = currentRotationArray[parPartIndex][0] + rotationIncrementArray[parPartIndex][0];
        theModelRendererArray[parPartIndex].rotateAngleY = currentRotationArray[parPartIndex][1] + rotationIncrementArray[parPartIndex][1];
        theModelRendererArray[parPartIndex].rotateAngleZ = currentRotationArray[parPartIndex][2] + rotationIncrementArray[parPartIndex][2];
    }

    private void nextTweenPositions(int parPartIndex)
    {
        theModelRendererArray[parPartIndex].rotationPointX = currentPositionArray[parPartIndex][0] + positionIncrementArray[parPartIndex][0];
        theModelRendererArray[parPartIndex].rotationPointY = currentPositionArray[parPartIndex][1] + positionIncrementArray[parPartIndex][1];
        theModelRendererArray[parPartIndex].rotationPointZ = currentPositionArray[parPartIndex][2] + positionIncrementArray[parPartIndex][2];
    }

    private void nextTweenOffsets(int parPartIndex)
    {
        theModelRendererArray[parPartIndex].offsetX = currentOffsetArray[parPartIndex][0] + offsetIncrementArray[parPartIndex][0];
        theModelRendererArray[parPartIndex].offsetY = currentOffsetArray[parPartIndex][1] + offsetIncrementArray[parPartIndex][1];
        theModelRendererArray[parPartIndex].offsetZ = currentOffsetArray[parPartIndex][2] + offsetIncrementArray[parPartIndex][2];
    }

    private float calculateInertiaFactor()
    {
        double inertiaFactor = (currentTickInTween + partialTicks) / numTicksInTween;
        if (useInertialTweens)
        {
            inertiaFactor = 0.5D + 0.5D * Math.sin((Math.PI * ((inertiaFactor) - 0.5D)));
        }

//        JurassiCraft.instance.getLogger().debug("inertiaFactor = " + inertiaFactor);
        return (float) inertiaFactor;
    }

    private void handleFinishedPose()
    {
        if (incrementCurrentPoseIndex()) // increments pose and returns true if finished sequence
        {
            setNextSequence(animatedEntity.getAnimation());
        }

        updateCurrentPoseArrays();
        setNextPoseModel();

        playSound();
    }

    private void playSound()
    {
        JurassiCraft.instance.getLogger().info("playSound in state " + animatedEntity.getAnimation() + " for " + animatedEntity.getDinosaur().getName());

        // only play sounds in first tick in tween
        if (currentTickInTween > 0)
        {
            JurassiCraft.instance.getLogger().info("Don't play sound cause currentTickInTween > 0");

            return;
        }

        // also idle sounds are taken care of separately from the pose system
        if (animatedEntity.getAnimation() == Animations.IDLE.get())
        {
            return;
        }

        JurassiCraft.instance.getLogger().info("playSound in state " + animatedEntity.getAnimation() + " for " + animatedEntity.getDinosaur().getName());

        SoundEvent sound = animatedEntity.getSoundForAnimation(animatedEntity.getAnimation());
        
        if (sound != null)
        {
            animatedEntity.playSound(sound, animatedEntity.getSoundVolume(), animatedEntity.getSoundPitch());
        }
    }

    // boolean returned indicates if tween was finished
    public boolean incrementTweenTick()
    {
//        JurassiCraft.instance.getLogger().info("current tween step = " + currentTickInTween);
        currentTickInTween++;

        return currentTickInTween >= numTicksInTween;
    }

    // boolean returned indicates if sequence was finished
    public boolean incrementCurrentPoseIndex()
    {
        boolean finishedSequence = false;

        // increment current sequence step
        currentPoseIndex++;
        // check if finished sequence
        if (currentPoseIndex >= numPosesInSequence)
        {
            Animations animation = Animations.getAnimation(animatedEntity.getAnimation());

            if (animation != null && animation.shouldHold()) // hold last dying pose indefinitely
            {
                currentPoseIndex--;
            }
            else
            {
                currentPoseIndex = 0;
                finishedSequence = true;
            }
        }

//        JurassiCraft.instance.getLogger().debug("Next pose is pose = " + currentPose);
        return finishedSequence;
    }

    // will return value of -1 if this is last pose in sequence
    private int getNextPoseIndex()
    {
        int nextPoseIndex = currentPoseIndex++;
        // check if finished sequence
        if (nextPoseIndex >= numPosesInSequence)
        {
            nextPoseIndex = -1;
        }

//        JurassiCraft.instance.getLogger().debug("Next pose is pose = " + nextPoseIndex);
        return nextPoseIndex;
    }

    private void setNextSequence(Animation parPoseSequenceIndex)
    {
        // TODO
        // Should control here which animations are interruptible, in which priority
        // I.e. could reject certain changes depending on what current animation is playing

        // handle case where animation sequence isn't available
        if (mapOfPoseSequences.get(parPoseSequenceIndex) == null)
        {
            JurassiCraft.instance.getLogger().error("Requested an anim id " + parPoseSequenceIndex.animationId + " (" + Animations.getAnimation(parPoseSequenceIndex).toString() + ") that doesn't have animation sequence in map for entity " + animatedEntity.getEntityId());
            currentPoseSequence = Animations.IDLE.get();
        }
        else if (currentPoseSequence != Animations.IDLE.get() && currentPoseSequence == parPoseSequenceIndex) // finished sequence but no new sequence set
        {
//            JurassiCraft.instance.getLogger().debug("Reverting to idle sequence");
            currentPoseSequence = Animations.IDLE.get();
        }
        else
        {
//            JurassiCraft.instance.getLogger().debug("Setting new sequence to " + parSequenceIndex);
            currentPoseSequence = parPoseSequenceIndex;
        }

        animatedEntity.setAnimation(currentPoseSequence);
        setNextPoseModel(0);
        startNextTween();

//        if (currentSequence != Animations.IDLE)
//        {
//            JurassiCraft.instance.getLogger().debug("current sequence for entity ID " + animatedEntity.getEntityId() + " is " + currentSequence + " out of " + mapOfSequences.size() + " and current pose " + currentPose + " out of " + mapOfSequences.get(currentSequence).length + " with " + numTicksInTween + " ticks in tween");
//        }
    }

    public int getCurrentPose()
    {
        return currentPoseIndex;
    }

    public static DinosaurModel getTabulaModel(String tabulaModel, int geneticVariant)
    {
        // catch the exception so you can call method without further catching
        try
        {
            return new DinosaurModel(TabulaModelHelper.parseModel(tabulaModel), null); // okay to use null for animator
            // parameter as we get animator
            // from passed-in model
        }
        catch (Exception e)
        {
            JurassiCraft.instance.getLogger().error("Could not load Tabula model = " + tabulaModel);
        }

        return null;
    }

    public DinosaurModel getTabulaModel(String tabulaModel)
    {
        return getTabulaModel(tabulaModel, 0);
    }

    private void performBloodSpurt()
    {
        double posX = animatedEntity.posX;
        double posY = animatedEntity.posY;
        double posZ = animatedEntity.posZ;

        World world = animatedEntity.worldObj;

        EffectRenderer effectRenderer = mc.effectRenderer;

        if (animatedEntity.hurtTime == animatedEntity.maxHurtTime - 1)
        {
            float entityWidth = animatedEntity.width;
            float entityHeight = animatedEntity.height;

            float amount = 2;

            for (int x = 0; x < amount; x++)
            {
                for (int y = 0; y < amount; y++)
                {
                    for (int z = 0; z < amount; z++)
                    {
                        addBloodEffect(world, effectRenderer, (x / amount * entityWidth) + posX - (entityWidth / 2.0F), (y / amount * entityHeight) + posY, (z / amount * entityWidth) + posZ - (entityWidth / 2.0F));
                    }
                }
            }
        }
    }

    private void addBloodEffect(World world, EffectRenderer effectRenderer, double x, double y, double z)
    {
        effectRenderer.addEffect((new BloodEntityFX(world, x + 0.5D, y + 0.5D, z + 0.5D, 0, 0, 0)).setBlockPos(new BlockPos(x, y, z)));
    }
}
