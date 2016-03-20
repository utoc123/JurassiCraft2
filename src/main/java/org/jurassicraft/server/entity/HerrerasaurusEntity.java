package org.jurassicraft.server.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.AggressiveDinosaurEntity;

import java.util.Random;

public class HerrerasaurusEntity extends AggressiveDinosaurEntity // implements IEntityAICreature, ICarnivore
{
    private static final Class[] targets = { CompsognathusEntity.class, AnkylosaurusEntity.class, EntityPlayer.class, DilophosaurusEntity.class, DimorphodonEntity.class, DodoEntity.class, LeaellynasauraEntity.class, LudodactylusEntity.class, HypsilophodonEntity.class, GallimimusEntity.class, SegisaurusEntity.class, ProtoceratopsEntity.class, ParasaurolophusEntity.class, OthnieliaEntity.class, MicroceratusEntity.class, TriceratopsEntity.class, StegosaurusEntity.class };
    private static final Class[] deftargets = { EntityPlayer.class, TyrannosaurusEntity.class, GiganotosaurusEntity.class, SpinosaurusEntity.class };

    public HerrerasaurusEntity(World world)
    {
        super(world);

        for (Class target : targets)
        {
            this.addAIForAttackTargets(target, new Random().nextInt(3) + 1);
        }

        for (Class deftarget : deftargets)
        {
            this.defendFromAttacker(deftarget, new Random().nextInt(3) + 1);
        }
    }

    @Override
    public int getTailBoxCount()
    {
        return 6;
    }

    @Override
    public float getSoundVolume()
    {
        return (float) transitionFromAge(1.3F, 2.0F);
    }
}
