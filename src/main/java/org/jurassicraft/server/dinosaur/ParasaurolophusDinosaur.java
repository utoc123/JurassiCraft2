package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.entity.dinosaur.ParasaurolophusEntity;
import org.jurassicraft.server.period.TimePeriod;

public class ParasaurolophusDinosaur extends Dinosaur
{
    public ParasaurolophusDinosaur()
    {
        super();

        this.setName("Parasaurolophus");
        this.setDinosaurClass(ParasaurolophusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x9F8138, 0x422306);
        this.setEggColorFemale(0x5F653E, 0x3C3F44);
        this.setHealth(10, 30);
        this.setSpeed(0.35, 0.41);
        this.setStrength(2, 8);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.45F, 2.45F);
        this.setSizeX(0.5F, 2.5F);
        this.setSizeY(0.8F, 3.5F);
        this.setStorage(36);
        this.setDiet(Diet.HERBIVORE);
        this.setBones("ribcage", "front_leg_bones", "hind_leg_bones", "neck_vertebrae", "pelvis", "shoulder_bone", "skull", "tail_vertebrae", "teeth");
        this.setHeadCubeName("Head");
        this.setScale(1.9F, 0.4F);
        this.setImprintable(true);
        this.setAttackBias(-200.0);
    }
}
