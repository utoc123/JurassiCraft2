package org.jurassicraft.server.entity;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.jurassicraft.client.animation.Animations;
import org.jurassicraft.server.animation.ControlledParam;
import org.jurassicraft.server.entity.ai.animations.JCNonAutoAnimBase;
import org.jurassicraft.server.entity.base.AggressiveDinosaurEntity;

import java.util.Random;

public class TyrannosaurusEntity extends AggressiveDinosaurEntity
{
    private static final Class[] targets = { CompsognathusEntity.class, AnkylosaurusEntity.class, EntityPlayer.class, DilophosaurusEntity.class, DimorphodonEntity.class, DodoEntity.class, LeaellynasauraEntity.class, LudodactylusEntity.class, HypsilophodonEntity.class, GallimimusEntity.class, SegisaurusEntity.class, ProtoceratopsEntity.class, ParasaurolophusEntity.class, OthnieliaEntity.class, MicroceratusEntity.class, TriceratopsEntity.class, StegosaurusEntity.class, BrachiosaurusEntity.class, ApatosaurusEntity.class, RugopsEntity.class, HerrerasaurusEntity.class, VelociraptorEntity.class, SpinosaurusEntity.class, AchillobatorEntity.class, CarnotaurusEntity.class, TherizinosaurusEntity.class, IndominusEntity.class };

    private int stepCount = 0;

    public ControlledParam roarCount = new ControlledParam(0F, 0F, 0.5F, 0F);
    public ControlledParam roarTiltDegree = new ControlledParam(0F, 0F, 1F, 0F);

    public TyrannosaurusEntity(World world)
    {
        super(world);

        animationTasks.addTask(2, new JCNonAutoAnimBase(this, Animations.INJURED.get(), 750));

        for (Class target : targets)
        {
            this.addAIForAttackTargets(target, new Random().nextInt(3) + 1);
        }

        setUseInertialTweens(true);
    }

    @Override
    public String getSoundForAnimation(Animation animation)
    {
        if (animation == Animations.ATTACKING.get())
        {
            return getSound("roar");
        }

        return super.getSoundForAnimation(animation);
    }

    @Override
    public int getTailBoxCount()
    {
        return 6;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        this.roarCount.update();
        this.roarTiltDegree.update();

        /** Step Sound */
        if (this.moveForward > 0 && this.stepCount <= 0)
        {
            this.playSound("jurassicraft:stomp", (float) transitionFromAge(0.1F, 1.0F), this.getSoundPitch());
            stepCount = 65;
        }

        this.stepCount -= this.moveForward * 9.5;
    }
}
