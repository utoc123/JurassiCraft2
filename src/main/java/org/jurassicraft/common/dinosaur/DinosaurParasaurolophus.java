package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.entity.EntityParasaurolophus;
import org.jurassicraft.common.entity.base.EnumDiet;
import org.jurassicraft.common.period.EnumTimePeriod;

public class DinosaurParasaurolophus extends Dinosaur
{
    public DinosaurParasaurolophus()
    {
        super();

        this.setName("Parasaurolophus");
        this.setDinosaurClass(EntityParasaurolophus.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0x9F8138, 0x422306);
        this.setEggColorFemale(0x5F653E, 0x3C3F44);
        this.setHealth(16, 65);
        this.setSpeed(0.46, 0.41);
        this.setStrength(6, 36);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.45F, 2.45F);
        this.setSizeX(0.5F, 2.5F);
        this.setSizeY(0.8F, 3.5F);
        this.setStorage(36);
        this.setDiet(EnumDiet.HERBIVORE);
        this.setBones("ribcage", "front_leg_bones", "hind_leg_bones", "neck_vertebrae", "pelvis", "shoulder_bone", "skull", "tail_vertebrae", "teeth");
    }
}
