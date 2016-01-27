package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.entity.EntityLambeosaurus;
import org.jurassicraft.common.entity.base.EnumDiet;
import org.jurassicraft.common.period.EnumTimePeriod;

public class DinosaurLambeosaurus extends Dinosaur
{
    public DinosaurLambeosaurus()
    {
        super();

        this.setName("Lambeosaurus");
        this.setDinosaurClass(EntityLambeosaurus.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS);
        this.setEggColorMale(0x82947A, 0x2F3129);
        this.setEggColorFemale(0x898969, 0x464C3A);
        this.setHealth(18, 75);
        this.setSpeed(0.46, 0.41);
        this.setStrength(6, 36);
        this.setMaximumAge(fromDays(50));
        this.setEyeHeight(0.5F, 3.45F);
        this.setSizeX(0.5F, 2.8F);
        this.setSizeY(0.8F, 4.25F);
        this.setStorage(45);
        this.setDiet(EnumDiet.HERBIVORE);
        this.setBones("skull", "cheek_teeth", "shoulder", "tail_vertebrae", "ribcage", "pelvis", "neck_vertebrae", "hind_leg_bones", "front_leg_bones");
        this.setHeadCubeName("Head");
    }
}