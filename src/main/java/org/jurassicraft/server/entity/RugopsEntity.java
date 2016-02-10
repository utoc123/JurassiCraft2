package org.jurassicraft.server.entity;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.AggressiveDinosaurEntity;

public class RugopsEntity extends AggressiveDinosaurEntity // implements IEntityAICreature, ICarnivore
{
    private static final Class[] targets = { CompsognathusEntity.class, AnkylosaurusEntity.class, EntityPlayer.class, DilophosaurusEntity.class, DimorphodonEntity.class, DodoEntity.class, LeaellynasauraEntity.class, LudodactylusEntity.class, HypsilophodonEntity.class, GallimimusEntity.class, SegisaurusEntity.class, ProtoceratopsEntity.class, ParasaurolophusEntity.class, OthnieliaEntity.class, MicroceratusEntity.class, TriceratopsEntity.class, StegosaurusEntity.class };
    private static final Class[] deftargets = { EntityPlayer.class, TyrannosaurusEntity.class, GiganotosaurusEntity.class, SpinosaurusEntity.class };

    public RugopsEntity(World world)
    {
        super(world);
        
        hurtSounds = new String[] { "rugops_hurt_1", "rugops_hurt_2" };
        idleSounds = new String[] { "rugops_living_1", "rugops_living_2", "rugops_living_3", "rugops_living_4" };
        deathSounds = new String[] { "rugops_death_1", "rugops_death_2" };

        for (int i = 0; i < targets.length; i++)
        {
            this.addAIForAttackTargets(targets[i], new Random().nextInt(3) + 1);
        }

        for (int j = 0; j < deftargets.length; j++)
        {
            this.defendFromAttacker(deftargets[j], new Random().nextInt(3) + 1);
        }
    }

    @Override
    public int getTailBoxCount()
    {
        return 6;
    }
}
