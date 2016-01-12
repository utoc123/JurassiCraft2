package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.entity.EntityTyrannosaurus;
import org.jurassicraft.common.entity.base.EnumDiet;
import org.jurassicraft.common.period.EnumTimePeriod;

public class DinosaurTyrannosaurus extends Dinosaur
{
    public DinosaurTyrannosaurus()
    {
        super();

        this.setName("Tyrannosaurus");
        this.setDinosaurClass(EntityTyrannosaurus.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0x6B6628, 0x39363B);
        this.setEggColorFemale(0xBA997E, 0x7D5D48);
        this.setHealth(10, 80);
        this.setSpeed(0.46, 0.42);
        this.setStrength(5, 35);
        this.setMaximumAge(fromDays(60));
        this.setEyeHeight(0.6F, 3.8F);
        this.setSizeX(0.45F, 4.5F);
        this.setSizeY(0.8F, 4.0F);
        this.setStorage(54);
        this.setDiet(EnumDiet.CARNIVORE);
        this.setBones("arm_bones", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder_bone", "skull", "tail_vertebrae", "tooth");
    }
}
